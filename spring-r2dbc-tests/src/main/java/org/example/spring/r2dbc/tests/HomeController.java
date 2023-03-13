package org.example.spring.r2dbc.tests;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

/**
 * This controller class is focused on rendering templates.<br>
 * - EmployeeRepository is injected into this controller using constructor injection.<br>
 * - Generates the web template served up at the root of our domain with the data returned by EmployeeRepository's findAll() method.<br>
 * - Handles mapped HTTP POST /api/new-employee calls in a method that processes the form-backed Employee bean by
 *    converting the outcome of the save() operation to the redirect request.<br>
 */
@Controller
public class HomeController {

  private final EmployeeRepository repository;

  public HomeController(EmployeeRepository repository) {
    this.repository = repository;
  }

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

  @PostMapping("/new-employee")
  Mono<String> newEmployee(@ModelAttribute Mono<Employee> newEmployee) {
    return newEmployee //
      .flatMap(e -> {
        Employee employeeToSave = new Employee(e.getName(), e.getRole());
        return repository.save(employeeToSave);
      }) //
      .map(employee -> "redirect:/");
  }
}
