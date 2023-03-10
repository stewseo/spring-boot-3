package org.example.spring.data.jpa.queries;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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

  @PostMapping("/new-video")
  public String newVideo(@ModelAttribute NewVideo newVideo) {
    videoService.create(newVideo);
    return "redirect:/";
  }

  // Deserializes the incoming form in @ModelAttribute VideoSearch
  // Forwards VideoSearch criteria to VideoService.
  // Inserts/stores search results into the Model object under the name videos to be rendered by the index template
  // returns the name of the template to render, index. Spring Boot is responsible for translating this name to src/main/resources/templates/index.mustache
  @PostMapping("/multi-field-search")
  public String multiFieldSearch( //
    @ModelAttribute VideoSearch search, //
    Model model) { //
    List<VideoEntity> searchResults =
      videoService.search(search);
    model.addAttribute("videos", searchResults);
    return "index";
  }

  // Web method to process a UniversalSearch DTO
  // Captures incoming form in the single-value UniversalSearch type
  // Search DTO is passed on to the videoService search() method
  // Stores search results in the Model field to be rendered by the index template
  @PostMapping("/universal-search")
  public String universalSearch(@ModelAttribute UniversalSearch search, Model model) {
    List<VideoEntity> searchResults = videoService.search(search); //
    model.addAttribute("videos", searchResults); //
    return "index";
  }
}
