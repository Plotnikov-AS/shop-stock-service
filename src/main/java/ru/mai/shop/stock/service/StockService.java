package ru.mai.shop.stock.service;

import org.springframework.data.domain.Pageable;
import ru.mai.shop.stock.generated.dto.AddProductTypeRequest;
import ru.mai.shop.stock.generated.dto.FillProductsRequest;
import ru.mai.shop.stock.generated.dto.PagedProductsResponse;
import ru.mai.shop.stock.generated.dto.ProductDto;
import ru.mai.shop.stock.generated.dto.ProductTypesResponse;
import ru.mai.shop.stock.generated.dto.ProductsResponse;

import java.util.Set;

/**
 * Сервис работы со складом
 */
public interface StockService {

    /**
     * Добавить типы товаров
     *
     * @param request запрос добавления типов
     * @return добавленные типы товаров
     */
    ProductTypesResponse addProductType(AddProductTypeRequest request);

    /**
     * Добавить товары на склад
     *
     * @param request запрос добавления товаров
     * @return добавленные товары
     */
    ProductsResponse fillProducts(FillProductsRequest request);

    /**
     * Получить все товары на складе
     *
     * @return страница товаров
     */
    PagedProductsResponse getAll(Pageable pageable);

    /**
     * Уменьшить количество товара на складе
     *
     * @param products Товары для уменьшения количества
     */
    void decrementProducts(Set<ProductDto> products);

    /**
     * Увеличить количество товаров на складе
     *
     * @param products Товары для увеличения количества
     */
    void incrementProducts(Set<ProductDto> products);
}
