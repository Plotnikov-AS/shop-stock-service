package ru.mai.shop.stock.integration.kafka.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.mai.shop.stock.integration.kafka.dto.NewSaleEvent;
import ru.mai.shop.stock.integration.kafka.dto.RefundEvent;
import ru.mai.shop.stock.integration.kafka.service.KafkaConsumer;
import ru.mai.shop.stock.service.StockService;

@Service
@RequiredArgsConstructor
public class KafkaConsumerImpl implements KafkaConsumer {
    private final StockService stockService;

    @Override
    @KafkaListener(
            topics = {"${spring.kafka.topics.new-sale.name}"},
            properties = {"spring.json.value.default.type=ru.mai.shop.stock.integration.kafka.dto.NewSaleEvent"}
    )
    public void consumeNewSale(@Payload NewSaleEvent newSaleEvent) {
        stockService.decrementProducts(newSaleEvent.getProducts());
    }

    @Override
    @KafkaListener(
            topics = {"${spring.kafka.topics.refund.name}"},
            properties = {"spring.json.value.default.type=ru.mai.shop.stock.integration.kafka.dto.RefundEvent"}
    )
    public void consumeRefund(@Payload RefundEvent refundEvent) {
        stockService.incrementProducts(refundEvent.getProducts());
    }
}
