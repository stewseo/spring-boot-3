### Creating a reactive application using Project Reactor, rolling out a reactive web method to both serve and consume JSON, and how to leverage Thymeleaf to reactively generate HTML and consume an HTML form

Reactive Javascript toolkits in the browser, an environment where there is only one thread, have shown that single threaded environment can scale and perform with power if approached properly.
“Reactive Streams is an initiative to provide a standard for asynchronous stream processing with non-blocking back pressure. This encompasses efforts aimed at runtime environments (JVM and JavaScript) as well as network protocols.”

### Reactive Streams
- Addresses the concern of fast data streams overrunning the stream’s destination by introducing a concept known as Backpressure that replaces the traditional publish-subscribe paradigm with a pull-based system. Downstream consumers reach back to publishers and have the power to ask for any number of units to process that it is ready to handle.
- The mechanism of communication in reactive streams is called signals. Every time data is handled or actions are taken, they are associated with a signal. Even if there is no data exchange, signals are still handled because even with no data results, there is still the need to send and receive signals.
- Can communicate over the network in a purely reactive way, with proper control

### Dependencies
- spring-boot-starter-webflux: Spring Boot’s starter that pulls in Spring WebFlux, Jackson for JSON serialization/deserialization, and Reactor Netty as our reactive web server
- spring-boot-starter-test: Spring Boot’s starter for testing, including unconditionally for all projects
- reactor-test: Project Reactor’s test module with additional tools to help test reactive apps, automatically included with any reactive app
- Thymeleaf : A templating engine that doesn’t block and renders HTML is Thymeleaf.

Reactor Netty is a Project Reactor library that wraps non-blocking Netty with Reactor hooks so our web container does not get stuck on blocking APIs.

### Serving data with a reactive GET method
```java
// Spring Web’s annotation to indicate that this controller involves data, not templates
@RestController
public class ApiController {

  // maps HTTP GET /api/employeesweb calls onto this method
  // Flux is Reactor’s implementation of Publisher and provides reactive operators
  // returns a Flux of Employee records
  @GetMapping("/api/employees")
  Flux<Employee> employees() {
    return Flux.just( //
            new Employee("alice", "management"), //
            new Employee("bob", "payroll"));
  }
}
```

### Consuming incoming data with a reactive POST method
```java
  // maps HTTP POST /api/employees web calls to this method
  // tells Spring Web to deserialize the incoming HTTP request body into an Employee data type
  // returns reactor’s alternative to Flux for a single item: Mono<Employee>
  @PostMapping("/api/employees")
  Mono<Employee> add(@RequestBody Mono<Employee> newEmployee) {
    return newEmployee //
      .map(employee -> {
        DATABASE.put(employee.name(), employee);
        return employee;
      });
  }
```

### Serving a reactive template

#### Creating a reactive web controller focused on serving templates

#### Crafting a Thymeleaf template

### Creating hypermedia reactively

