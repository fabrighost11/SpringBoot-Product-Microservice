package org.fabridev.controller;

import org.fabridev.exception.ProductAlreadyExistsException;
import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.exception.InvalidProductException;
import org.fabridev.model.Product;
import org.fabridev.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api-rest/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) throws ProductNotFoundException{
         return new ResponseEntity<>(service.findProductById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll(){
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> addProductById(@RequestBody Product product) throws InvalidProductException, ProductAlreadyExistsException {
        return new ResponseEntity<>(service.save(product), HttpStatus.CREATED);
    }
//
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product product) throws ProductNotFoundException, InvalidProductException{
        return new ResponseEntity<>(service.updateProduct(id,product), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Integer id) throws ProductNotFoundException, InvalidProductException {
        service.deleteProduct(id);
    }

}
