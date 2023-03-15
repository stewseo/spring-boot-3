## Querying for Data with Spring Boot

### This projects aims to demonstrate and explain how to:
- Query for data using Spring Data JPA
    -  Writing custom finders, using Query By Example, and directly accessing the data store using custom JPQL and SQL
- Hook variations of queries into search boxes.
- Use Java 17 records to quickly assemble DTOs to ferry form requests into web methods and onto VideoService.
- Assess when it makes sense to use various querying tactics.


### Understanding a fundamental paradigm: data transfer objects (DTOs) versus entities versus plain old Java objects (POJOs)
- DTO: A class whose purpose is to transfer data, usually from server to client (or vice versa)
- Entity: A class whose purpose is to store/retrieve data to/from a data store
- POJO: A class that doesn’t extend any framework code nor has any sort of restrictions baked into it

#### Entities
- Every class involved with storing and retrieving data through JPA must be annotated with @Entity.
- JPA wraps entity objects returned from queries with proxies to let the JPA provider track updates so that it knows when to actually push updates out to the data store (known as flushing) and also helps it to better handle entity caching.
```java
/**
 * This class is annotated as a JPA-managed type.
 * The flagged primary key is of type Long.
 * Primary key generation is offloaded to the JPA provider.
 * The protected no-argument constructor satisfies one of JPA's entity requirements
 * This class includes a constructor designed for creating new entries in the database where the id field isn’t provided. 
 *  When the id field is null, it tells JPA we want to create a new row in the table.
 */
@Entity 
class VideoEntity {
  private @Id @GeneratedValue Long id; 
  private String name; 
  private String description; 
  protected VideoEntity() { 
    this(null, null); 
  } 
  VideoEntity(String name, String description) { 
    this.id = null; 
    this.description = description; 
    this.name = name; 
  } 
  // getters and setters 
}
```

#### DTOs
- DTOs, on the other hand:
  - Are typically used in the web layer of applications.
  - Are more concerned with taking data and ensuring it’s properly formatted for either HTML generation or JSON handling.
  - Aren’t confined to JSON. Using XML or any other form of data exchange format has the same need of ensuring proper formatting of data.
  - Are a way to implement the single-responsibility principle (SRP): Classes are easier to maintain and update if they try to concentrate on doing just one task.
  - Should only have one stakeholder, the web layer.

#### POJOs
- Spring’s concept of registering beans with an application context makes it possible to avoid writing user code that extends any framework code. Registered beans with a built-in lifecycle opens the door to wrapping POJO-based objects with proxies that allow the application of services.
- Being able to register a service with the application context, allows us to wrap a bean with a proxy that applies Spring’s TransactionTemplate to every method call made from an outside caller, making it easy to unit test that service, ensuring it did its job while making transactional support a configuration step that the service doesn't even have to know about.

### Creating a Spring Data repository

#### Spring Data provides the means to read the metadata of the data store and perform query derivation:
```java
/**
 * Interface extending JpaRepository with two generic parameters: VideoEntity and Long (the domain type and the primary key type)
 * JpaRepository, a Spring Data JPA interface, contains a set of already supported Change Replace Update Delete (CRUD) operations
  */
public interface VideoRepository extends JpaRepository 
  <VideoEntity, Long> { 
}
```
- Class hierarchy ends with Repository, a marker interface with nothing inside it.
- Spring Data is coded to look for all Repository instances and apply its various query derivation tactics, meaning any interface we create that extends Repository or any of its subinterfaces will be picked up by Spring Boot’s component scanning and automatically registered for us to use.

#### Generic types in various repository signatures:
- ID: The generic type of the repository’s primary key
- T: The generic type of the repository’s immediate domain type
- S: The generic subtype that extends T (sometimes used for projection types)

#### Container types:
- Iterable: An iterable type that does not require all its elements to be fully realized in memory
- Example: An object used to serve Query By Example

### Using custom finders
```
  /**
   * Custom finder method that Spring Data implements by parsing the method name.
   * @param partialName
   * @return a List of type <Video Entity>, indicating it must return a list of the repository’s domain type.
   */
  List<VideoEntity> findByNameContainsIgnoreCase(String partialName);
```
- This interface method is all we need to write a query.
  - The magic of Spring Data is that it will parse the method name.
  - All repository methods that start with findBy are flagged as queries.
  - After that, it looks for field names (Name) with some optional qualifiers (Containing and/or IgnoreCase).
  - Since this is a field, it expects there to be a corresponding argument (String name), which doesn’t matter.
- Spring Data JPA will
  - translate this method signature into SELECT video.* FROM VideoEntity video WHERE video.name = ?1,
  - perform proper binding on the incoming argument to avoid SQL injection attacks,
  - and will convert every row coming back into a VideoEntity object.

