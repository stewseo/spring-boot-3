## Sub-Projects and their Goals

This repository is composed of several sub-projects, each utilizing Spring Boot 3 with distinct goals and objectives. Here is a brief overview of the high-level objectives of each sub-project:

### spring-boot-webapplication

#### Purpose: 
- To demonstrate how to create a web application using Spring Boot, including creating a Spring MVC web controller, leveraging templates to create content, creating JSON-based APIs, and integrating Node.js into a Spring Boot web app.

#### High-level functional goal:
- Develop a web application using Spring Boot that can handle HTTP requests, serve dynamic content in web pages, and design RESTful APIs that return JSON-formatted data. Integrate Node.js to enable server-side rendering and serving client-side JavaScript apps.

#### High-Level Behavioral Goal:
- Build a flexible and maintainable web application using Spring Boot that provides APIs and allows for the integration of external technologies such as Node.js, to enhance its functionality and flexibility. This will involve leveraging Spring Boot's features, such as automatic web controller registration, embedded servlet container management, JSON serialization/deserialization, and Mustache templates for separating presentation and business logic layers.

### spring-data-jpa-queries
#### Purpose:
- To provide a practical example of querying and manipulating data with Spring Boot, covering topics such as DTOs, entities, POJOs, Spring Data repositories, custom finders, Query By Example, and custom JPA queries.

#### High-level Behavioral Goal
- Efficiently manage and manipulate data within the application using Spring Data JPA, by designing and implementing a system that handles data effectively.

#### High-level Functional Goal
- Effectively manage and manipulate data within the application context using Spring Data JPA, incorporating specific techniques and structures such as DTOs, POJOs, entities, custom finders, Query By Example, and custom JPA queries and specifications.

