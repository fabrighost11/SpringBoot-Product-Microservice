package org.products.repository;

import org.products.model.Product;
import org.products.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    boolean existsByProductType(ProductType type);
    List<Product> findByProductType(ProductType type);
}
