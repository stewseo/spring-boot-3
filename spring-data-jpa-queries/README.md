## Querying for Data with Spring Boot

This project aims to demonstrate different approaches to query data using Spring Boot, including:

- Querying for data using Spring Data JPA:
  - Writing custom finders.
  - Using Query By Example.
  - Directly accessing the data store using custom JPQL and SQL.
- Using Java 17 records to quickly assemble DTOs that can be used to ferry form requests into web methods and onto VideoService to simplify development.
- Assessing when it makes sense to use different querying tactics by understanding the trade-offs and benefits of each approach.

### Understanding a fundamental paradigm: data transfer objects (DTOs) versus entities versus plain old Java objects (POJOs)
- DTO is a class whose purpose is to transfer data, usually from server to client (or vice versa)
- Entity is a class whose purpose is to store/retrieve data to/from a data store
- POJO is a class that doesn’t extend any framework code nor has any sort of restrictions baked into it

#### Entities
Every class involved with storing and retrieving data through JPA must be annotated with @Entity. JPA wraps entity objects returned from queries with proxies to let the JPA provider track updates so that it knows when to actually push updates out to the data store (known as flushing) and also helps it to better handle entity caching.

#### DTOs, on the other hand:
- Are typically used in the web layer of applications.
- Are more concerned with taking data and ensuring it’s properly formatted for either HTML generation or JSON handling.
- Aren’t confined to JSON. Using XML or any other form of data exchange format has the same need of ensuring proper formatting of data.
- Are a way to implement the Single-Responsibility Principle (SRP): Classes are easier to maintain and update if they try to concentrate on doing just one task.
- Should only have one stakeholder, the web layer.

#### POJOs
Spring’s concept of registering beans with an application context makes it possible to avoid writing user code that extends any framework code. Registered beans with a built-in lifecycle opens the door to wrapping POJO-based objects with proxies that allow the application of services. Being able to register a service with the application context allows us to wrap a bean with a proxy that applies Spring’s TransactionTemplate to every method call made from an outside caller, making it easy to unit test that service, ensuring it did its job while making transactional support a configuration step that the service doesn't even have to know about.

### Creating a Spring Data repository

#### Spring Data reads the metadata of the data store and performs query derivation:
```java
public interface VideoRepository extends JpaRepository 
  <VideoEntity, Long> { 
}
```
- VideoRepository extends JpaRepository, which provides a set of methods for working with VideoEntity instances.
- Spring Data query derivation allows you to define custom query methods in VideoRepository using a naming convention.
- The naming convention uses keywords such as find, findBy, and findAllBy followed by property names to generate queries automatically.
- For example, findByTitle(String title) generates a query that selects all VideoEntity instances with the specified title.
- Keywords and operators, such as And, Or, Between, LessThan, GreaterThan, and Like can be used to create more complex queries.

#### Generic types in various repository signatures:
- ID: The generic type of the repository’s primary key
- T: The generic type of the repository’s immediate domain type
- S: The generic subtype that extends T (sometimes used for projection types)

#### Container types:
- Iterable: An iterable type that does not require all its elements to be fully realized in memory
- Example: An object used to serve Query By Example

### Using custom finders
- Custom finders in Spring Data JPA can be created by defining methods in the repository interface that follow a specific naming convention.
- The naming convention for custom finders follows the format "find{Entity}By{Property}ContainingIgnoreCase".
#### Example of a custom finder in Spring Data JPA that finds all VideoEntity instances whose name contains a specified partial name, regardless of case:
```
  List<VideoEntity> findByNameContainsIgnoreCase(String partialName);
```
- The method returns a list of VideoEntity instances, specified by "List<VideoEntity>".
- "findByName" tells Spring Data JPA to create a query that finds VideoEntity instances based on their name property.
- "Contains" specifies that the name property should contain the specified partial name.
- "IgnoreCase" tells Spring Data JPA to ignore the case of the partial name when performing the query.
- When this method is called on a VideoRepository instance, Spring Data JPA will generate a query that looks for all VideoEntity instances whose name property contains the specified partial name, ignoring case.
- Writing a query involves Spring Data parsing the method name, flagging all repository methods that start with "findBy" as queries, and looking for field names (such as "Name") with some optional qualifiers (such as "Containing" and/or "IgnoreCase").
- Spring Data JPA will then translate the method signature into a SELECT statement, perform proper binding on the incoming argument to avoid SQL injection attacks, and convert every row coming back into a VideoEntity object.

#### Search box in our templates/index.mustache file:
```
<form action="/multi-field-search" method="post">
  <label for="name">Name:</label>
  <input type="text" name="name">
  <label for="description">Description:</label>
  <input type="text" name="description">
  <button type="submit">Search</button>
</form>
```
- The form action attribute denotes /multi-field-search as the target URL, with an HTTP method of POST
- There is a label and a text input for both name and description
- The button labeled Search will actuate the whole form

#### A Java 17 record defining the lightweight data type that Mustache needs to collect the name and description fields:
```java
record VideoSearch(String name, String description) {
}
```
- This Java 17 record has two String fields—name and description—that perfectly match up with the names defined in our HTML form's /multi-field-search

