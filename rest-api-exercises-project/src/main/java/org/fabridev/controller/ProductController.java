package org.fabridev.controller;

import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.model.Product;
import org.fabridev.service.ProductService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public Product addProductById(@RequestBody Product product){
        return service.save(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Integer id, @RequestBody Product product){
        return service.updateProduct(id,product);

    }

//    @PatchMapping("/{id}")
//    public Product partialUpdate(@PathVariable Integer id, @RequestBody Product product){
//        return service.partialUpdateProduct(id,product);
//    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id){
        service.deleteProduct(id);
    }

}
