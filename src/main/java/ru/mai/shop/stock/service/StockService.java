package ru.mai.shop.stock.service;

import org.springframework.data.domain.Pageable;
import ru.mai.shop.stock.generated.dto.AddProductTypeRequest;
import ru.mai.shop.stock.generated.dto.FillProductsRequest;
import ru.mai.shop.stock.generated.dto.PagedProductsResponse;
import ru.mai.shop.stock.generated.dto.ProductTypesResponse;
import ru.mai.shop.stock.generated.dto.ProductsResponse;

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
}