#### VideoService method to perform a search using the transported details of the request:
```java
  public List<VideoEntity> search(VideoSearch videoSearch) {
    if (StringUtils.hasText(videoSearch.name()) //
            && StringUtils.hasText(videoSearch.description())) {
      return repository //
              .findByNameContainsOrDescriptionContainsAllIgnoreCase( // invokes a custom finder that matches the name field and the description field, but with the Contains qualifiers and the AllIgnoreCase modifier
                      videoSearch.name(), videoSearch.description());
    }
    if (StringUtils.hasText(videoSearch.name())) {
      return repository.findByNameContainsIgnoreCase(videoSearch.name());
    }
    if (StringUtils.hasText(videoSearch.description())) {
      return repository.findByDescriptionContainsIgnoreCase(videoSearch.description());
    }
    return Collections.emptyList();
  }
```
- This method takes a VideoSearch object containing user-entered data containing both name and description details, only the name field, or only the description field
- The method returns a list of VideoEntity objects
- This series of if clauses is clunky and verbose. Using QueryByExample could be a more concise, readable solution.


### Using Query By Example

#### Query by Example, Creating a Probe, and Wrapping a Probe with Example
- Query By Example allows for a flexible search criteria where the exact criteria for a query varies from request to request.
- We can use Query By Example to create a probe, which is an instance of the domain object with fields populated with criteria we want to apply and leave the ones we aren’t interested in empty (null).
- We can then wrap the probe with Example, creating an Example<VideoEntity>, which allows us to use methods such as findOne and findAll to retrieve the desired data.
- ExampleMatcher is a feature of Query By Example that allows us to alter the search criteria, including making queries case-insensitive and applying filters.
- By wrapping the probe with Example and using ExampleMatcher, we can find tricky answers by exactly matching only the non-null fields.

### Creating a Universal Search Box with HTTP POST Method

#### To create a Universal Search Box that allows users to enter their search query and submit it to the server using the HTTP POST method, add the following to our index.mustache:
```
<form action="/universal-search" method="post">
  <label for="value">Search:</label>
  <input type="text" name="value">
  <button type="submit">Search</button>
</form>
```
- The target URL is /universal-search.
- There is only one input field named value.

#### To transport this input data, we need to wrap it with a Data Transfer Object (DTO). Using Java 17 records, we create a simple DTO named UniversalSearch:
```
public record UniversalSearch(String value) {}
```
- The UniversalSearch DTO contains only one entry, value.

### Creating a Universal Search Handler

#### To process this new UniversalSearch DTO, we need to create a new web method:
```java
@PostMapping("/universal-search")
public String universalSearch(
  @ModelAttribute UniversalSearch search, Model model) {
    List<VideoEntity> searchResults =
      videoService.search(search);
  model.addAttribute("videos", searchResults);
  return "index";
}
```
- It responds to /universal-search.
- The incoming form is captured in the single-value UniversalSearch type.
- The search DTO is passed on to the search() method of the videoService instance.
- The search results are stored in the Model field to be rendered by the index template.

### Leveraging Query by Example for Universal Search

#### We can leverage Query by Example to create a VideoService.search() method that takes in one value and applies it to all the relevant fields:
```java
public List<VideoEntity> search(UniversalSearch search) {
  VideoEntity probe = new VideoEntity();
  if (StringUtils.hasText(search.value())) {
    probe.setName(search.value());
    probe.setDescription(search.value());
  }
  Example<VideoEntity> example = Example.of(probe, //
    ExampleMatcher.matchingAny() //
      .withIgnoreCase() //
      .withStringMatcher(StringMatcher.CONTAINING));
  return repository.findAll(example);
}
```
- Takes in the UniversalSearch DTO.
- Creates a probe based on the same domain type as the repository and copies the value attribute into the probe’s Name and Description fields, but only if there is text. If the value attribute is empty, the fields are left null.
- Assembles an Example<VideoEntity> using the Example.of() static method. In addition to providing the probe, it also provides additional criteria of ignoring the casing and applying a CONTAINING match, which puts wildcards on both sides of every input.
- Since the same criteria is applied to all fields, it uses matchingAny() to perform an OR operation.


### Using custom JPA

#### Custom JPQL statement using @Query annotation:
```java
@Query("select v from VideoEntity v where v.name = ?1") 
List<VideoEntity> findCustomerReport(String name);
```
- The JPQL statement selects VideoEntity objects where the name attribute is equal to the first positional binding parameter, which is tied to the method argument using ?1.
- The return type of the method is a List of VideoEntity objects.
- @Query essentially bypasses Spring Data JPA's query derivation tactics and uses the custom query provided by the user.
- Spring Data JPA will still apply ordering and paging to the result.
- If the custom query is too simplistic, a predefined query method such as findByName(String name) can be used instead.

#### @Query annotation used to write a custom JPQL statement that joins four tables using standard inner joins:
```java
@Query("select v FROM VideoEntity v " // 
    + "JOIN v.metrics m " // 
    + "JOIN m.activity a " // 
    + "JOIN v.engagement e " // 
    + "WHERE a.views < :minimumViews " // 
    + "OR e.likes < :minimumLikes") 
List<VideoEntity> findVideosThatArentPopular( // 
    @Param("minimumViews") Long minimumViews, // 
    @Param("minimumLikes") Long minimumLikes);
```
- Named parameters :minimumViews and :minimumLikes are used and bound to method arguments using @Param annotations from Spring Data.
- The method findVideosThatArentPopular(Long minimumViews, Long minimumLikes) is used to execute the JPQL query and returns a List of VideoEntity.
- A comparable custom finder for this query could be findByMetricsActivityViewsLessThanOrEngagementLikesLessThan(Long minimumViews, Long minimumLikes).
- Choosing between custom finders and @Query can be difficult, with a custom finder being preferred for simple queries, but as queries become more complex, it is more beneficial to write the query by hand using @Query.
- If JPQL is too limiting, @Query's nativeQuery=true argument can be used to write pure SQL.