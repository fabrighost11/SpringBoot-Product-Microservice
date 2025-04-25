package org.fabridev.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.fabridev.dto.ProductDto;
import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.exception.InvalidProductException;
import org.fabridev.response.ProductResponse;
import org.fabridev.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/product")
@Tag(name = "Products", description = "CRUD operation on products")
public class ProductController {

    @Autowired
    private ProductService service;

    @Operation(summary = "Get product by ID", description = "Search a product by its ID a return it")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) throws ProductNotFoundException{
         return new ResponseEntity<>(service.findProductById(id), HttpStatus.OK);
    }

    @Operation(summary = "List of products", description = "Return a list with all existing products")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(){
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Add product", description = "Create new product")
    @PostMapping
    public ResponseEntity<ProductResponse> addProductById(@Valid @RequestBody ProductDto productDto) throws MethodArgumentNotValidException {
        return new ResponseEntity<>(service.createProduct(productDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update product", description = "Update an existing product by its ID")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) throws ProductNotFoundException, MethodArgumentNotValidException{
        return new ResponseEntity<>(service.updateProduct(id,productDto), HttpStatus.OK);

    }

    @Operation(summary = "Delete product", description = "Delete a product by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws ProductNotFoundException {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
