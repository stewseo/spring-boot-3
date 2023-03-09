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

// Spring Security policies will be in effect.
@WebMvcTest(controllers = HomeController.class)
public class SecurityBasedTest {
  //use the auto-configured MockMvc to access HomeController endpoints
  @Autowired MockMvc mvc;
  // HomeController’s collaborator is to be replaced by a Mockito mock.
  @MockBean
  VideoService videoService;

  // Verify that unauthenticated users are denied access
  @Test
  void unauthUserShouldNotAccessHomePage() throws Exception {
    mvc //
      .perform(get("/")) //
      .andExpect(status().isUnauthorized()); // asserts that the result is an HTTP 401 Unauthorized error code
  }

  // good path test case: inserts an authentication token into the MockMVC servlet context with a username of alice and an authority of ROLE_USER.
  @Test
  @WithMockUser(username = "alice", roles = "USER")
  void authUserShouldAccessHomePage() throws Exception {
    mvc //
      .perform(get("/")) //
      .andExpect(status().isOk()); // asserts that the result is an HTTP 200 Ok code
  }

  // assert that @WithMockUser has alice and ROLE_ADMIN stored in the servlet context
  @Test
  @WithMockUser(username = "alice", roles = "ADMIN")
  void adminShouldAccessHomePage() throws Exception {
    mvc //
      .perform(get("/")) //
      .andExpect(status().isOk());
  }

  // Uses MockMVC to perform a POST /new-video action. The param("key", "value") arguments let us provide the fields normally entered through an HTML form
  @Test
  void newVideoFromUnauthUserShouldFail() throws Exception {
    mvc.perform( //
      post("/new-video") //
        .param("name", "new video") //
        .param("description", "new desc") //
        .with(csrf())) //
      .andExpect(status().isUnauthorized()); // Ensures that we get an HTTP 401 Unauthorized response.
  }

  // Verifies that we get something in the 300 series of HTTP response signals
  // to make this test case less brittle if, say, someone switches from soft redirects to hard redirects in the future.
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
