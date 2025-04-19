package org.fabridev.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyInt;
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
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/api-rest/product/{id}", 1));

        // Then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Lavarropas"))
                .andExpect(jsonPath("$.price").value(250.99));
    }

    @Test
    void getProductById_invalidId_returnsProductNotFoundException() throws Exception {
        // Given
        Mockito.when(service.findProductById(anyInt())).thenThrow(new ProductNotFoundException());

        // When
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/api-rest/product/{id}", product.getId()));

        // Then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product with this ID not found."));
    }

}
