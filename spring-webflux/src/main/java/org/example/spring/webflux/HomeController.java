package org.example.spring.webflux;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.example.spring.webflux.ApiController.DATABASE;

// This class contains web methods that render templates.
@Controller
public class HomeController {

  // Maps GET / web calls onto this method.
  // Wraps a Java list of Employee objects(canned data stored in java map) into a Flux using fromIterable
  //  and extracts them back out using collectList.
  // Transforms List<Employee> into a Rendering, while keeping everything inside this Mono
  //  by unpacking the original Mono<List<Employee>> and using the results to create a new Mono<Rendering>
  // returns Mono<Rendering>: Reactor’s single-valued reactive type. Rendering is Spring WebFlux’s value type that allows us to pass both the name of the view to render along with model attributes.
  @GetMapping("/")
  Mono<Rendering> index() {
    return Flux.fromIterable(DATABASE.values()) //
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
      .map(employee -> {
        DATABASE.put(employee.name(), employee);
        return "redirect:/";
      });
  }
}
