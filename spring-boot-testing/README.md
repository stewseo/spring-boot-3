## Testing Spring Boot 3 Applications

### Primary Purpose:
- Provide examples of how to demonstrate how to test various components of Spring Boot applications, including data repositories, web controllers, and security policies, using various techniques such as mocks, embedded databases, and containerized databases.

### Functional Goals:
- To ensure that the data repositories are being tested properly by using mocks, embedded databases, and containerized databases in a Spring Boot application.
- To test web controllers using MockMVC to verify the response of the application to various requests.
- To test the security policies of a Spring Boot application to ensure that the application is secure and meets the desired security standards.

### Behavioral Goals:
- Demonstrate and explain how to 
- test data repositories using mocks, embedded databases, and containerized databases
- use MockMVC to test web controllers and endpoints
- test security policies in Spring Boot applications

### Creating tests for our domain objects
#### Primary purpose:
#### Functional goals:
#### Behavioral goals:

### Testing web controllers using MockMVC
#### Primary purpose:
#### Functional goals:
#### Behavioral goals:

### Testing data repositories with mocks
#### Primary purpose
#### Functional Goals:
#### Behavioral Goals:

  
### Testing data repositories with embedded databases
#### Primary purpose
- Demonstrate how to write automated tests for data repositories using an embedded database with Spring Boot's testing framework, and to provide examples of different test methods that can be used to validate the functionality of data repository methods.

#### Functional Goals:
- Verify that findAll() method returns all the videos in the repository.
- Verify that findByNameContainsIgnoreCase() method returns a video with a name containing the given string.
- Verify that findByNameContainsOrDescriptionContainsAllIgnoreCase() method returns videos whose names or descriptions contain the given strings.

#### Behavioral Goals:
- The findAllShouldProduceAllVideos() test method should assert that the size of the result list returned by findAll() is equal to 3.
- The findByNameShouldRetrieveOneEntry() test method should assert that the size of the result list returned by findByNameContainsIgnoreCase() is equal to 1 and that the name field of the returned video is equal to "Need HELP with your SPRING BOOT 3 App?".
- The findByNameOrDescriptionShouldFindTwo() test method should assert that the size of the result list returned by findByNameContainsOrDescriptionContainsAllIgnoreCase() is equal to 2 and that the description field of the returned videos contains the strings "As a pro developer, never ever EVER do this to your code" and "Discover ways to not only debug your code".

### Testing data repositories using containerized databases
#### Primary purpose:
- Demonstrate how to test security policies in Spring Boot applications using the @WebMvcTest annotation, MockMvc, and Spring Security's @WithMockUser annotation.

#### Functional Goals:
- Define a process for testing data repositories using containerized databases.
- Inject the application's real Spring Data repository for testing.
- Store a list of VideoEntity objects in the database to verify its behavior.

#### Behavioral Goals:
- These goals describe what the code should accomplish in terms of user behavior.
- Verify that the findAll() method correctly returns all three entities stored in the database.
- Verify that the custom finder supporting the search feature is working as intended.
- Verify that the custom finder with a method name of length 52 is functioning correctly.

### Testing security policies
#### Primary purpose: 
- Demonstrate how to test security policies in Spring Boot applications using the @WebMvcTest annotation, MockMvc, and Spring Security's @WithMockUser annotation.

#### Functional goals:
- Provide tests that verify specific user roles and their access to the home page, ensuring that the required functionality is properly tested.
  - Verify that unauthenticated users are denied access to the home page.
  - Verify that authenticated users with the role "USER" can access the home page.
  - Verify that authenticated users with the role "ADMIN" can access the home page.

#### Behavioral goals:
- Goals that specify the expected behavior of the tests when attempting to access the home page with different user roles, including the expected HTTP response codes.
  - For unauthenticated users, the test should expect an HTTP 401 Unauthorized error code when attempting to access the home page
  - For authenticated users with the role "USER", the test should expect an HTTP 200 Ok code when attempting to access the home page
  - For authenticated users with the role "ADMIN", the test should expect an HTTP 200 Ok code when attempting to access the home page