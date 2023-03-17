package org.example.method.security.util;

import org.example.method.security.model.UserAccount;
import org.example.method.security.repository.UserManagementRepository;
import org.example.method.security.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  // Initializes 3 default users for the UserManagementRepository.
  @Bean
  CommandLineRunner initUsers(UserManagementRepository repository) {
    return args -> {
      repository.save(new UserAccount("alice", "password", "ROLE_USER"));
      repository.save(new UserAccount("bob", "password", "ROLE_USER"));
      repository.save(new UserAccount("admin", "password", "ROLE_ADMIN"));
    };
  }

  // Returns a custom UserDetailsService that retrieves users from the UserRepository.
  @Bean
  UserDetailsService userService(UserRepository repo) {
    return username -> repo.findByUsername(username).asUser();
  }

  // Defines a SecurityFilterChain that configures URL-based authorization for various endpoints, as well as form-based and HTTP basic authentication.
  @Bean
  SecurityFilterChain configureSecurity(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests() //
      .requestMatchers("/login").permitAll() //
      .requestMatchers("/", "/search").authenticated() //
      .requestMatchers(HttpMethod.GET, "/api/**").authenticated() //
      .requestMatchers("/admin").hasRole("ADMIN") //
      .requestMatchers(HttpMethod.POST, "/delete/**", "/new-video").authenticated() //
      .anyRequest().denyAll() //
      .and() //
      .formLogin() //
      .and() //
      .httpBasic();
    return http.build();
  }
}
