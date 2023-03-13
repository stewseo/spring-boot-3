package org.example.spring.boot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * EnableConfigurationProperties flags this record as a source of property settings.
 * The app.config value is the prefix for its properties.
 * AppConfig is the name of this bundle of type-safe configuration properties. It doesn’t matter what name we give it.
 */
@ConfigurationProperties("app.config")
public record AppConfig(String header, String intro, List<UserAccount> users) {
}
