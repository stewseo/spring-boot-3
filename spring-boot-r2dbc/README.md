## Working with data reactively

### spring-data-r2dbc aims to:
- Explain what it means to fetch data reactively.
- Build an introductory reactive Spring Boot application.
  - Leverage Spring Data to help manage our content.
  - Create a reactive data repository, Spring Data Commons’ base interface for any reactive repository, ReactiveCrudRepository.
  - Load test data by injecting a copy of our Spring Data R2DBC bean(R2dbcEntityTemplate) to the application's context.
  - Return data reactively to our API controller that will be used to generate our web template served up at the root of our domain.

### Fetching data reactively

Choosing a database that needs to be accessed reactively and fits our needs, is a difficult task. 

All parts of the system must be reactive. Otherwise, we risk a blocking call tying up a thread and clobbering our throughput.

Because context switching is expensive, Project Reactor’s default thread pool size is the number of cores on the operating machine . By having no more threads than cores, we’re guaranteed to never have to suspend a thread, save its state, activate another thread, and restore its state.

By taking such an expensive operation off the table, reactive applications can instead focus on the more effective tactic of simply going back to Reactor’s runtime for the next task (a.k.a. work stealing). However, this is only possible when we use Reactor’s Mono and Flux types along with their various operators.

### Dependencies needed to build a reactive relational data store implementing the R2DBC specification:
- spring-boot-starter-data-r2dbc: Spring Boot’s starter for Spring Data R2DBC
- h2: The third-party embeddable database
- r2dbc-h2: The Spring team’s R2DBC driver for H2

### Creating a reactive data repository
```java
/**
* Interface definition that declares:<br>
* - The name for our Spring Data repository<br>
* - Spring Data Commons’ base interface for any reactive repository, ReactiveCrudRepository as it's parent interface<br>
* - The domain and primary key’s type for this repository
    */
    public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Long> {}
```

```java
/**
* This class defines our domain’s type, as required in the EmployeeRepository declaration.
  */
  public class Employee {
  /**
  * The primary key of type Long is denoted by Spring Data Commons’ annotation, @Id.<br>
  *  Note that this is NOT JPA’s jakarta.persistence.Id annotation but instead a Spring Data-specific annotation.
   */
     private @Id Long id;
     private String name;
     private String role;
public Employee(String name, String role) {
this.name = name;
this.role = role;
}
// setters, getters, equals, hashcode, toString...
}
```

### Loading data with R2dbcEntityTemplate
- Our Startup configuration class is flagged as a collection of bean definitions, needed to autoconfigure our application.
- This class' initDatabase() method is turned into a Spring bean, added to the application context.
- Inside this Java 8 lambda function that is coerced into a CommandLineRunner, we:
  - Define our Spring Data R2DBC schema set up the schema by carrying out a SQL statement that creates an EMPLOYEE table with a self-incrementing id field in H2's dialect.
  - Define 3 typesafe insert operations with a provided type parameter.
  - Force execution of our reactive flow using Reactor Test.
  - Verify that we receive an expected count, indicating that the operation was successful.
  - Ensure that we receive Reactive Streams onComplete signals.

### Returning data reactively to an API controller
#### Hook up our reactive data supply to an API controller:
```java
/**
 * This controller class directly serializes request mapping outputs to the HTML response, instead of processing templates.<br>
 * Injects the EmployeeRepository through constructor injection<br>
 */
@RestController
public class ApiController {

  private final EmployeeRepository repository;

  public ApiController(EmployeeRepository repository) {
    this.repository = repository;
  }

  /**
   * Maps HTTP GET /api/employees calls to this method.
   * @return all data using the prebuilt findAll method from Spring Data Commons’ ReactiveCrudRepository interface.
   */
  @GetMapping("/api/employees")
  Flux<Employee> employees() {
    return repository.findAll();
  }

  /**
   * Maps HTTP POST /api/employees calls to this method.
   * @param newEmployee processes and deserializes the incoming request body into an Employee object only when the system is ready.<p>
   *                    Ensures a completely new entry will be made to the database
   * @return the result Mono<Employee> from the executed save operation with a newly created Employee object inside. This new object includes a fresh id field.
   */

  @PostMapping("/api/employees")
  Mono<Employee> add(@RequestBody Mono<Employee> newEmployee) {
    return newEmployee.flatMap(e -> {
      Employee employeeToLoad = new Employee(e.getName(), e.getRole());
      return repository.save(employeeToLoad);
    });
  }
}
```


### Reactively dealing with data in a template

#### Create a class that is focused on rendering html templates:
```java
/**
 * This controller class is focused on rendering templates.<p>
 * EmployeeRepository is injected into this controller using constructor injection.
 */
@Controller
public class HomeController {
```

#### Generate the web template served up at the root of our domain with data returned by EmployeeRepository's findAll() method:
```java
  /**
   * Uses the Flux returned from EmployeeRepository's findAll() method to gather a stream of items into Mono<List<Employee>>
   * Invokes the map operation to access the list inside that Mono where we then transform it into a Rendering.
   * @return the constructed Mono<Rendering> through the build() step that transforms all the pieces into a final, immutable instance.
   */
  @GetMapping("/")
  Mono<Rendering> index() {
    return repository.findAll() //
      .collectList() //
      .map(employees -> Rendering //
        .view("index") //
        .modelAttribute("employees", employees) //
        .modelAttribute("newEmployee", new Employee("", "")) //
        .build());
  }
```

#### Process that form-backed Employee bean in a POST-based web method:
```java
  /**
   * @param newEmployee Extracts the name and role from the incoming Employee object but ignores any possible id value, since we’re inserting a new entry.<p>
   *                    Flattens/flatMaps the Mono<Employee> results returned by our repository’s save() method.
   *                    Translates the saved Employee object into a redirect request.
   * @return Mono<String> 
   */
  @PostMapping("/new-employee")
  Mono<String> newEmployee(@ModelAttribute Mono<Employee> newEmployee) {
    return newEmployee //
      .flatMap(e -> {
        Employee employeeToSave = new Employee(e.getName(), e.getRole());
        return repository.save(employeeToSave);
      }) //
      .map(employee -> "redirect:/");
  }
```

