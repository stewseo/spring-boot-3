package org.example.spring.oauth.controller;

import org.example.spring.oauth.service.YouTube;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Annotation indicating that this is a template-based web controller. Each web method returns the name of a template to render.
@Controller
public class HomeController {

  // Inject the YouTube service through constructor injection
  private final YouTube youTube;

  public HomeController(YouTube youTube) {
    this.youTube = youTube;
  }

  @GetMapping("/")
  String index(Model model) {
    // Spring MVC Model object, where we create a channelVideos attribute.
    // It invokes our YouTube service’s channelVideos method with a channel ID, a page size of 10, and uses view counts as the way to sort search results.
    model.addAttribute("channelVideos", //
      youTube.channelVideos("UC7yfnfvEUlXUIfm8rGLwZdA", //
        10, YouTube.Sort.VIEW_COUNT) //
    );

    // The name of the template to render is index.
    return "index";
  }
}
