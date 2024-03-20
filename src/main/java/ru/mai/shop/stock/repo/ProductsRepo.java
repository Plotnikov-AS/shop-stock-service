package ru.mai.shop.stock.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.mai.shop.stock.entity.Product;
import ru.mai.shop.stock.entity.ProductType;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ProductsRepo extends JpaRepository<Product, UUID> {

    boolean existsByType(ProductType type);

    @Query(value = """
            select p.* from products p
                join product_type type on type.id = p.type
            where type.name = :name
                and p.price = :price
            """, nativeQuery = true)
    Optional<Product> findByTypeAndPrice(String name, BigDecimal price);
}
