package org.example.spring.boot.webapplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Annotated as a Spring MVC controller that returns JSON
 * Automatically copies a VideoService using constructor injection
 * Responds to HTTP GET calls from /api/videos by fetching that list of Video records and return them,
 *  causing them to get rendered into a JSON array by Jackson
 */
@RestController
public class ApiController {

  private final VideoService videoService;

  public ApiController(VideoService videoService) {
    this.videoService = videoService;
  }

  @GetMapping("/api/videos")
  public List<Video> all() {
    return videoService.getVideos();
  }

  /**
   * Maps HTTP POST calls to /api/videos onto this method.<br>
   * Signals that the incoming HTTP request body should be deserialized via Jackson into
   * @param newVideo <br>
   * @return the record after it’s been added to the system.<br>
   * The actual handling of this incoming Video record is delegated to our VideoService
   */
  @PostMapping("/api/videos")
  public Video newVideo(@RequestBody Video newVideo) {
    return videoService.create(newVideo);
  }
}
