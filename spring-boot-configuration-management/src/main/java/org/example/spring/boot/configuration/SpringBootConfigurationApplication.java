package org.example.spring.boot.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Activates this application configuration, making it possible to inject into any Spring bean.
 * Apply a Spring bean of the AppConfig type that will be registered automatically in the application context,
 *  bound to the values applied inside application.properties to the entry point for our application
 */
@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class SpringBootConfigurationApplication {
  public static void main(String[] args) {
    SpringApplication.run(SpringBootConfigurationApplication.class, args);
  }
}
