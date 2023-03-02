package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Annotation specifying that Spring Context includes components required for testing Spring MVC parts of the application.
// auto-configures: @Controller, @ControllerAdvice, @JsonComponent, Converter, Filter, WebMvcConfigurer.
// @WebMvcTest is annotated with JUnit5 hook @ExtendsWith(SpringExtension.class)
@WebMvcTest(controllers = HomeController.class) // isolate test scope to HomeController
public class HomeControllerTest {

  // use the auto-configured MockMvc to access HomeController endpoints
  @Autowired MockMvc mvc;

  // mock dependent bean of controller endpoint.
  // add a mock bean to a Spring ApplicationContext.
  @MockBean VideoService videoService;

  // verify that index page has html form
  @Test
  @WithMockUser
  void indexPageHasSeveralHtmlForms() throws Exception {
    String html = mvc.perform( //
      get("/")) //
      .andExpect(status().isOk()) //
      .andExpect( //
        content().string( //
          containsString("Username: user"))) //
      .andExpect( //
        content().string( //
          containsString("Authorities: [ROLE_USER]"))) //
      .andReturn() //
      .getResponse().getContentAsString();

    assertThat(html).contains( //
      "<form action=\"/logout\"", //
      "<form action=\"/search\"", //
      "<form action=\"/new-video\"");
  }

  @Test
  @WithMockUser
  void postNewVideoShouldWork() throws Exception {
    mvc.perform( //
      post("/new-video") //
        .param("name", "new video") //
        .param("description", "new desc") //
        .with(csrf())) //
      .andExpect(redirectedUrl("/"));

    verify(videoService).create( //
      new NewVideo( //
        "new video", //
        "new desc"), //
      "user");
  }
}
