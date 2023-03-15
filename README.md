## Project Goals

### spring-boot-application-release's goal is to demonstrate how to:
- Turn our application from a collection of code into an executable, ready for any production environment, including the cloud:
    - Creating an uber JAR that is runnable anywhere.
    - Baking a Docker container image that can be run locally with no need for Java.
    - Pushing our Docker container to Docker Hub where it can be consumed by our clients.
- Manage/tune/tweak applications once deployed so that we could scale it up as needed:
    - Running multiple instances of our uber JAR pointed at a persistent database, different than the one it came bundled with.

### spring-boot-configuration aims to show how to:
- Externalize parts of our system from content that appears in the web layer to the list of users allowed to authenticate with the system.
- Create type-safe configuration classes, bootstrap them from property files, and then inject them into parts of our application.
- Use profile-based properties and choose between using traditional Java property files or using YAML
- Override property settings from the command line

### spring-boot-nativeimages' goal is to demonstrate how to:
- Build native images using GraalVM. This is a faster, more efficient version of our application than we could ever build using the standard JVM.
    - Retrofit our application for GraalVM
    - Run our native Spring Boot application inside GraalVM
- Bake native images into a Docker container using Paketo Buildpacks.
    - Bake a Docker container with GraalVM

### spring-boot-webapplication has the following goals:
- Inject demo data using a service
- Create a web controller that uses Mustache to render dynamic content based on the demo data
- Create a JSON-based API allowing third-party apps to interact with our web app, whether it’s to retrieve data or send in updates
- Leverage Node.js to introduce some JavaScript to our web app using Gradle plugin:

### spring-boot-webflux aims to:
- Create a reactive application using Project Reactor
- Roll out reactive web methods to both serve and consume JSON
- Leverage Thymeleaf to reactively generate HTML and consume an HTML form
- Reactively generate a hypermedia-aware API using Spring HATEOAS

### spring-data-jpa-queries aims to demonstrate and explain how to:
- Query for data using Spring Data JPA
    - Writing custom finders, using Query By Example, and directly accessing the data store using custom JPQL and SQL
- Hook variations of queries into search boxes.
- Use Java 17 records to quickly assemble DTOs to ferry form requests into web methods and onto VideoService.
- Assess when it makes sense to use various querying tactics.

### spring-security-methodlevel aims to:
- Implement and test method-level fine-grained controls using Spring Security.
- Explain how to control authentication and authorization at the object level, not just the URL level.

### spring-security-oauth2 aims to:
- Outsource user management to Google using Spring Security’s OAuth2 integration.
- Take advantage of this by grabbing hold of some YouTube data and serve up video links.

### spring-webservice-tests aims to:
- Test different aspects of our application through simple, midgrade, and complex tests.
- Explain the various tradeoffs of each tactic.
    - Using real database engines if we’re willing to spend the extra runtime.
- Ensure our security policies are properly shaken out with both unauthorized and fully authorized users.

### spring-boot-webflux aims to:
- Create a reactive application using Project Reactor.
- Roll out reactive web methods to both serve and consume JSON.
- Leverage Thymeleaf to reactively generate HTML and consume an HTML form.
- Reactively generate a hypermedia-aware API using Spring HATEOAS.

### spring-boot-r2dbc aims to:
- Explain what it means to fetch data reactively.
- Build an introductory reactive Spring Boot application.
  - Leverage Spring Data to help manage our content.
  - Create a reactive data repository, Spring Data Commons’ base interface for any reactive repository, ReactiveCrudRepository.
  - Load test data by injecting a copy of our Spring Data R2DBC bean(R2dbcEntityTemplate) to the application's context.
  - Return data reactively to our API controller that will be used to generate our web template served up at the root of our domain.

### db-migration-tests has the following goals:
- Create a database schema that describes how to construct our database, then:
  - Validate Entity Mappings.
  - Verify Constraints.
- Reset the database to a known state after each test.
- Insert sufficient and appropriate data.
- Test against a real database:
  - Test Custom Queries: Inferred, JPQL, and Native SQL Queries.
  - Test db migrations.

### spring-amqplistener-tests's goal is to demonstrate how to test the integration of:
- A component publishing messages to a RabbitMQ exchange.
- An amqp listener consuming messages from a RabbitMQ queue that is bound to an exchange.
