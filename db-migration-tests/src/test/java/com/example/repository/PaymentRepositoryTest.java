package com.example.repository;

import com.example.entity.Order;
import com.example.entity.Payment;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Tests that verify our database schema and constraints to ensure the consistency of our data
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentRepositoryTest {

    // Testing the repository interface that we use to build queries and send to the database.
    @Autowired
    private PaymentRepository paymentRepository;

    // Handler of Entities that the framework maps to the database schema.
    // Provides a subset of EntityManager methods for common testing tasks such as persist/flush/find.
    @Autowired
    private TestEntityManager entityManager;

    // Verify manual insertion of Entities
    @Test
    void existingPaymentCanBeFound() {
        Order order = new Order(LocalDateTime.now(), BigDecimal.valueOf(100.0), true);
        Payment payment = new Payment(order, "4532756279624064");

        // Make an instance managed and persistent. Delegates to EntityManager.persist(Object) then returns the original source entity.
        Long orderId = entityManager.persist(order).getId();

        // Flush to synchronize persistence context changes to database
        entityManager.persistAndFlush(payment);


        // Clear the persistence context so that entities are not fetched from the first-level cache.
        // Causes all managed entities to become detached.
        entityManager.clear();

        Optional<Payment> savedPayment = paymentRepository.findByOrderId(orderId);

        assertThat(savedPayment).isPresent();
        assertThat(savedPayment.get().getOrder().getPaid()).isTrue();
    }

    // Verify Constraints
    // If we try to save more than one payment for an order, it should throw a PersistenceException:
    @Test
    void paymentsAreUniquePerOrder() {
        Order order = new Order(LocalDateTime.now(), BigDecimal.valueOf(100.0), true);
        Payment first = new Payment(order, "4532756279624064");
        Payment second = new Payment(order, "4716327217780406");

        entityManager.persist(order);
        entityManager.persist(first);

        // Thrown by the persistence provider when a problem occurs
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(second));
    }

    // Test Sql Scripts to Insert data
    // Custom query does not include WHERE clause
    @Test
    @Sql("/multiple-payments.sql")
    void findPaymentsAfterDate() {
        List<Payment> payments = paymentRepository.findAllAfter(LocalDateTime.now().minusDays(1));

        assertThat(payments).extracting("order.id").containsOnly(1L);
    }

    @Test
    @Sql("/multiple-payments.sql")
    void findPaymentsByCreditCard() {
        List<Payment> payments = paymentRepository.findByCreditCardNumber("4532756279624064");

        assertThat(payments).extracting("order.id").containsOnly(1L);
    }
}
