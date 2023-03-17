## Testing our Spring AMQP listeners

This project demonstrates how to test a stock ticker system built on top of RabbitMQ (AMQP) and Spring AMQP. RabbitMQ is a message broker that uses AMQP to specify message formats, APIs, and other related information. Spring AMQP takes advantage of these features to provide a simple way to use RabbitMQ with Spring.

### Project Details

The project includes a POJO class called StockMovement that has a stock's name and current price. One service randomly generates stock movements, while another consumes them.

The StockMarketService is a Spring-activated bean that uses scheduling every 500 milliseconds to simulate a stock system. It randomly selects a stock symbol, looks up the last trade, generates a new price, and then randomizes the stock's movement up or down. The latest trade is sent using the Spring AMQP template.

On the other end, there's a StockWatcher that listens to the message broker. It's a Spring component called a Rabbit Listener that's bound to a Topic Exchange called Stock Market. This exchange is a message broker that receives messages from an exchange. In AMQP, all messages go into an exchange, and how they reach their destination depends on the settings. A queue-based exchange sends one message in and one message out, while a topic-based exchange sends one message in but can send it to multiple consumers. Finally, there's a fan-out exchange where one message goes in and is sent to everyone connected to it.

### Focus on Testing

The focus of this project is on testing. We are using Testcontainers, a tool that allows us to create and manage Docker containers in our tests. In this project, we use Testcontainers to create a RabbitMQ container.

To get started, we need to add some annotations to our test class. We use the @SpringBootTest annotation to indicate that we are testing a Spring Boot application. We also use the @Testcontainers annotation to indicate that we are using Testcontainers.

We create a RabbitMQ container using Testcontainers. The container is created by calling the GenericContainer constructor and passing in the Docker image name and tag. We also annotate the container with @Container to indicate that this container should be started before the tests run.

However, there is one issue that we need to address. When the RabbitMQ container starts, it will have a certain hostname and IP address, but when Spring Boot starts up, it assumes that there is a RabbitMQ broker running somewhere. We can't guarantee that in a Docker-based test containers environment. To solve this issue, we use dynamic properties to configure the connection to the RabbitMQ broker.

### Dynamic Properties

We use dynamic properties to configure the connection to the RabbitMQ broker. In our application.yml file, we define the spring.rabbitmq.host and spring.rabbitmq.port properties as placeholders. In our test class, we use the @DynamicPropertySource annotation to set these properties to the values of the RabbitMQ container's host and port.

By using dynamic properties, we ensure that the test case can connect to the RabbitMQ broker running inside the Docker container created by Testcontainers.

### Conclusion

This project demonstrates how to use Testcontainers to test a Spring Boot application that uses RabbitMQ (AMQP) and Spring AMQP. By using dynamic properties, we ensure that the test case can connect to the RabbitMQ broker running inside the Docker container created by Testcontainers.