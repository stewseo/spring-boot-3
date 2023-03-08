package org.example.spring.oauth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

@Configuration
public class SecurityConfig {

  // Defines an OAuth2AuthorizedClientManager bean responsible for handling all interactions between your users and the OAuth service
  // When Spring Security OAuth2 is put on the classpath, Spring Boot auto-configures policies that will combine OAuth2 beans,
  // ClientRegistrationRepository and OAuth2AuthorizedClientRepository, with OAuth2AuthorizationClientManager.
  @Bean
  public OAuth2AuthorizedClientManager clientManager( //
    ClientRegistrationRepository clientRegRepo, //
    OAuth2AuthorizedClientRepository authClientRepo) {

    OAuth2AuthorizedClientProvider clientProvider = //
      OAuth2AuthorizedClientProviderBuilder.builder() //
        .authorizationCode() //
        .refreshToken() //
        .clientCredentials() //
        .password() //
        .build();

    DefaultOAuth2AuthorizedClientManager clientManager = //
      new DefaultOAuth2AuthorizedClientManager( //
        clientRegRepo, //
        authClientRepo);

    clientManager //
      .setAuthorizedClientProvider(clientProvider);

    return clientManager;
  }
}
