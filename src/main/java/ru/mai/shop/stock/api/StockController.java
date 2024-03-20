package ru.mai.shop.stock.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.mai.shop.stock.generated.api.StockApi;
import ru.mai.shop.stock.generated.dto.AddProductTypeRequest;
import ru.mai.shop.stock.generated.dto.FillProductsRequest;
import ru.mai.shop.stock.generated.dto.ProductTypesResponse;
import ru.mai.shop.stock.generated.dto.ProductsResponse;
import ru.mai.shop.stock.service.StockService;

@RestController
@RequiredArgsConstructor
public class StockController implements StockApi {
    private final StockService stockService;

    @Override
    public ResponseEntity<ProductTypesResponse> addProductType(AddProductTypeRequest addProductTypeRequest) {
        return ResponseEntity.ok(stockService.addProductType(addProductTypeRequest));
    }

    @Override
    public ResponseEntity<ProductsResponse> fillProducts(FillProductsRequest fillProductsRequest) {
        return ResponseEntity.ok(stockService.fillProducts(fillProductsRequest));
    }

    @Override
    public ResponseEntity<ProductsResponse> getAll(Pageable pageable) {
        return ResponseEntity.ok(stockService.getAll(pageable));
    }
}