### spring-method-level-security
Purpose:
- To demonstrate how to secure a Spring Boot web application by locking down access to the owner of the data, enabling method-level security, and displaying user details on the site using Spring Security and Thymeleaf t`emplates. The code provides practical examples and instructions for implementing security measures to protect an application from unauthorized access and malicious attacks.

High-level functional goal:
- Develop a secure and user-friendly web application with robust security features, such as role-based authentication and authorization, method-level security, and user details display capabilities, to restrict unauthorized access to sensitive information while allowing users to manage their account and access information.

High-Level Behavioral Goal:
- Demonstrate the implementation of various security features in a web application using Spring Boot, emphasizing method-level security, authentication and authorization, and security monitoring and management through Spring Boot Actuator

### spring-security-oauth2

### Primary Purpose:
- To demonstrate how to configure a Spring Boot application for Google OAuth2 authentication, access protected resources on a remote server with obtained OAuth2 credentials, and create a secure web application using OAuth2 authentication and the Spring Security framework, providing a practical example of leveraging Spring Boot for industry-standard authentication mechanisms.

### High-level functional goal:
- Develop a secure web application with Spring Boot that implements OAuth2 authentication to protect resources and allows authenticated users to access them via REST API calls.

### High-Level Behavioral Goal:
- To enhance application security by implementing OAuth2 authentication and authorization mechanisms, allowing secure user access and interaction with protected resources through configuration of the application with an external authentication provider, integration of the Spring Security framework, and utilization of obtained OAuth2 credentials for authentication and authorization.

### spring-boot-testing

#### Purpose:
- Provide examples demonstrating how to effectively test different aspects of a Spring Boot application, including creating domain-based test cases, testing web controllers using MockMVC, testing data repositories with mocks, embedded databases, and containerized databases, and testing security policies with Spring Security Test.

#### Functional Goals:
- Demonstrate different types of tests to evaluate various aspects of an application is to improve the efficiency and effectiveness of the testing process by focusing on testing the specific functionality of the code.
- Create domain-based test cases is to test the specific functionality of the code related to different domains within the application, ensuring that they operate as expected.
- using MockMVC to test web controllers' handling of HTTP requests and responses is to verify the code's specific functionality related to request and response handling and ensure the proper operation of the web controllers.

#### Behavioral Goals:
- Write tests to evaluate various aspects of an application is to focus on the code's behavior in response to different test types, including simple, midgrade, and complex, rather than on any specific functionality of the code.
- Create domain-based test cases is to evaluate how the code behaves in response to specific input and output related to different domains within the application.
- Test web controllers with MockMVC to simulate HTTP requests and responses is to observe how the code behaves in response to these requests and responses.

### spring-boot-configuration-management
#### Purpose:
- Showcase how to configure a Spring Boot application by externalizing the configuration, defining configurations specific to various environments, managing them using profiles, and loading configurations from external sources, in order to make the application more adaptable, maintainable, and scalable across different environments.

#### Functional Goal:
- Enable the definition of environment-specific configurations through property files, the configuration of applications using properties and YAML files, the management of various profiles for different environments, and the loading of external configurations

#### Behavioral Goal:
- Demonstrate the management of an application's behavior in various environments, highlighting the use of profiles to create behavior, and loading configurations from external sources to manage application behavior.

### spring-boot-release-management
#### Purpose:
#### Functional Goal:
#### Behavioral Goal:
- Turn our application from a collection of code into an executable, ready for any production environment, including the cloud:
  - Creating an uber JAR that is runnable anywhere.
  - Baking a Docker container image that can be run locally with no need for Java.
  - Pushing our Docker container to Docker Hub where it can be consumed by our clients.
- Manage/tune/tweak applications once deployed so that we could scale it up as needed:
  - Running multiple instances of our uber JAR pointed at a persistent database, different than the one it came bundled with.

### spring-native-image-build
#### Purpose:
- Provide a practical guide for retrofitting, running, and building native images of a Spring Boot application inside GraalVM, as well as creating a Docker container with GraalVM and Paketo Buildpacks.

#### Functional  Goals:
- Modify the code of the Spring Boot application to be compatible with GraalVM and use it.
- Execute the Spring Boot application natively in GraalVM is to optimize its performance and reduce memory usage. 
- Optimize the Spring Boot application's performance and reducing its attack surface by building and packaging native images in a Docker container using GraalVM and Paketo Buildpacks.

#### Behavioral Goals:
Gain knowledge and experience 
- in retrofitting applications for GraalVM by understanding this technique.
- with the GraalVM environment by running a native Spring Boot application inside it.
- in optimizing application performance and reducing memory usage by learning to build native images using GraalVM.

### web-api-spring-webflux
#### Purpose:
- Demonstrate effective implementation of reactive programming in Spring Boot applications through the creation of reactive web controllers, serving and consuming data reactively with Spring WebFlux, and using reactive templating engines to create hypermedia.
#### High-level Functional Goal:
- "Develop a Spring Boot application that uses reactive programming techniques to enhance its performance and responsiveness."
#### High-level Behavioral Goal:
- "Gain proficiency in applying reactive programming principles and techniques in software development to improve performance and create a reactive, easily navigable API."

### spring-boot-r2dbc-integration

#### Purpose:
- Demonstrate how to work with data reactively using Spring Boot. This involves understanding reactive programming concepts, selecting a reactive data store, creating a reactive data repository with Spring Data, and using R2DBC for reactive SQL database interactions.

#### High-level Functional Goal:
- evaluate and select an appropriate reactive data store based on its performance and features, in order to support the implementation of reactive programming and reactive streams for asynchronous and reactive data fetching, emphasizing the importance of the selection process.

#### High-level Behavioral Goal:
- Implement reactive programming techniques and use reactive streams to fetch data asynchronously and reactively, replacing traditional imperative programming techniques with a focus on the implementation process.

### spring-data-jpa-integration-testing
#### Purpose:
#### Functional Goal:
#### Behavioral Goal:
- Create a database schema that describes how to construct our database, then:
  - Validate Entity Mappings.
  - Verify Constraints.
- Reset the database to a known state after each test.
- Insert sufficient and appropriate data.
- Test against a real database:
  - Test Custom Queries: Inferred, JPQL, and Native SQL Queries.
  - Test db migrations.

### spring-rabbitmq-listener-test
#### Purpose:
#### Functional Goal:
#### Behavioral Goal:
- Ensure seamless integration between components by performing integration tests between:
  - A component responsible for publishing messages to a RabbitMQ exchange.
  - An AMQP listener that consumes messages from a RabbitMQ queue. The queue will be bound to the exchange to facilitate communication between the two components.

### spring-data-cassandra-observability's
#### Purpose:
- Purpose is to demonstrate observability in a Spring Data Cassandra application.
#### Functional Goal:
#### Behavioral Goal: 
The project aims to:
- Demonstrate the usage of Micrometer for measuring various aspects of a Spring Data Cassandra application.
- Demonstrate the creation of observability points in a Spring Data Cassandra application using Micrometer.
- Provide functional examples of all essential components of our Spring Data Cassandra application:
  - An application that utilizes observability points to measure different performance aspects.
  - A configuration class that establishes a connection to Cassandra for the Spring Data Cassandra application.
  - A controller class that manages HTTP requests in the Spring Data Cassandra application.
  - A repository class that handles interactions with the Cassandra database in the Spring Data Cassandra application.
  - An entity class that represents data in the Cassandra database for the Spring Data Cassandra application.