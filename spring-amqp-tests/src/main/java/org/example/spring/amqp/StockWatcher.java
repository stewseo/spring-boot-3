package org.example.spring.amqp;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StockWatcher {

    List<StockMovement> trades = new ArrayList<>();

    // Target a Rabbit message listener on the specified bindings:
    // queue, "stock-market" topic exchange, the binding key "*".
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
