package org.example.spring.oauth.configuration;

import org.example.spring.oauth.service.YouTube;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

// Spring configuration class containing bean definitions for
//  hooking OAuth 2 support into an HTTP remote service invoker,
//  creating a proxy that implements a Http service interface to perform HTTP requests and retrieve responses through an HTTP client.
@Configuration
public class YouTubeConfig {

  static String YOUTUBE_V3_API = //
    "https://www.googleapis.com/youtube/v3";

  // Define a WebClient bean responsible for all remote means of accessing the YouTube API using an interface.
  @Bean
  WebClient webClient( //
    OAuth2AuthorizedClientManager clientManager) {

    // Registers an exchange filter function using the OAuth2AuthorizedClientManager defined in SecurityConfig as a way to give it OAuth2 power
    ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = //
      new ServletOAuth2AuthorizedClientExchangeFilterFunction( //
        clientManager);

    // This WebClient instance will have YouTube as its base URL and will pipe all requests through this OAuth2-handling function.
    oauth2.setDefaultClientRegistrationId("google");

    return WebClient.builder() //
      .baseUrl(YOUTUBE_V3_API) //
      .apply(oauth2.oauth2Configuration()) //
      .build();
  }

  // Builds HttpServiceProxyFactory instance that will generate a client proxy for our YouTube Http service interface
  @Bean
  HttpServiceProxyFactory proxyFactory(WebClient oauth2WebClient) {
    return HttpServiceProxyFactory.builder() //
      .clientAdapter(WebClientAdapter.forClient(oauth2WebClient)) //
      .build();
  }

  // Create the client proxy that implements the YouTube service interface to perform remote invocations through the WebClient.
  @Bean
  YouTube client(HttpServiceProxyFactory factory) {
    return factory.createClient(YouTube.class);
  }
}
