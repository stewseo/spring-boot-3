package org.example.spring.r2dbc.tests;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
