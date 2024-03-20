package ru.mai.shop.stock.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mai.shop.stock.entity.Product;
import ru.mai.shop.stock.entity.ProductType;
import ru.mai.shop.stock.generated.dto.AddProductTypeRequest;
import ru.mai.shop.stock.generated.dto.FillProductsRequest;
import ru.mai.shop.stock.generated.dto.PagedProductsResponse;
import ru.mai.shop.stock.generated.dto.ProductDto;
import ru.mai.shop.stock.generated.dto.ProductTypeDto;
import ru.mai.shop.stock.generated.dto.ProductTypesResponse;
import ru.mai.shop.stock.generated.dto.ProductsResponse;
import ru.mai.shop.stock.repo.ProductTypesRepo;
import ru.mai.shop.stock.repo.ProductsRepo;
import ru.mai.shop.stock.service.StockService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final ProductTypesRepo productTypesRepo;
    private final ProductsRepo productsRepo;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public ProductTypesResponse addProductType(AddProductTypeRequest request) {
        List<ProductTypeDto> savedTypes = request.getTypes().stream()
                .map(productType -> mapper.map(productType, ProductType.class))
                .filter(productType -> !productTypesRepo.existsByName(productType.getName()))
                .map(productTypesRepo::save)
                .map(savedType -> mapper.map(savedType, ProductTypeDto.class))
                .toList();

        return new ProductTypesResponse()
                .types(savedTypes);
    }

    @Override
    @Transactional
    public ProductsResponse fillProducts(FillProductsRequest request) {
        List<ProductDto> savedProducts = request.getProducts().stream()
                .map(productDto -> {
                    String typeName = productDto.getType().getName();
                    if (productTypesRepo.existsByName(typeName)) {
                        return productsRepo.findByTypeAndPrice(typeName, productDto.getPrice())
                                .map(existingProduct -> updateProduct(existingProduct, productDto, typeName))
                                .orElseGet(() -> productsRepo.save(mapper.map(productDto, Product.class)));
                    } else {
                        throw new IllegalArgumentException("Невозможно добавить товар с несуществующим типом %s"
                                .formatted(typeName));
                    }
                })
                .map(product -> mapper.map(product, ProductDto.class))
                .toList();

        return new ProductsResponse()
                .products(savedProducts);
    }

    private Product updateProduct(Product existingProduct, ProductDto productDto, String typeName) {
        if (existingProduct.getCount() > productDto.getCount()) {
            throw new IllegalArgumentException("Невозможно уменьшить количество товара %s"
                    .formatted(typeName));
        }
        return productsRepo.save(existingProduct
                .setCount(Long.valueOf(productDto.getCount())));
    }

    @Override
    public PagedProductsResponse getAll(Pageable pageable) {
        Page<Product> productsPage = productsRepo.findAll(pageable);

        return new PagedProductsResponse()
                .content(productsPage.stream()
                        .map(product -> mapper.map(product, ProductDto.class))
                        .toList())
                .pageable(pageable)
                .last(productsPage.isLast())
                .first(productsPage.isFirst())
                .empty(productsPage.isEmpty())
                .number(productsPage.getNumber())
                .numberOfElements(productsPage.getNumberOfElements())
                .size(productsPage.getSize())
                .sort(productsPage.getSort())
                .totalElements(productsPage.getTotalElements())
                .totalPages(productsPage.getTotalPages());
    }
}
