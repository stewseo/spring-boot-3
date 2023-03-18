## Spring Data Cassandra Observability

This project aims to provide observability to a Spring Data Cassandra application with the goal of tracking and monitoring application performance and behavior.

#### EmployeeController: Spring RestController that provides endpoints to handle HTTP requests related to employee data.
- Primary purpose: Expose the methods for retrieving all employees or a specific employee by ID.
- Depends on the EmployeeRepository to perform the necessary data access operations.

#### EmployeeRepository: Spring CassandraRepository that provides methods to perform CRUD operations on the Employee domain model.
- Primary goal: Abstract away the low-level Cassandra database operations and provide a higher-level API for accessing employee data.
- Relies on the CassandraConfiguration class to configure the connection to the Cassandra database.

#### CassandraConfiguration: Spring Configuration class that configures the CqlSession used to connect to the Cassandra database.
- Primary goal: Provide a CqlSessionBuilderCustomizer bean that sets the local datacenter and keyspace for the Cassandra connection.
- Used by the EmployeeRepository.

#### Employee: domain model class that represents an employee entity in the system.
- Primary purose: Encapsulate the employee data and provide getter and setter methods for accessing it. 
- Mapped to a Cassandra table through the @Table annotation.

#### ObservabilityConfiguration: Spring Configuration class that configures the ObservableCqlSessionFactoryBean and ObservableReactiveSessionFactoryBean.
- Primary goal: Provide an instance of ObservationRegistry that can be used to monitor the Cassandra database queries performed by the application.
- Used by the EmployeeController and CommandLineRunner components.

#### SpringDataCassandraObservabilityApplication: main class of the Spring Boot application.
- Primary goal: Bootstrap the Spring application context and start the application.
- Preloads some data into the EmployeeRepository using a CommandLineRunner bean.
- Tracks the execution time of the operation using the ObservationRegistry.