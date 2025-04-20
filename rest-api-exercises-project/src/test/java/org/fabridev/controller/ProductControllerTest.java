package org.fabridev.controller;

import org.fabridev.exception.InvalidProductException;
import org.fabridev.exception.ProductAlreadyExistsException;
import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.model.Product;
import org.fabridev.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    private Product product;

    @BeforeEach
    void setUp(){
        product = new Product(1,"Lavarropas",250.99);
    }

    @Test
    void getProductById_validId_returnsProduct() throws Exception {
        // Given
        Integer idProduct = 1;
        Mockito.when(service.findProductById(idProduct)).thenReturn(product);

        // When
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api-rest/product/{id}", 1));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Lavarropas"))
                .andExpect(jsonPath("$.price").value(250.99));
    }

    @Test
    void getProductById_invalidId_returnsProductNotFoundException() throws Exception {
        // Given
        Mockito.when(service.findProductById(anyInt())).thenThrow(new ProductNotFoundException());

        // When
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api-rest/product/{id}", product.getId()));

        // Then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product with this ID not found."));
    }

    @Test
    void addProductById_createProduct_returnProduct() throws Exception{
        //given
        Integer idProduct = 4;
        Product createdProduct = new Product(idProduct,"Mesa",30.50);
        when(service.save(any(Product.class))).thenReturn(createdProduct);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api-rest/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Mesa\", \"price\": 30.50 }"));

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("Mesa"))
                .andExpect(jsonPath("$.price").value(30.50));
    }

    @Test
    void addProductById_createProductWithEmptyName_returnException() throws Exception{
        //given
        when(service.save(any(Product.class))).thenThrow(new InvalidProductException("Name of the product cant be empty."));

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api-rest/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"\", \"price\": 30.50 }"));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Name of the product cant be empty."));

    }

    @Test
    void addProductById_createProductWithPriceLowerThanZero_returnException() throws Exception{
        //given
        when(service.save(any(Product.class))).thenThrow(new InvalidProductException("Price of the product must be higher than 0."));

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api-rest/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Mesa\", \"price\": 0 }"));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Price of the product must be higher than 0."));

    }

    @Test
    void addProductById_createExistingProduct_returnException() throws Exception{
        //given
        when(service.save(any(Product.class))).thenThrow(new ProductAlreadyExistsException("Product already exists."));

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api-rest/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Mesa\", \"price\": 30.50 }"));

        //then
        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Product already exists."));

    }

    @Test
    void updateProduct_updatedProductInfo_returnProduct() throws Exception{
        //given
        Integer idProduct = 3;
        Product updatedProduct = new Product(idProduct,"Laptop",350.00);
        when(service.updateProduct(eq(idProduct), any(Product.class))).thenReturn(updatedProduct);

        //when
        ResultActions result = mockMvc.perform(put("/api-rest/product/{id}",idProduct)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Laptop\", \"price\": 350.00 }"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(350.00));
    }

    @Test
    void updateProduct_updatedProductWithNoName_returnException() throws Exception{
        //given
        when(service.save(any(Product.class))).thenThrow(new InvalidProductException("Name of the product cant be empty."));

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api-rest/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Mesa\", \"price\": 30.50 }"));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Name of the product cant be empty."));

    }

    @Test
    void updateProduct_updatedProductWithPriceLowerThanZero_returnException() throws Exception{
        //given
        when(service.save(any(Product.class))).thenThrow(new InvalidProductException("Price of the product must be higher than 0."));

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api-rest/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Mesa\", \"price\": 30.50 }"));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Price of the product must be higher than 0."));

    }

    @Test
    void deleteProduct_deleteProductById_returnVoid() throws Exception{
        //given
        Integer idProduct = 3;

        //when
        ResultActions result = mockMvc.perform(delete("/api-rest/product/{id}", idProduct));

        //then
        result.andExpect(status().isNoContent());
        verify(service).deleteProduct(idProduct);
    }

    @Test
    void deleteProduct_deleteProductThatNotExists_returnException() throws Exception{
        //given
        Integer idNonExistent = 333;
        doThrow(new ProductNotFoundException()).when(service).deleteProduct(idNonExistent);

        //when
        ResultActions result = mockMvc.perform(delete("/api-rest/product/{id}", idNonExistent));

        //then
        result.andExpect(status().isNotFound());
        verify(service).deleteProduct(idNonExistent);
    }

}
