## Method-level security

### Purpose:
- To demonstrate how to secure a Spring Boot web application by locking down access to the owner of the data, enabling method-level security, and displaying user details on the site using Spring Security and Thymeleaf templates. The code provides practical examples and instructions for implementing security measures to protect an application from unauthorized access and malicious attacks.

### Functional Goals:
- The functional goal of:
  - locking down access to the owner of the data is to restrict access to the application's data by implementing role-based authentication and authorization to ensure only authorized users have access to their own data. This goal is functional because it involves implementing a security feature to restrict access to data based on user roles, which is a necessary function of any application that deals with sensitive information.
  - enabling method-level security is to protect individual methods in the application using annotations and custom security rules to ensure that only authorized users can access those methods. This goal is functional because it involves implementing a security feature to restrict access to specific methods in the application based on user roles, which is a necessary function of any application that deals with sensitive information.
  - displaying user details on the site is to display user details such as name and roles on the web page using Thymeleaf templates and Spring Security tags to provide users with information about their account and role-based access. This goal is functional because it involves implementing a feature that displays user details on the web page, which is necessary for users to understand their account and role-based access within the application.

### Behavioral Goals:
- The behavioral goal of 
  - configuring method-level security using Spring Boot's @PreAuthorize and @PostAuthorize annotations is to provide a practical demonstration of securing individual methods in an application by enforcing security policies based on user roles and privileges using these annotations.
  - implementing authentication and authorization using Spring Boot's built-in security features is to demonstrate how to configure security settings, define user roles and privileges, and use these settings to restrict access to certain parts of the application, thereby providing a practical demonstration of the steps involved in implementing authentication and authorization in a Spring Boot application.
  - using Spring Boot Actuator to monitor and manage the security of an application is to demonstrate how to configure Actuator to display security-related metrics and provide insights into the security of the application, thereby showing developers how to practically monitor and manage the security of an application using Spring Boot Actuator.