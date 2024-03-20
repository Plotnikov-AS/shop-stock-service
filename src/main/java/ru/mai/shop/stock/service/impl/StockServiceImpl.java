package ru.mai.shop.stock.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.mai.shop.stock.generated.dto.AddProductTypeRequest;
import ru.mai.shop.stock.generated.dto.FillProductsRequest;
import ru.mai.shop.stock.generated.dto.ProductTypesResponse;
import ru.mai.shop.stock.generated.dto.ProductsResponse;
import ru.mai.shop.stock.service.StockService;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {


    @Override
    public ProductTypesResponse addProductType(AddProductTypeRequest request) {
        return null;
    }

    @Override
    public ProductsResponse fillProducts(FillProductsRequest request) {
        return null;
    }

    @Override
    public ProductsResponse getAll(Pageable pageable) {
        return null;
    }
}
