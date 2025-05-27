package org.products.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.products.dto.request.ProductTypeRequest;
import org.products.dto.response.ProductTypeResponse;
import org.products.exception.ProductTypeNotFoundException;
import org.products.exception.ProductTypeRelatedException;
import org.products.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/product-type")
@Tag(name = "Types of products", description = "CRUD operation on types of products")
public class ProductTypeController {

    @Autowired
    private ProductTypeService service;

    @Operation(summary = "Get type of product by ID", description = "Search a type of product by its ID a return it.")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Product type not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeResponse> getProductById(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.findProductTypeById(id), HttpStatus.OK);
    }


    @Operation(summary = "List of types of products", description = "Return a list with all types existing.")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "List of types not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping
    public ResponseEntity<List<ProductTypeResponse>> findAll(){
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Add a product type.", description = "Create new type.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created."),
            @ApiResponse(responseCode = "400", description = "Bad Request."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @Secured("ROLE_ADMIN")
    @PostMapping("/{userId}")
    public ResponseEntity<ProductTypeResponse> addProductType(@PathVariable Long userId, @Valid @RequestBody ProductTypeRequest productTypeRequest,  @RequestHeader("Authorization") String token) throws MethodArgumentNotValidException {
        return new ResponseEntity<>(service.createProductType(userId,productTypeRequest,token), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a product type.", description = "Update an existing product type by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Type not found."),
            @ApiResponse(responseCode = "400", description = "Bad Request."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<ProductTypeResponse> updateProductType(@PathVariable Long id, @Valid @RequestBody ProductTypeRequest productTypeRequest) throws ProductTypeNotFoundException, MethodArgumentNotValidException{
        return new ResponseEntity<>(service.updateProductType(id, productTypeRequest), HttpStatus.OK);
    }


    @Operation(summary = "Delete product type.", description = "Delete a type by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Type not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductType(@PathVariable Long id) throws ProductTypeNotFoundException, ProductTypeRelatedException {
        service.deleteProductType(id);
        return ResponseEntity.noContent().build();
    }
}
