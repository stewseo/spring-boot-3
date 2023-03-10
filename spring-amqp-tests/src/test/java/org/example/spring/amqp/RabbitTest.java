package org.example.spring.amqp;

import org.example.spring.amqp.StockWatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

// integration tests interaction between the Spring Boot application and the RabbitMQ message broker.
@SpringBootTest
@Testcontainers  // Annotation to give testcontainers hooks into the junit lifecycle
public class RabbitTest {

    @Container
    static RabbitMQContainer container = new RabbitMQContainer(
            DockerImageName.parse("rabbitmq").withTag("3-management-alpine"));

    // Add environment variables as if they were in our application.properties file
    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) { // Registry used with @DynamicPropertySource methods so that they can add properties to the Environment that have dynamically resolved values.
        // assign connection details to spring boot properties in advance
        registry.add("spring.rabbitmq.host", container::getHost);
        registry.add("spring.rabbitmq.port", container::getAmqpPort);
    }

    @Autowired
    StockWatcher stockWatcher;

    @Test
    void rabbitTest() throws InterruptedException {
        // Give the test some time to send some message.
        Thread.sleep(5000);
        assertThat(stockWatcher.getTrades()).hasSizeGreaterThan(0);
    }

}
