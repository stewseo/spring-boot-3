package org.example;

import org.example.controller.HomeController;
import org.example.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HomeController.class)
public class SecurityBasedTest {
  //use the auto-configured MockMvc to access HomeController endpoints
  @Autowired MockMvc mvc;

  @MockBean
  VideoService videoService;

  @Test
  void unauthUserShouldNotAccessHomePage() throws Exception {
    mvc //
      .perform(get("/")) //
      .andExpect(status().isUnauthorized());
  }

  // When used with WithSecurityContextTestExecutionListener this annotation can be added to a test method to emulate running with a mocked user.
  // In order to work with MockMvc The SecurityContext that is used will have the following properties:
  //The SecurityContext created with be that of SecurityContextHolder.createEmptyContext()
  //It will be populated with an UsernamePasswordAuthenticationToken that uses the username of either value() or username(), GrantedAuthority that are specified by roles(), and a password specified by password().
  @Test
  @WithMockUser(username = "alice", roles = "USER")
  void authUserShouldAccessHomePage() throws Exception {
    mvc //
      .perform(get("/")) //
      .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "alice", roles = "ADMIN")
  void adminShouldAccessHomePage() throws Exception {
    mvc //
      .perform(get("/")) //
      .andExpect(status().isOk());
  }

  @Test
  void newVideoFromUnauthUserShouldFail() throws Exception {
    mvc.perform( //
      post("/new-video") //
        .param("name", "new video") //
        .param("description", "new desc") //
        .with(csrf())) //
      .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "alice", roles = "USER")
  void newVideoFromUserShouldWork() throws Exception {
    mvc.perform( //
      post("/new-video") //
        .param("name", "new video") //
        .param("description", "new desc") //
        .with(csrf())) //
      .andExpect(status().is3xxRedirection()) //
      .andExpect(redirectedUrl("/"));
  }
}