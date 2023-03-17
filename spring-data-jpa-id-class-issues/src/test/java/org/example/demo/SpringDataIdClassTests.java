package org.example.demo;

import org.example.demo.IdClass.CustomerWithIdClass;
import org.example.demo.IdClass.CustomerWithIdClassRepository;
import org.example.demo.IdClass.VipCustomerWithIdClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Purpose: Investigate and solve an issue that arises when saving a modified subclass object with a composite primary key using IdClass, which results in a primary key conflict with an INSERT statement instead of an UPDATE statement.
 * <p>
 * Goal: Ensure that objects with composite primary keys using IdClass can be saved and modified correctly, and to resolve the identified issue.
 */
@SpringBootTest
@Testcontainers
class SpringDataIdClassTests {

    @Container
    static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:9.6.12")
        .withDatabaseName("demo")
        .withUsername("postgres")
        .withPassword("password");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }


    @Autowired
    CustomerWithIdClassRepository repositoryIdClassVersion;

    @Autowired
    TransactionTemplate txTemplate;

    @Test
    void idClassWithoutTransaction() {
        doStuff();
    }

    @Test
    void idClassWithTransaction() {
        txTemplate.execute(status -> doStuff());
    }

    @Test
    @Transactional
    void idClassWithTransactional() {
        doStuff();
    }


    @Test
    void baseIdClassWithoutTransaction() {
        doStuffWithBase();
    }

    @Test
    void baseIdClassWithTransaction() {
        txTemplate.execute(status -> doStuffWithBase());
    }

    @Test
    @Transactional
    void baseIdClassWithTransactional() {
        doStuffWithBase();
    }

    // Crates an instance of the CustomerWithIdClass, sets its properties, saves it to the repository, modifies it, saves it again, and returns the object.
    private CustomerWithIdClass doStuffWithBase() {
        CustomerWithIdClass customer = new CustomerWithIdClass("a", "b");
        customer.setVersionId(123L);
        customer.setUnitId(456L);

        customer = repositoryIdClassVersion.save(customer);//save object of base class, ok

        customer.setFirstName("a2");
        customer = repositoryIdClassVersion.save(customer);//modify object of base class and save again, ok
        return customer;
    }

    private VipCustomerWithIdClass doStuff() {

        VipCustomerWithIdClass vipCustomer = new VipCustomerWithIdClass("a", "b", "888");
        vipCustomer.setVersionId(987L);
        vipCustomer.setUnitId(654L);

        vipCustomer = repositoryIdClassVersion.save(vipCustomer); //save object of subclass, ok

        vipCustomer.setVipNumber("999");

        // The issue occurs in Spring JPA when saving a modified subclass object with a composite primary key using IdClass.
        // This leads to a primary key conflict with an INSERT statement instead of an UPDATE statement.

        vipCustomer = repositoryIdClassVersion.save(vipCustomer);

        // The issue is specific to cases where the base class has a composite primary key with IdClass.
        // Occurs only when the subclass is saved for the second time.

        return vipCustomer;
    }
}
