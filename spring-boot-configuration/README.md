# Configuring an Application with Spring Boot

### This Spring-Configuration project aims to show how to:
- Externalize parts of our system from content that appears in the web layer to the list of users allowed to authenticate with the system.
- Create type-safe configuration classes, bootstrap them from property files, and then inject them into parts of our application.
- Use profile-based properties and choose between using traditional Java property files or using YAML
- Override property settings from the command line

The details needed to point/connect our application at a given database, message broker, authentication system, and other external services are contained in property settings.

### Custom properties
- Create a bundle of custom properties named AppConfig using a java 17 record
- Populate our AppConfig properties in our application.properties
- Apply our set of application-wide properties to our application's entry point
- Leverage this AppConfig in our HomeController, by declaring a field of the AppConfig type that will be initialized in the constructor call
- Render our index template using our AppConfig's provided values
- Route the string values we put into application.properties so that they render index.mustache
- Freeze a copy of these parameters that Spring can find and use by extending Spring's Converter interface with a custom interface that applies our generic parameters
- Register this converter with the application context so that it gets picked up properly

#### Create a bundle of custom properties named AppConfig using a java 17 record:
```java
/**
 * EnableConfigurationProperties flags this record as a source of property settings.
 * The app.config value is the prefix for its properties.
 * AppConfig is the name of this bundle of type-safe configuration properties.
 */
@ConfigurationProperties("app.config")
public record AppConfig(String header, String intro, List<UserAccount> users) {
}
```

#### Populate our AppConfig properties in our application.properties:
```
# A string value to insert into the top-most part of our template
app.config.header=Spring Boot 3

# A string greeting to put in the template
app.config.intro=Configuring an Application with Spring-Boot

# A list of UserAccount entries with each attribute split out into a separate line. The square bracket notation is used to populate the Java list.
app.config.users[0].username=alice
app.config.users[0].password=password
app.config.users[0].authorities[0]=ROLE_USER
app.config.users[1].username=bob
app.config.users[1].password=password
app.config.users[1].authorities[0]=ROLE_USER
app.config.users[2].username=admin
app.config.users[2].password=password
app.config.users[2].authorities[0]=ROLE_ADMIN
```

#### Apply our set of application-wide properties to our application's entry point:
```java
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
```

#### Leverage this AppConfig in our HomeController by declaring a field of the AppConfig type to be initialized in the constructor call:
```java
/**
 * Declares a field of the AppConfig type to be initialized in the constructor call.
 * Populates the model’s "header" and "intro" attributes with appConfig.intro() and appConfig.header().
 * Routes the string values we put into application.properties so that they render index.mustache
 */
@Controller
public class HomeController {
    private final VideoService videoService;
    private final AppConfig appConfig;

    public HomeController(VideoService videoService, AppConfig appConfig) {
        this.videoService = videoService;
        this.appConfig = appConfig;
    }
}
```

#### Route the string values we put into application.properties so that they render index.mustache
```
<h1>{{header}}</h1> 
<p>{{intro}}</p>
```

#### Freeze a copy of our custom converter's generic parameters that Spring can find and use by extending Spring's Converter interface
```java
interface GrantedAuthorityCnv extends Converter<String, GrantedAuthority> {}
```

#### Register this converter with the application context so that it gets picked up properly
```
@Bean 
@ConfigurationPropertiesBinding 
Converter<String, GrantedAuthority> converter() { 
  return new Converter<String, GrantedAuthority>() { 
    @Override 
    public GrantedAuthority convert(String source) { 
      return new SimpleGrantedAuthority(source); 
    } 
  }; 
}
```

### Profile-based property file
- Create a Spring profile named test by appending –test to the base name of the property file

### Properties using YAML
YAML is a more succinct way to represent the same settings: application-alternate.yaml file in the src/main/resources 
```
app:
  config:
    header: Header YAML-based settings
    intro: Check out this page hosted from YAML
    users:
      -
        username: yaml1
        password: password
        authorities:
          - ROLE_USER
      -
        username: yaml2
        password: password
        authorities:
          - ROLE_USER
      -
        username: yaml3
        password: password
        authorities:
          - ROLE_ADMIN
```
- The nested nature of YAML prevents duplicate entries and makes it clear where each property is located
- The hyphens underneath users denote array entries
- Because the users field of AppConfig is a complex type (List<UserAccount>), each field of each entry is listed on a separate line
- Because authorities itself is a list, it too uses hyphens

### Setting properties with environment variables

```
$ SPRING_PROFILES_ACTIVE=alternative ./mvnw spring-boot:run
$ ./gradlew bootRun --args='--spring.profiles.active=alternative'
```
- SPRING_PROFILES_ACTIVE and spring.profiles.active: The alternative way to reference properties on a Mac/Linux-based system. Dots typically don’t work as well, so capitalized tokens with underscores work.
- alternative: The profile we are running. In the console output you can see The following 1 profile is active: “alternative” as proof that it’s running with that profile activated.
- ./mvnw and ./gradlew: Run things using the Gradle/Maven wrapper.
- spring-boot:run and bootRun: The command that activates the run goal of spring-boot-maven-plugin/standard gradle 'run' task with additional Spring Boot features

### Ordering property overrides
-
