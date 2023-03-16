## Verifying an application with Spring Boot 3

### Test-scoped dependencies that spring-boot-starter-test contains:
- Spring Boot Test: Spring Boot-oriented test utilities
- JSONPath: The query language for JSON documents
- AssertJ: Fluent API for asserting results
- Hamcrest: Library of matchers
- JUnit 5: Cornerstone library for writing test cases
- Mockito: Mocking framework for building test cases
- JSONassert: Library of assertions aimed at JSON documents
- Spring Test: Spring Framework’s test utilities
- XMLUnit: Toolkit for verifying XML documents


### Creating domain-based test cases
- Writing unit-level tests against a domain class

### Verifying our web controller
- Testing our web controller with MockMVC
- Leveraging Spring Boot Test’s slice-based @WebMvcTest annotation
- Proving fundamental controller behavior quickly

### Verifying our Service Layer
- Testing data repositories with mocks
  - Unit-based tests for the service layer that the web controller invokes.
  - Isolating the VideoService bean from any outside influences by mocking VideoService's one collaborator, VideoRepository.
  - Capturing intent through stubbing and verifying against test data.
  - Capturing behavior with mocking and verifying that the right methods were called.

### Verifying our Repository Layer
- Testing data repositories
  - With embedded databases
    - Test cases against an in-memory database concerning our application
      - The database that runs in the same memory space as our application: HyperSQL Database (HSQLDB)
    - Verify that we have written the correct queries, using custom finders, query by example, or any other strategies we wish to leverage.
  - Using containerized databases
    - Test cases that ensure that our data repository properly interacts with the database.
    - Test cases that verify our design of case-insensitive queries against various fields supports our service layer
### Verifying security policies with Spring Security Test
- Ensure our security policies are properly shaken out with both unauthorized and fully authorized users