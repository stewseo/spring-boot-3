package org.example.spring.webflux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

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

  // temporary data store
  public static Map<String, Employee> DATABASE = new LinkedHashMap<>();

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
}