#### First Search box in our templates/index.mustache file: When a user enters search criteria in either box and clicks Submit, it will POST a form to /multi-field-search.
```
<form action="/multi-field-search" method="post">
  <label for="name">Name:</label>
  <input type="text" name="name">
  <label for="description">Description:</label>
  <input type="text" name="description">
  <button type="submit">Search</button>
</form>
```
- The action denotes /multi-field-searchas the target URL, with an HTTP method of POST
- There is a label and a text input for both name and description
- The button labeled Search will actuate the whole form

#### A Java 17 record defining the lightweight data type that mustache needs to collect the name and description fields:
```java
/**
 * This Java 17 record has two String fields—name and description — that perfectly match up with the names defined in our HTML form's /multi-field-search
 */
record VideoSearch(String name, String description) {
}
```

#### VideoService method to perform a search using the transported details of the request:
```java
  /**
   * @param videoSearch containing user entered data containing both name and description details, only the name field, or only the description field
   * @return list of VideoEntity objects
   */
  public List<VideoEntity> search(VideoSearch videoSearch) {
    // Checks that both fields of the VideoSearch record contain actual text and are neither empty nor null using Spring Framework utility class, StringUtils
    if (StringUtils.hasText(videoSearch.name()) //
            && StringUtils.hasText(videoSearch.description())) {
      return repository //
              .findByNameContainsOrDescriptionContainsAllIgnoreCase( // invokes a custom finder that matches the name field and the description field, but with the Contains qualifiers and the AllIgnoreCase modifier
                      videoSearch.name(), videoSearch.description());
    }
    // If either field is empty (or null) check if the name field has text. If so, invoke the custom finder matching on name with the Contains and IgnoreCase qualifiers
    if (StringUtils.hasText(videoSearch.name())) {
      return repository.findByNameContainsIgnoreCase(videoSearch.name());
    }
    // Also, check whether the description field has text. If so, use the custom finder that matches on description with Contains and IgnoreCase qualifiers.
    if (StringUtils.hasText(videoSearch.description())) {
      return repository.findByDescriptionContainsIgnoreCase(videoSearch.description());
    }
    return Collections.emptyList();
  }
```
- This series of if clauses is a clunky and verbose. Using QueryByExample could be a more concise, readable solution.

### Using Query By Example

#### Our 1 input universal search box in index.mustache:

```
<form action="/universal-search" method="post"> 
  <label for="value">Search:</label> 
  <input type="text" name="value"> 
  <button type="submit">Search</button> 
</form>
```

#### To transport the input data in our universal search box, we can wrap it with a DTO.
```java
record UniversalSearch(String value) { }
```

#### web method to process this new UniversalSearch
```java
  /**
   * Maps HTTP post requests /universal-search to this method. 
   * Processes the incoming form, captured in the single-value UniversalSearch type.
   * @param search DTO is passed on to the videoService search() method.
   * @param model search results are stored in the Model field to be rendered by the index template.
   * @return index the name of the template to render.
   */
  @PostMapping("/universal-search")
  public String universalSearch(@ModelAttribute UniversalSearch search, Model model) {
    List<VideoEntity> searchResults = videoService.search(search); //
    model.addAttribute("videos", searchResults); //
    return "index";
  }
 
```
#### Leverage Query By Example by creating a VideoService.search() method that takes in one value and applies it to all the fields:
```java
  /**
   * Creates a probe based on the same domain type as the repository and copies the value attribute into the probe’s Name and Description fields, but only if there is text. If the value attribute is empty, the fields are left null.
   * Assembles an Example<VideoEntity> using and in addition to providing the probe, also provides additional criteria of ignoring the casing and applying a CONTAINING match, which puts wildcards on both sides of every input.
   * Matches any, an Or operation, since the same criteria is put in all fields.
   * @param search the UniversalSearch DTO.
   * @return List<VideoEntity> the result finding all using this example.
   */
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
### Using custom JPA
```java
  /**
 * Supplies a custom JPQL statement that joins four different tables together, using standard inner joins.<br>
 * Binds named parameters :minimumViews and :minimumLikes to the method arguments by the Spring Data @Param("minimumViews") and @Param("minimumLikes") annotations.<br>
 * (instead of the default positional parameters)
 * @return List<VideoEntity>, Spring Data will form a collection.
 */

@Query(
        """
                select v FROM VideoEntity v
                JOIN v.metrics m
                JOIN m.activity a
                JOIN v.engagement e
                WHERE a.views < :minimumViews
                OR e.likes < :minimumLikes
                """
)
  List<VideoEntity> findVideosThatArentPopular(//
@Param("minimumViews") Long minimumViews, //
@Param("minimumLikes") Long minimumLikes);
```