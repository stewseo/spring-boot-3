package org.example.demo;

import org.example.demo.EmbeddedId.CustomerWithEmbedIdRepository;
import org.example.demo.EmbeddedId.VipCustomerWithEmbedId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * This is a Spring Boot integration test class that uses Docker containers to create isolated test environments.
 * The purpose of this test class is to verify the functionality of a repository that manages objects belonging to a class hierarchy with an embedded primary key.
 * It tests whether saving and modifying objects of a subclass with an embedded primary key works correctly using the Spring Data JPA framework.
 */
@SpringBootTest
@Testcontainers
class SpringDataEmbeddedIdTests {

    @Container
    static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:9.6.12")
        .withDatabaseName("demo")
        .withUsername("postgres")
        .withPassword("password");

    /**
     * This method configures dynamic properties for our test container, including the database URL, username, password, and Hibernate schema generation strategy.
     * postgresProperties() uses the DynamicPropertyRegistry to add the necessary properties for our Spring Boot application to access the database.
     * This involves adding the URL, username, and password retrieved from the database object, as well as setting the Hibernate schema generation strategy to "create-drop" using a lambda expression.
     */
    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    // Injects the repository to be used for the test.
    @Autowired
    CustomerWithEmbedIdRepository repositoryEmbedIdVersion;

    // Injects a template for executing a transaction.
    @Autowired
    TransactionTemplate txTemplate;


    // Executes the doStuff() method without transactions
    @Test
    void embeddedIdWithoutTransaction() {
        doStuff();
    }

    // Executes the doStuff() method with transactions
    @Test
    void embeddedIdWithTransaction() {
        txTemplate.execute(status -> doStuff());
    }

    /**
     * Method that creates a VipCustomerWithEmbedId object with specific properties, such as versionId and unitId, using a repository and saves it to the database.
     * It then modifies the object and saves it again, utilizing an embedded id annotation to ensure successful saving to the database multiple times for our PostgreSQL database.
     * @return the saved and modified VipCustomerWithEmbedId object.
     */
    private VipCustomerWithEmbedId doStuff() {

//        CustomerWithEmbedId customer = new CustomerWithEmbedId("a", "b");
//        customer.setVersionId(123L);
//        customer.setUnitId(456L);
//
//        customer = repositoryEmbedIdVersion.save(customer); //save object of base class, ok
//
//        customer.setFirstName("a2");
//        customer = repositoryEmbedIdVersion.save(customer);//modify object of base class and save again, ok

        VipCustomerWithEmbedId vipCustomer = new VipCustomerWithEmbedId("a", "b", "888");
        vipCustomer.setVersionId(987L);
        vipCustomer.setUnitId(654L);

        vipCustomer = repositoryEmbedIdVersion.save(vipCustomer); //save object of subclass, ok

        vipCustomer.setVipNumber("999");
        vipCustomer = repositoryEmbedIdVersion.save(vipCustomer);//modify object of subclass and save again, ok
        // using embedded id annotation, all 4 times of saving to db ok, for both pg and mysql

        return vipCustomer;
    }
}
