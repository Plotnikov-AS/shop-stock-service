package ru.mai.shop.stock.integration.kafka.dto;

import lombok.Data;
import ru.mai.shop.stock.generated.dto.ProductDto;

import java.util.Set;

@Data
public class RefundEvent {
    private Set<ProductDto> products;
}
