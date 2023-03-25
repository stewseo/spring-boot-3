## Querying for Data with Spring Boot

### Purpose:
- To provide a practical example of querying and manipulating data with Spring Boot, covering topics such as DTOs, entities, POJOs, Spring Data repositories, custom finders, Query By Example, and custom JPA queries.

### Objectives:
- Using DTOs, POJOs, and entities to transfer data, model objects, and map to database tables.
- Creating a Spring Data repository 
- Using custom finders
- Using Query By Example
- Using custom JPA

### The functional goal of:
- Using DTOs, entities, and POJOs: To transfer data, model objects, and map them to database tables by utilizing DTOs, POJOs, and entities, which are specific techniques and structures for data management within the application.
- Creating a Spring Data repository: To create a specific component within the application responsible for handling data retrieval and manipulation tasks using Spring Data by creating a component responsible for performing CRUD operations on entities and defining customized methods for specific queries.
- Using custom finders: To develop a component within the application that can perform specific data retrieval tasks based on complex queries with multiple parameters by creating finder methods using SpEL that accept multiple parameters to execute intricate queries.
- Using Query By Example to find tricky answers: To search for entities based on specific criteria using a specific technique, Query By Example (QBE), with a probe object and matcher, rather than describing a general behavior.
- Using custom JPA: To perform data retrieval tasks within the application by creating specific queries and query specifications using JPA and Spring Data JPA, which are techniques for generating unique JPA queries utilizing JPQL and native SQL and generating reusable query specifications with Spring Data JPA specifications.

### The behavioral goal of:
- utilizing DTOs, entities, and POJOs is to transfer data, model objects, and map to database tables using different objects and their relationships for managing data within the application.
- creating a Spring Data repository is to perform CRUD operations on entities and define customized methods for specific queries by developing a component responsible for managing data retrieval and manipulation within the application.
- using custom finders is to create SpEL-based finder methods that accept multiple parameters for executing intricate queries, allowing for the retrieval of specific data within the application.
- using Query By Example (QBE) to find tricky answers is to search for entities based on specific criteria by utilizing QBE with a probe object and matcher to perform complex data searches within the application.
- using custom JPA is to craft unique JPA queries using JPQL and native SQL, and to generate reusable query specifications with Spring Data JPA specifications, enabling specific queries and query specifications for data retrieval tasks within the application.
