## Working with data reactively

### This project aims to:
- Create a reactive data repository
- Properly work with a reactive data store: R2DBC
- Fetch data reactively
- Leverage Spring Data to help manage our content

All parts of the system must be reactive. Otherwise, we risk a blocking call tying up a thread and clobbering our throughput. 
Project Reactor’s default thread pool size is the number of cores on the operating machine. That’s because context switching is expensive. By having no more threads than cores, we’re guaranteed to never have to suspend a thread, save its state, activate another thread, and restore its state.

#### Dependencies needed to build a reactive relational data story implementing the R2DBC specification:
- spring-boot-starter-data-r2dbc: Spring Boot’s starter for Spring Data R2DBC
- h2: The third-party embeddable database
- r2dbc-h2: The Spring team’s R2DBC driver for H2

### Fetching data reactively

### Creating a reactive data repository
```java
/**
 * Interface definition that declares:
 * - The name for our Spring Data repository
 * - Spring Data Commons’ base interface for any reactive repository, ReactiveCrudRepository as a parent interface
 * - The domain and primary key’s type for this repository
 */
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Long> {}
```

### Using R2DBC

#### Loading data with R2dbcEntityTemplate
Startup Configuration responsibilities:
- Flags a collection of bean definitions, needed to autoconfigure our application.
- Turns the initDatabase() method into a Spring bean, added to the application context.
  - Defines our Spring Data R2DBC schema once the application is started inside Spring Boot’s functional interface for an object.
  - Carries out the SQL statement that creates an EMPLOYEE table with a self-incrementing id field in H2's dialect using the underlying DatabaseClient
  - Verifies each step using Reactor Test’s operator
  - Injects a copy of this Spring Data R2DBC bean, R2dbcEntityTemplate, so we can load test data.
  - Coerces a Java 8 lambda function into CommandLineRunner.

  
  
#### Returning data reactively to an API controller
Api controller responsibilities:
- Serializing request mapping outputs to the HTML response, instead of processing templates
- Handling mapped HTTP GET /api/employees calls in a method that fetches all data using the prebuilt findAll method from Spring Data Commons’ ReactiveCrudRepository interface<br>
- Handling mapped POST /api/employees calls in a method that 
  - processes and deserializes the incoming request body into an Employee object only when the system is ready,
  - ensures a completely new entry will be made to the database,
  - and returns the result Mono<Employee> from the executed save operation with a newly created Employee object inside


#### Reactively dealing with data in a template
Home controller responsibilities:
- Rendering templates
- Generating the web template served up at the root of our domain with the data returned by EmployeeRepository's findAll() method.<br>
- Handling mapped HTTP POST /api/new-employee calls in a method that processes the form-backed Employee bean by converting the outcome of the save() operation to the redirect request.