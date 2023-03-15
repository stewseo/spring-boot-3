package org.example.spring.boot.webapplication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Annotated to communicate that this class is a web controller. When the application starts, Spring Boot will automatically detect this class through component scanning and will create an instance.
 * Maps HTTP GET /calls to the "index" method that is the name of the template we want to render.
 * Private final instance of VideoService that is populated using constructor injection
 */
@Controller
public class HomeController {
  private final VideoService videoService;

  public HomeController(VideoService videoService) {
    this.videoService = videoService;
  }

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("videos", videoService.getVideos());
    return "index";
  }

  @GetMapping("/react")
  public String react() {
    return "react";
  }

  // Captures POST /new-video calls and route them to this method.
  // Parses an incoming HTML form and unpacks it into a Video object.
  // Stores the new video object.
  // Sends the browser an HTTP 302 Found to URL /. A 302 redirect is the standard for a soft redirect. (301 is a permanent redirect, instructing the browser to not try the original path again.)
  @PostMapping("/new-video")
  public String newVideo(@ModelAttribute Video newVideo) {
    videoService.create(newVideo);
    return "redirect:/";
  }
}
