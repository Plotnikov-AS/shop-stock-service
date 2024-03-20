package ru.mai.shop.stock.integration.kafka.service;

import ru.mai.shop.stock.integration.kafka.dto.NewSaleEvent;
import ru.mai.shop.stock.integration.kafka.dto.RefundEvent;

public interface KafkaConsumer {

    void consumeNewSale(NewSaleEvent newSaleEvent);

    void consumeRefund(RefundEvent refundEvent);
}
