package org.example;

import org.example.model.VideoEntity;
import org.example.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

// Spring Boot’s test annotation and indicates that we want it to perform all of its automated scanning of entity class definitions and Spring Data JPA repositories
@DataJpaTest
public class VideoRepositoryHsqlTest {

  //  Automatically injects an instance of our VideoRepository object to test against.
  @Autowired
  VideoRepository repository;

  @BeforeEach
  void setUp() {
    repository.saveAll( // saves a batch of test data
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

  // check the size of the results.
  @Test
  void findAllShouldProduceAllVideos() {
    List<VideoEntity> videos = repository.findAll();
    assertThat(videos).hasSize(3);
  }

  @Test
  void findByNameShouldRetrieveOneEntry() {
    List<VideoEntity> videos = repository //
      .findByNameContainsIgnoreCase("SpRinG bOOt 3");
    assertThat(videos).hasSize(1); //  verify the size of the results as 1.
    assertThat(videos).extracting(VideoEntity::getName) // extract the name field of each entry
      .containsExactlyInAnyOrder( //
        "Need HELP with your SPRING BOOT 3 App?");
  }

  @Test
  void findByNameOrDescriptionShouldFindTwo() {
    List<VideoEntity> videos = repository //
      .findByNameContainsOrDescriptionContainsAllIgnoreCase( //
        "CoDe", "YOUR CODE");
    assertThat(videos).hasSize(2); // assert the size of the results to verify we are on the right path
    assertThat(videos) //
      .extracting(VideoEntity::getDescription) // use the extracting() operator to fetch the description field
      .contains("As a pro developer, never ever EVER do this to your code.", // check that the extracting operator contains 2 descriptions without worrying about the order.Without an ORDER BY clause, databases are not obligated to return the results in the same order as they were stored
        "Discover ways to not only debug your code");
  }
}
