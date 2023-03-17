package org.example.spring.r2dbc.tests;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.test.StepVerifier;

/**
 * This class is flagged as a collection of bean definitions, needed to autoconfigure our application<br>
 * - Turns initDatabase() into a Spring bean, added to the application context<br>
 * - Defines our Spring Data R2DBC schema once the application is started inside Spring Boot’s functional interface for an object<br>
 * - Injects a copy of this Spring Data R2DBC bean, so we can load test data<br>
 */
@Configuration
public class Startup {

  /**
   * @param template R2dbcEntityTemplate<p>
   *                 - Carries out the SQL statement that creates an EMPLOYEE table with a self-incrementing id field in H2's dialect.<p>
   *                 - Defines 3 typesafe insert operations by providing a type parameter.<p>
   *                 - Forces execution of our reactive flow using Reactor Test.<p>
   *                 - Verifies that we receive an expected count, indicating that the operation was successful.<p>
   *                 - Ensures that we receive Reactive Streams onComplete signals.
   * @return an object that is automatically executed once the application is started.
   */
  @Bean
  CommandLineRunner initDatabase(R2dbcEntityTemplate template) {
    return args -> {
      template.getDatabaseClient() //
        .sql("CREATE TABLE EMPLOYEE (id IDENTITY NOT NULL PRIMARY KEY , name VARCHAR(255), role VARCHAR(255))") //
        .fetch() //
        .rowsUpdated() //
        .as(StepVerifier::create) //
        .expectNextCount(1) //
        .verifyComplete();

      /**
       * Defines an insert operation with provided Employee data. By providing a type parameter, the subsequent operations are typesafe.
       * Forces the execution of our reactive flow using Reactor Test
       * Expects a single response, for a single insert.
       * Verifies whether we received the onComplete signal.
       */
      template.insert(Employee.class) //
        .using(new Employee("Frodo Baggins", "ring bearer")) //
        .as(StepVerifier::create) //
        .expectNextCount(1) //
        .verifyComplete();

      template.insert(Employee.class) //
        .using(new Employee("Samwise Gamgee", "gardener")) //
        .as(StepVerifier::create) //
        .expectNextCount(1) //
        .verifyComplete();

      template.insert(Employee.class) //
        .using(new Employee("Bilbo Baggins", "burglar")) //
        .as(StepVerifier::create) //
        .expectNextCount(1) //
        .verifyComplete();
    };
  }
}
