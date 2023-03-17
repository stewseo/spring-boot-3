package org.example.method.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.stream.Stream;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasRole;

public class ComplexSecurityConfig {
    // This policy uses authorizeHttpRequests, signaling web-based checks
  @Bean
  SecurityFilterChain configureSecurity(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests() //
      .requestMatchers("/resources/**", "/about", "/login").permitAll() // Path-based check to see if the URL starts with /resources, /about, or /login. If so, access is immediately granted, regardless of authentication status.
      .requestMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN") // Looks for any GET calls to the /admin pages. HTTP verb can be combined with a path to control access.
      .requestMatchers("/db/**").access((authentication, object) -> { // User must be both a DBA and ADMIN to access this path.
        boolean anyMissing = Stream.of("ADMIN", "DBA")//
          .map(role -> hasRole(role).check(authentication, object).isGranted()) //
          .filter(granted -> !granted) //
          .findAny() //
          .orElse(false); //
        return new AuthorizationDecision(!anyMissing);
      }) //
      .anyRequest().denyAll() // Denies access If the user can’t meet any of the earlier rules, they shouldn’t be granted access to anything.
      .and() //
      .formLogin() //
      .and() //
      .httpBasic();
    return http.build();
  }

}
