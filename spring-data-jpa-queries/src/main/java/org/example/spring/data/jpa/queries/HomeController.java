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

  /**
   * Method is marked for processing HTTP POST requests to the /multi-field-search.
   * @param search VideoSearch record type that is annotated with Spring MVC’s signal to deserialize the incoming form.
   * @param model is a mechanism to send information out for rendering.
   *               Forwards VideoSearch criteria to VideoService in the search() method.
   *               Inserts the results into the Model object under the name videos.
   * @return the name of the template to render, index. Spring Boot is responsible for translating this name to src/main/resources/templates/index.mustache.
   */
  @PostMapping("/multi-field-search")
  public String multiFieldSearch( //
    @ModelAttribute VideoSearch search, //
    Model model) { //
    List<VideoEntity> searchResults =
      videoService.search(search);
    model.addAttribute("videos", searchResults);
    return "index";
  }

  /**
   * Maps HTTP post requests /universal-search to this method.
   * Processes the incoming form, captured in the single-value UniversalSearch type.
   * @param search DTO is passed on to the videoService search() method.
   * @param model search results are stored in the Model field to be rendered by the index template.
   * @return index the name of the template to render.
   */

  @PostMapping("/universal-search")
  public String universalSearch(@ModelAttribute UniversalSearch search, Model model) {
    List<VideoEntity> searchResults = videoService.search(search); //
    model.addAttribute("videos", searchResults); //
    return "index";
  }
}
