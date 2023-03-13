package org.example.spring.reactive.web;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static org.example.spring.reactive.web.ApiController.DATABASE;


@Component
public class Startup {
  @Bean
  CommandLineRunner initDatabase() {
    return args -> {
      DATABASE.put("Frodo Baggins", new Employee("Frodo Baggins", "ring bearer"));
      DATABASE.put("Samwise Gamgee", new Employee("Samwise Gamgee", "gardener"));
      DATABASE.put("Bilbo Baggis", new Employee("Bilbo Baggins", "burglar"));
    };
  }
}
