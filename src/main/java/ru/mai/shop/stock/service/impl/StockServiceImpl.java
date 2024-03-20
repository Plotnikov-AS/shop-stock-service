package ru.mai.shop.stock.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Set;

@Slf4j
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
                .peek(saved -> log.info("Добавлен новый тип товара: {}", saved.getName()))
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
                    Product product = productTypesRepo.findByName(typeName)
                            .map(type -> productsRepo.findByTypeAndPrice(type, productDto.getPrice())
                                    .map(existingProduct -> updateProduct(existingProduct, productDto, typeName))
                                    .orElseGet(() -> createNewProduct(productDto, type)))
                            .orElseThrow(() -> new IllegalArgumentException("Невозможно добавить товар с несуществующим типом %s"
                                    .formatted(typeName)));
                    return productsRepo.save(product);
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
        Product updated = productsRepo.save(existingProduct
                .setCount(Long.valueOf(productDto.getCount())));
        log.info("Обновлён товар {}, количество: {}",
                updated.getType().getName(), updated.getCount());

        return updated;
    }

    private Product createNewProduct(ProductDto productDto, ProductType type) {
        Product newProduct = new Product()
                .setCount(Long.valueOf(productDto.getCount()))
                .setPrice(productDto.getPrice())
                .setPlace(productDto.getPlace())
                .setType(type);
        log.info("Добавлен новый товар {}, количество {}",
                newProduct.getType().getName(), newProduct.getCount());

        return newProduct;
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

    @Override
    @Transactional
    public void decrementProducts(Set<ProductDto> products) {
        products.forEach(saledProduct -> productsRepo.findById(saledProduct.getId())
                .map(product -> {
                    Long currentCount = product.getCount();
                    Integer countToDecrement = saledProduct.getCount();
                    if (countToDecrement > currentCount) {
                        throw new IllegalArgumentException("Товара %s недостаточно".formatted(product.getType().getName()));
                    }
                    return product.setCount(currentCount - countToDecrement);
                })
                .map(product -> {
                    Product saved = productsRepo.save(product);
                    log.info("Уменьшено количество товара {} на {}",
                            saved.getType().getName(), product.getCount());
                    return saved;
                }));
    }

    @Override
    @Transactional
    public void incrementProducts(Set<ProductDto> products) {
        products.forEach(refundedProduct -> productsRepo.findById(refundedProduct.getId())
                .map(product -> {
                    Long currentCount = product.getCount();
                    return product.setCount(currentCount + refundedProduct.getCount());
                })
                .map(product -> {
                    Product saved = productsRepo.save(product);
                    log.info("Увеличено количество товара {} на {}",
                            saved.getType().getName(), product.getCount());
                    return saved;
                }));
    }
}
