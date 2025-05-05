package org.products.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.products.dto.request.ProductRequest;
import org.products.exception.ProductNotFoundException;
import org.products.dto.response.ProductResponse;
import org.products.service.ProductService;
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
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) throws ProductNotFoundException{
         return new ResponseEntity<>(service.findProductById(id), HttpStatus.OK);
    }


    @Operation(summary = "List of products", description = "Return a list with all existing products")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "List of products not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(){
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }


    @Operation(summary = "Add product", description = "Create new product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created."),
            @ApiResponse(responseCode = "400", description = "Bad Request."),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> addProductById(@Valid @RequestBody ProductRequest productRequest) throws MethodArgumentNotValidException {
        return new ResponseEntity<>(service.createProduct(productRequest), HttpStatus.CREATED);
    }


    @Operation(summary = "Update product", description = "Update an existing product by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request."),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) throws ProductNotFoundException, MethodArgumentNotValidException{
        return new ResponseEntity<>(service.updateProduct(id, productRequest), HttpStatus.OK);

    }

    @Operation(summary = "Delete product", description = "Delete a product by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws ProductNotFoundException {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
