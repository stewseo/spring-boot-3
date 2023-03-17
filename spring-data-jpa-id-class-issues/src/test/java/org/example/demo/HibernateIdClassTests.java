package org.example.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
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
 * This test class tests the behavior of Hibernate when using an IdClass for a composite primary key with inheritance.
 * The main purpose of the tests is to identify issues with merging objects of a subclass with a composite primary key
 *  and to ensure that updates are properly executed instead of insert statements.
 */
@SpringBootTest
@Testcontainers
class HibernateIdClassTests {

    // Create a PostgreSQL database container for testing purposes that ensures isolation and reproducibility
    @Container
    static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:9.6.12")
        .withDatabaseName("demo")
        .withUsername("postgres")
        .withPassword("postgres");

    // Adds dynamic properties to the Spring environment: the database URL, username, password, and hibernate.ddl-auto.
    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }


    @Autowired
    EntityManager entityManager;

    @Autowired
    TransactionTemplate txTemplate;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    // Calls the doStuff() method without a transaction.
    @Test
    void idClassWithoutTransaction() {
        doStuff(entityManager);
    }

    // Creates an entity manager and transaction, begins the transaction, calls the doStuff() method, and commits the transaction.
    @Test
    void idClassWithHandRolledTransaction() {

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        doStuff(em);

        tx.commit();
    }

    // Uses Spring's TransactionTemplate to execute the doStuff() method with a transaction.
    @Test
    void idClassWithTransaction() {
        txTemplate.execute(status -> doStuff(entityManager));
    }

    // Uses Spring's @Transactional annotation to execute the doStuff() method with a transaction.
    @Test
    @Transactional
    void idClassWithTransactional() {
        doStuff(entityManager);
    }

    // Merges objects of the base class and a subclass with a composite primary key, and tests whether updates are properly executed instead of insert statements when modifying and merging the objects.
    private VipCustomerWithIdClass doStuff(EntityManager em) {
//        CustomerWithIdClass customer = new CustomerWithIdClass("a", "b");
//        customer.setVersionId(123L);
//        customer.setUnitId(456L);
//
//        customer = entityManager.merge(customer);//merge object of base class, ok
//
//        customer.setFirstName("a2");
//        customer = entityManager.merge(customer);//modify object of base class and merge again, ok

        VipCustomerWithIdClass vipCustomer = new VipCustomerWithIdClass("a", "b", "888");
        vipCustomer.setVersionId(987L);
        vipCustomer.setUnitId(654L);

        vipCustomer = em.merge(vipCustomer);//merge object of subclass, ok

        vipCustomer.setVipNumber("999");

        // When modifying an object of the subclass and attempting to merge it again, a primary key conflict occurs resulting in an INSERT statement instead of an UPDATE.
        // This issue only occurs when the base class uses IdClass for composite primary key and an instance of the subclass is saved for the second time after modification.
        vipCustomer = em.merge(vipCustomer);

        return vipCustomer;
    }
}
