package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

//Annotation for a JPA test that focuses only on JPA components.
//Using this annotation will disable full auto-configuration and instead apply only configuration relevant to JPA tests.
//By default, tests annotated with @DataJpaTest are transactional and roll back at the end of each test.
// They also use an embedded in-memory database (replacing any explicit or usually auto-configured DataSource).
// The @AutoConfigureTestDatabase annotation can be used to override these settings.
// SQL queries are logged by default by setting the spring.jpa.show-sql property to true. This can be disabled using the showSql attribute.
@DataJpaTest
public class VideoRepositoryHsqlTest {

  //  Inject a DataSource, @JdbcTemplate or @EntityManager in to our test class if we need them.
  //  Also, we can inject any of the Spring Data repositories from our application.
  //  All of the above components will be automatically configured to point to an embedded,
  //  in-memory database instead of the “real” database we might have configured in application.properties or application.yml files.
  @Autowired
  VideoRepository repository;

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

  @Test
  void findAllShouldProduceAllVideos() {
    List<VideoEntity> videos = repository.findAll();
    assertThat(videos).hasSize(3);
  }

  @Test
  void findByNameShouldRetrieveOneEntry() {
    List<VideoEntity> videos = repository //
      .findByNameContainsIgnoreCase("SpRinG bOOt 3");
    assertThat(videos).hasSize(1);
    assertThat(videos).extracting(VideoEntity::getName) //
      .containsExactlyInAnyOrder( //
        "Need HELP with your SPRING BOOT 3 App?");
  }

  @Test
  void findByNameOrDescriptionShouldFindTwo() {
    List<VideoEntity> videos = repository //
      .findByNameContainsOrDescriptionContainsAllIgnoreCase( //
        "CoDe", "YOUR CODE");
    assertThat(videos).hasSize(2);
    assertThat(videos) //
      .extracting(VideoEntity::getDescription) //
      .contains("As a pro developer, never ever EVER do this to your code.", //
        "Discover ways to not only debug your code");
  }
}
