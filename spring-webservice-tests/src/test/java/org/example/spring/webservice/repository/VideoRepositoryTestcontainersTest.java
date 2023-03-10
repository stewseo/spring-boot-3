package org.example.spring.webservice.repository;

import org.example.spring.webservice.model.VideoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

// Testcontainers junit-jupiter annotation that hooks into the life cycle of a JUnit 5 test case
// Spring Boot Test annotations that
// - indicates that all entity classes and Spring Data JPA repositories should be scanned.
// - tells Spring Boot NOT to replace the DataSource bean like it normally does when there’s an embedded database on the classpath
// Spring test annotation that applies set of properties by hooking the Testcontainers-managed database into Spring Boot’s autoconfigured DataSource
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(initializers = VideoRepositoryTestcontainersTest.DataSourceInitializer.class)
public class VideoRepositoryTestcontainersTest {

  // Injects the application’s real Spring Data repository.
  @Autowired
  VideoRepository repository;

  // Testcontainer’s annotation to flag this as the container to control through the JUnit life cycle.
  @Container //
  static final PostgreSQLContainer<?> database = //
    new PostgreSQLContainer<>("postgres:9.6.12") //
      .withUsername("postgres");

  static class DataSourceInitializer //
    // Class that gives us a handle on the application context
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    // Callback Spring will invoke while the application context is getting created.
    // Adds additional properties provided by the PostgreSQLContainer to the application context by
    //  tapping into a container already started by Testcontainers to harness its JDBC URL, username, and password.
    // Overrides autoconfigured JPA create-drop policy and switches back to create-drop.
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, //
        "spring.datasource.url=" + database.getJdbcUrl(), //
        "spring.datasource.username=" + database.getUsername(), //
        "spring.datasource.password=" + database.getPassword(), //
        "spring.jpa.hibernate.ddl-auto=create-drop"); //
    }
  }

  // Stores a whole list of VideoEntity objects in the database.
  // Each VideoEntity instance has a user, a name, and a description.
  @BeforeEach
  void setUp() {
    repository.saveAll( //
      List.of( //
        new VideoEntity( //
          "alice", //
          "Need HELP with your SPRING BOOT 3 App?", //
          "SPRING BOOT 3 will only speed things up."),
        new VideoEntity("alice", //
          "Don't do THIS to your own CODE!", //
          "As a pro developer, never ever EVER do this to your code."),
        new VideoEntity("bob", //
          "SECRETS to fix BROKEN CODE!", //
          "Discover ways to not only debug your code")));
  }

  // Smoke test that verifies the findAll()method returns all three entities stored in the database. This type of test is to verify we’ve set everything up correctly.
  @Test
  void findAllShouldProduceAllVideos() {
    List<VideoEntity> videos = repository.findAll();
    assertThat(videos).hasSize(3);
  }

  // Proving that our custom finder that supports our search feature is working
  // Focuses on findByNameContainsIgnoreCase, using data stored in a database.
  @Test
  void findByName() {
    List<VideoEntity> videos = repository.findByNameContainsIgnoreCase("SPRING BOOT 3");
    assertThat(videos).hasSize(1);
  }

  // Verifies our custom finder with a method name of length 52
  @Test
  void findByNameOrDescription() {
    List<VideoEntity> videos = repository.findByNameContainsOrDescriptionContainsAllIgnoreCase("CODE", "your code");
    assertThat(videos).hasSize(2);
  }
}
