package org.example.spring.webservice.service;

import org.example.spring.webservice.model.NewVideo;
import org.example.spring.webservice.model.Search;
import org.example.spring.webservice.model.VideoEntity;
import org.example.spring.webservice.repository.VideoRepository;
import org.example.spring.webservice.service.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoServiceTest {
  // The class under test
  VideoService service;

  // A collaborator required for VideoService is marked for mocking
  @Mock
  VideoRepository repository;

  // Before every test method, VideoService is created with the mock VideoRepository injected through its constructor
  @BeforeEach
  void setUp() {
    this.service = new VideoService(repository);
  }

  // Uses Mockito to define how the mock VideoRepository responds when its findAll() method is invoked.
  // Verifies the outcome returned by the getVideos() method of VideoService.
  @Test
  void getVideosShouldReturnAll() {
    // given
    VideoEntity video1 = new VideoEntity("alice", "Spring Boot 3 Intro", "Learn the basics!");
    VideoEntity video2 = new VideoEntity("alice", "Spring Boot 3 Deep Dive", "Go deep!");
    when(repository.findAll()).thenReturn(List.of(video1, video2));

    // when
    List<VideoEntity> videos = service.getVideos();

    // then
    assertThat(videos).containsExactly(video1, video2);
  }


  @Test
  void searchShouldReturnASubset() {
    // given
    VideoEntity video1 = new VideoEntity("alice", "Spring Boot 3 Intro", "Learn the basics!");
    when(repository.findAll(any(Example.class))).thenReturn(List.of(video1));

    // when
    List<VideoEntity> videos = service.search(new Search("Spring Boot 3"));

    // then
    assertThat(videos).containsExactly(video1);
  }

  // Uses Mockito’s BDDMockito.given operator, a synonym for Mockito’s when() operator
  // Asserts against the results returned by VideoService create()
  @Test
  void creatingANewVideoShouldReturnTheSameData() {
    // given
    given(repository.saveAndFlush(any(VideoEntity.class))) //
      .willReturn(new VideoEntity("alice", "name", "des"));

    // when
    VideoEntity newVideo = service.create(new NewVideo("name", "des"), "alice");

    // then
    assertThat(newVideo.getName()).isEqualTo("name");
    assertThat(newVideo.getDescription()).isEqualTo("des");
    assertThat(newVideo.getUsername()).isEqualTo("alice");
  }

  // Verifies the methods invoked inside the service after invoking the delete() method.
  @Test
  void deletingAVideoShouldWork() {
    // given
    VideoEntity entity = new VideoEntity("alice", "name", "desc");
    entity.setId(1L);
    when(repository.findById(1L)).thenReturn(Optional.of(entity));

    // when
    service.delete(1L);

    // then
    verify(repository).findById(1L);
    verify(repository).delete(entity);
  }
}
