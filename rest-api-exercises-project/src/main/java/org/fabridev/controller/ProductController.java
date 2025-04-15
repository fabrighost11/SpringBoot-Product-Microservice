package org.fabridev.controller;

import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.model.Product;
import org.fabridev.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api-rest/product")
public class ProductController {

    private ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id){
         return service.findProductById(id)
                 .orElseThrow(() -> new ProductNotFoundException(id));
    }

}
