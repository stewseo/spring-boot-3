package org.example.spring.amqp;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Behavior: This class has a method that listens for incoming stock trade messages using RabbitMQ messaging and adds the received trade information to a list of trades.
 * Purpose: To monitor stock trades in real-time using RabbitMQ messaging and store trade information for future processing and analysis.
 * Goal: To provide a centralized location for monitoring and analyzing incoming stock trade information.
 */
@Component
public class StockWatcher {

    List<StockMovement> trades = new ArrayList<>();

    /**
     * Marks this method as a message listener for RabbitMQ messages.
     * The bindings parameter defines the exchange and queue binding details for the listener method.
     * Whenever a message arrives on the specified exchange and queue, the annotated method is called, and the message is passed as an argument to the method.
     * @param trade The StockMovement object received from RabbitMQ
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue, //
                    exchange = @Exchange(value = "stock-market", type= ExchangeTypes.TOPIC), //
                    key = "*")
    )
    void listenForStockTrades(StockMovement trade) {
//        System.out.println("Received and update on " + trade.getStockName() + " " );
        trades.add(trade);
    }

    public List<StockMovement> getTrades() {
        return trades;
    }

}
