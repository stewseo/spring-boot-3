package org.example.spring.r2dbc.tests;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

/**
 * This controller class is focused on rendering templates.<p>
 * EmployeeRepository is injected into this controller using constructor injection.
 */
@Controller
public class HomeController {

  private final EmployeeRepository repository;

  public HomeController(EmployeeRepository repository) {
    this.repository = repository;
  }

  /**
   * Generates the web template served up at the root of our domain<p>
   * Uses the Flux returned from EmployeeRepository's findAll() method to gather a stream of items into Mono<List<Employee>><p>
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
}
