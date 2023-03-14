## Testing our Spring AMQP listeners

### Publish stock market movements to a Topic Exchange
- Schedule a simulated market movement every 500 ms
  - Look up the last trade
  - Generate a new price.
  - Send a new stock as an AMQP message to the "stock-market" Exchange

### Consume stock market movements from RabbitMQ queue
- Attach a queue to our exchange in our consumer class: StockWatcher 

### Use TestContainer's RabbitMQ Container as RabbitMQ Broker
- Use @Container to hook into the JUnit lifecycle.
- Declare static attribute to start container early.

### Add environment variables as if they were in our application.properties file
- DynamicPropertySource
