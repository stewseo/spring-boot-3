package org.example.spring.amqp;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// Service that generates random stock action
@Service
@EnableScheduling
public class StockMarket {
    List<String> STOCK_SYMBOLS = List.of("ATT", "SBUX", "ZOOM");
    Random RAND = new Random();

    // Specifies a basic set of AMQP operations
    AmqpTemplate template;
    Map<String, StockMovement> lastTrade;

    public StockMarket(AmqpTemplate template) {
        this.template = template;
        lastTrade = new HashMap<>();
        STOCK_SYMBOLS.forEach(symbol -> this.lastTrade.put(symbol, new StockMovement(symbol, RAND.nextFloat() + 100.0f)));
    }

    // Every 500 ms generates a market movement
    // Publishes the converted message to our "stock-market" topic exchange
    // Routing key that binds a queue to our exchange: stock name of the last trade
    @Scheduled(fixedRate = 500L)
    void marketMovement() {
        StockMovement lastTrade = this.lastTrade.get(randomStockSymbol());
        StockMovement newTrade = new StockMovement(lastTrade.getStockName(), newPrice(lastTrade.getPrice()));
        template.convertAndSend("stock-market", lastTrade.getStockName(), newTrade);
    }

    private float newPrice(float oldPrice) {
        return oldPrice * (RAND.nextFloat() + 0.5f);
    }

    private String randomStockSymbol() {
        return STOCK_SYMBOLS.get(RAND.nextInt(STOCK_SYMBOLS.size()));
    }
}
