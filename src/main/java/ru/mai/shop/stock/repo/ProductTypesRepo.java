package ru.mai.shop.stock.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mai.shop.stock.entity.ProductType;

import java.util.Optional;
import java.util.UUID;

public interface ProductTypesRepo extends JpaRepository<ProductType, UUID> {

    boolean existsByName(String name);

    Optional<ProductType> findByName(String name);
}
