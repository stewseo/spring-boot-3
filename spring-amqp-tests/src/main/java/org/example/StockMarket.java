package org.example;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
// Enables Spring's scheduled task execution capability
@EnableScheduling
public class StockMarket {
    List<String> STOCK_SYMBOLS = List.of("ATT", "SBUX", "ZOOM");
    Random RAND = new Random();

    // Specifies a basic set of AMQP operations. Provides synchronous send and receive methods.
    // The convertAndSend(Object) and receiveAndConvert() methods allow let you send and receive POJO objects.
    // Implementations are expected to delegate to an instance of org.springframework.amqp.support.converter.MessageConverter
    // to perform conversion to and from AMQP byte[] payload type.
    AmqpTemplate template;
    Map<String, StockMovement> lastTrade;

    public StockMarket(AmqpTemplate template) {
        this.template = template;
        lastTrade = new HashMap<>();
        STOCK_SYMBOLS.forEach(symbol -> this.lastTrade.put(symbol, new StockMovement(symbol, RAND.nextFloat() + 100.0f)));
    }

    @Scheduled(fixedRate = 500L)
    void marketMovement() {
        StockMovement lastTrade = this.lastTrade.get(randomStockSymbol());
        StockMovement newTrade = new StockMovement(lastTrade.getStockName(), newPrice(lastTrade.getPrice()));
        // Convert a Java object to an Amqp Message and send it to a specific exchange with a specific routing key.
        //exchange – the name of the exchange routingKey – the routing key message – a message to send
        template.convertAndSend("stock-market", lastTrade.getStockName(), newTrade);
    }

    private float newPrice(float oldPrice) {
        return oldPrice * (RAND.nextFloat() + 0.5f);
    }

    private String randomStockSymbol() {
        return STOCK_SYMBOLS.get(RAND.nextInt(STOCK_SYMBOLS.size()));
    }
}
