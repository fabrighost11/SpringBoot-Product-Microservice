package org.fabridev.controller;

import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api-rest/product")
public class ProductController {

    private final List<Product> productList = new ArrayList<>();

    public ProductController(){
        productList.add(new Product(1,"Lavarropas",250.99));
        productList.add(new Product(2,"Televisor",480.75));
        productList.add(new Product(3,"Telefono móvil",135.99));
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id) {
            Optional<Product> product = productList.stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst();
            Product prod = product.orElseThrow(() -> new ProductNotFoundException(id));
            return prod;

    }
}
