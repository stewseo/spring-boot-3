package org.example.spring.data.jpa.queries;

import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;


@Service
public class VideoService {

  private final VideoRepository repository;

  public VideoService(VideoRepository repository) {
    this.repository = repository;
  }

  public List<VideoEntity> getVideos() {
    return repository.findAll();
  }

  public VideoEntity create(NewVideo newVideo) {
    return repository.saveAndFlush(new VideoEntity(newVideo.name(), newVideo.description()));
  }

  /**
   * @param videoSearch containing user entered data containing both name and description details, only the name field, or only the description field
   * @return list of VideoEntity objects
   */
  public List<VideoEntity> search(VideoSearch videoSearch) {
    // Checks that both fields of the VideoSearch record contain actual text and are neither empty nor null using Spring Framework utility class, StringUtils
    if (StringUtils.hasText(videoSearch.name()) //
      && StringUtils.hasText(videoSearch.description())) {
      return repository //
        .findByNameContainsOrDescriptionContainsAllIgnoreCase( // invokes a custom finder that matches the name field and the description field, but with the Contains qualifiers and the AllIgnoreCase modifier
          videoSearch.name(), videoSearch.description());
    }
    // If either field is empty (or null) check if the name field has text. If so, invoke the custom finder matching on name with the Contains and IgnoreCase qualifiers
    if (StringUtils.hasText(videoSearch.name())) {
      return repository.findByNameContainsIgnoreCase(videoSearch.name());
    }
    // Also, check whether the description field has text. If so, use the custom finder that matches on description with Contains and IgnoreCase qualifiers.
    if (StringUtils.hasText(videoSearch.description())) {
      return repository.findByDescriptionContainsIgnoreCase(videoSearch.description());
    }
    return Collections.emptyList();
  }

  /**
   * Creates a probe based on the same domain type as the repository and copies the value attribute into the probe’s Name and Description fields, but only if there is text. If the value attribute is empty, the fields are left null.
   * Assembles an Example<VideoEntity> using and in addition to providing the probe, also provides additional criteria of ignoring the casing and applying a CONTAINING match, which puts wildcards on both sides of every input.
   * Matches any, an Or operation, since the same criteria is put in all fields.
   * @param search the UniversalSearch DTO.
   * @return List<VideoEntity> the result finding all using this example.
   */
  public List<VideoEntity> search(UniversalSearch search) {
    VideoEntity probe = new VideoEntity();
    if (StringUtils.hasText(search.value())) {
      probe.setName(search.value());
      probe.setDescription(search.value());
    }
    Example<VideoEntity> example = Example.of(probe, //
      ExampleMatcher.matchingAny() //
        .withIgnoreCase() //
        .withStringMatcher(StringMatcher.CONTAINING));
    return repository.findAll(example);
  }

  @PostConstruct
  void initDatabase() {
    repository.save(new VideoEntity("Need HELP with your SPRING BOOT 3 App?",
      "SPRING BOOT 3 will only speed things up and make it super SIMPLE to serve templates and raw data."));
    repository.save(new VideoEntity("Don't do THIS to your own CODE!",
      "As a pro developer, never ever EVER do this to your code. Because you'll ultimately be doing it to YOURSELF!"));
    repository.save(new VideoEntity("SECRETS to fix BROKEN CODE!",
      "Discover ways to not only debug your code, but to regain your confidence and get back in the game as a software developer."));
  }
}
