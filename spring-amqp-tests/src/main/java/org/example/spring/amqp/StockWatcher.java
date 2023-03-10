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

    // Annotation that marks a method to be the target of a Rabbit message listener on the specified queues() (or bindings()).
    // The containerFactory() identifies the RabbitListenerContainerFactory to use to build the rabbit listener container.
    // If not set, a default container factory is assumed to be available with a bean name of rabbitListenerContainerFactory unless an explicit default has been provided through configuration.
    //Processing of @RabbitListener annotations is performed by registering a RabbitListenerAnnotationBeanPostProcessor. This can be done manually or, more conveniently, through the <rabbit:annotation-driven/> element or EnableRabbit annotation.
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue, // A queue definition used within the bindings attribute of a QueueBinding.
                    exchange = @Exchange(value = "stock-market", type= ExchangeTypes.TOPIC), // An exchange to which to bind a RabbitListener queue.
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
