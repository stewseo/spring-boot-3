### Reactive Programming in Spring Boot 3

#### This project aims to:
- Create a reactive application using Project Reactor
- Roll out a reactive web method to both serve and consume JSON
- Leverage Thymeleaf to reactively generate HTML and consume an HTML form
- Reactively generate a hypermedia-aware API using Spring HATEOAS

### Reactive
- Reactive Javascript toolkits in the browser, an environment where there is only one thread, have shown that single threaded environment can scale and perform with power if approached properly.
- “Reactive Streams is an initiative to provide a standard for asynchronous stream processing with non-blocking back pressure. This encompasses efforts aimed at runtime environments (JVM and JavaScript) as well as network protocols.”

### Reactive Streams
- Addresses the concern of fast data streams overrunning the stream’s destination by introducing a concept known as Backpressure that replaces the traditional publish-subscribe paradigm with a pull-based system. Downstream consumers reach back to publishers and have the power to ask for any number of units to process that it is ready to handle.
- The mechanism of communication in reactive streams is called signals. Every time data is handled or actions are taken, they are associated with a signal. Even if there is no data exchange, signals are still handled because even with no data results, there is still the need to send and receive signals.
- Can communicate over the network in a purely reactive way, with proper control

#### Dependencies
- spring-boot-starter-webflux: Spring Boot’s starter that pulls in Spring WebFlux, Jackson for JSON serialization/deserialization, and Reactor Netty as our reactive web server
  - Reactor Netty is a Project Reactor library that wraps non-blocking Netty with Reactor hooks so our web container does not get stuck on blocking APIs.
- spring-boot-starter-test: Spring Boot’s starter for testing, including unconditionally for all projects
- reactor-test: Project Reactor’s test module with additional tools to help test reactive apps, automatically included with any reactive app
- Thymeleaf : A templating engine that doesn’t block and renders HTML is Thymeleaf.

### Serving data with a reactive GET method
```java
// Spring Web’s annotation to indicate that this controller involves data, not templates
@RestController
public class ApiController {

  // maps HTTP GET /api/employeesweb calls onto this method
  // Flux is Reactor’s implementation of Publisher and provides reactive operators
  // returns a Flux of org.example.springdatacassandraobservability.Employee records
  @GetMapping("/api/employees")
  Flux<org.example.springdatacassandraobservability.Employee> employees() {
    return Flux.just( //
            new org.example.springdatacassandraobservability.Employee("alice", "management"), //
            new org.example.springdatacassandraobservability.Employee("bob", "payroll"));
  }
}
```

### Consuming incoming data with a reactive POST method
```java
  // maps HTTP POST /api/employees web calls to this method
  // Wraps incoming data inside Reactor’s alternative to Flux for a single item, Mono.
  // returns a Mono<org.example.springdatacassandraobservability.Employee>, the single-item counterpart to a Reactor Flux
  @PostMapping("/api/employees")
  Mono<org.example.springdatacassandraobservability.Employee> add(@RequestBody Mono<org.example.springdatacassandraobservability.Employee> newEmployee) {
    return newEmployee //
      .map(employee -> {
        DATABASE.put(employee.name(), employee);
        return employee;
      });
  }
```

### Serving a reactive template

#### Creating a reactive web controller focused on serving templates
```java
// This class contains web methods that render templates.
@Controller
public class HomeController {

  // Maps GET / web calls onto this method.
  // Wraps a Java list of org.example.springdatacassandraobservability.Employee objects(canned data stored in java map) into a Flux using fromIterable
  // Transforms List<org.example.springdatacassandraobservability.Employee> into a Rendering, while keeping everything inside this Mono
  //  by unpacking the original Mono<List<org.example.springdatacassandraobservability.Employee>> and using the results to create a new Mono<Rendering>
  // returns Mono<Rendering>: Reactor’s single-valued reactive type
  @GetMapping("/")
  Mono<Rendering> index() {
    return Flux.fromIterable(DATABASE.values()) //
            .collectList() //
            .map(employees -> Rendering //
                    .view("index") //
                    .modelAttribute("employees", employees) //
                    .modelAttribute("newEmployee", new org.example.springdatacassandraobservability.Employee("", "")) //
                    .build());
  }
```
#### Crafting a Thymeleaf template
```java
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <title>Writing a Reactive Web Controller</title>
</head>
<body>
<h2>Employees</h2>
<ul>
  <li th:each="employee : ${employees}">
    <div th:text=
      "${employee.name + ' (' + employee.role + ')'}">
    </div>
  </li>
</ul>
</body>
</html>
```
- xmlns:th=http://www.thymeleaf.org: This XML namespace allows us to use Thymeleaf’s HTML processing directives.
- th:each: Thymeleaf’s for-each operator, giving us a <li> node for every entry in the employees model attribute. In each node, employee is the stand-in variable.
- th:text: Thymeleaf’s directive to insert text into the node. In this situation, we are concatenating two attributes from the employee record with strings

#### rendered web page
![Screenshot_20230311_124027](https://user-images.githubusercontent.com/54422342/224510628-e353b618-760a-48fb-8794-626a14b3ce3c.png)

![Screenshot_20230311_124032](https://user-images.githubusercontent.com/54422342/224510631-31690fb3-42fe-4870-9dda-c7668b459fb1.png)


### Creating hypermedia reactively

