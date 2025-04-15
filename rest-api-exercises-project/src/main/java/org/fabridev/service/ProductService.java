package org.fabridev.service;

import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final List<Product> productList = new ArrayList<>();

    public ProductService() {
        productList.add(new Product(1,"Lavarropas",250.99));
        productList.add(new Product(2,"Televisor",480.75));
        productList.add(new Product(3,"Telefono móvil",135.99));
    }

    public Optional<Product> findProductById(Integer id) {
        return productList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }
}
