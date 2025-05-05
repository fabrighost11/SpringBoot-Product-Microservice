package org.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.products.dto.request.ProductRequest;
import org.products.dto.response.ProductResponse;
import org.products.model.Product;
import org.products.model.ProductType;
import org.products.service.ProductService;
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


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService service;

    private Product product;
    private ProductType productType;
    private ProductResponse productResponse;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp(){
        product = new Product(1L,"Washing machine",250.99,22,productType);
        productType = new ProductType(1L,"HOME_APPLIANCE");
        productResponse = new ProductResponse(1L,"Washing machine",250.99,22, productType.getName());
        productRequest = new ProductRequest("Washing machine",250.99,22, productType.getId());
    }

    @Test
    void getProductById_validId_returnsProductResponse() throws Exception {
        // Given
        Long idProduct = 1L;
        productResponse.setId(idProduct);
        Mockito.when(service.findProductById(idProduct)).thenReturn(productResponse);

        // When
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/product/{id}", idProduct));

        // Then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Washing machine"))
                    .andExpect(jsonPath("$.price").value(250.99))
                    .andExpect(jsonPath("$.stock").value(22))
                    .andExpect(jsonPath("$.type").value("HOME_APPLIANCE"));

    }

    @Test
    void findAll_findAllProducts_returnProductList() throws Exception {
        //given
        List<ProductResponse>productResponseList = fillProductsResponse();
        Mockito.when(service.findAll()).thenReturn(productResponseList);

        //when
        ResultActions result = mockMvc.perform(get("/api/product"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(4))
                .andExpect(jsonPath("$[0]id").value(1))
                .andExpect(jsonPath("$[0]name").value("Washing machine"))
                .andExpect(jsonPath("$[0]price").value(250.99))
                .andExpect(jsonPath("$[0]stock").value(22))
                .andExpect(jsonPath("$[0]type").value("HOME_APPLIANCE"));

    }

    @Test
    void addProductById_createProduct_returnProduct() throws Exception{
        //given
        Long idProduct = 1L;
        ProductResponse expected = productResponse;
        when(service.createProduct(any(ProductRequest.class))).thenReturn(expected);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)));

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Washing machine"))
                .andExpect(jsonPath("$.price").value(250.99))
                .andExpect(jsonPath("$.stock").value(22))
                .andExpect(jsonPath("$.type").value("HOME_APPLIANCE"));
    }

    @Test
    void addProductById_createProductWithEmptyName_returnException() throws Exception{
        //given
        Long idProduct = 4L;
        productRequest.setName("");

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name: Name of the product cant be empty."));

    }

    @Test
    void addProductById_createProductWithPriceLowerThanZero_returnException() throws Exception{
        //given
        productRequest.setPrice(0.0);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("price: Price of the product cant be lower than zero."));

    }

    @Test
    void addProductById_createProductWithNullThanZero_returnException() throws Exception{
        //given
        productRequest.setPrice(null);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("price: Price of the product cant be null."));

    }

    @Test
    void addProductById_createProductWithNullStock_returnException() throws Exception{
        //given
        productRequest.setStock(null);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("stock: Stock cant be null."));

    }

    @Test
    void addProductById_createProductStockLowerThanZero_returnException() throws Exception{
        //given
        productRequest.setStock(-99);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("stock: Stock must be zero or higher."));

    }

    @Test
    void addProductById_createProductWithNullType_returnException() throws Exception{
        //given
        productRequest.setType(null);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("type: Type of product cant be null."));

    }

    @Test
    void updateProduct_updatedProductInfo_returnProduct() throws Exception{
        //given
        Long idProduct = 1L;
        productRequest.setName("Table");
        productRequest.setPrice(45.0);
        productRequest.setStock(78);
        productRequest.setType(productType.getId());
        ProductResponse updatedProduct = new ProductResponse(idProduct, productRequest.getName(), productRequest.getPrice(), productRequest.getStock(),"HOME_APPLIANCE");
        when(service.updateProduct(eq(idProduct), any(ProductRequest.class))).thenReturn(updatedProduct);

        //when
        ResultActions result = mockMvc.perform(put("/api/product/{id}",idProduct)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Table"))
                .andExpect(jsonPath("$.price").value(45.0))
                .andExpect(jsonPath("$.stock").value(78))
                .andExpect(jsonPath("$.type").value("HOME_APPLIANCE"));
    }

    @Test
    void deleteProduct_deleteProductById_returnVoid() throws Exception{
        //given
        Long idProduct = 1L;

        //when
        ResultActions result = mockMvc.perform(delete("/api/product/{id}", idProduct));

        //then
        result.andExpect(status().isNoContent());
        verify(service).deleteProduct(idProduct);
    }

    private List<ProductResponse> fillProductsResponse(){
        List<ProductResponse> productResponseListList = new ArrayList<>();
        productResponseListList.add(productResponse);
        productResponseListList.add( new ProductResponse(2L,"Notebook",450.00,45, productType.getName()));
        productResponseListList.add(new ProductResponse(3L,"Television",480.75,1, productType.getName()));
        productResponseListList.add(new ProductResponse(4L,"Washer machine",135.99,22, productType.getName()));
        return productResponseListList;
    }

}