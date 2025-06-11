package org.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.products.api.userClient.UserClient;
import org.products.dto.request.ProductRequest;
import org.products.dto.response.ProductResponse;
import org.products.dto.response.UserResponse;
import org.products.exception.ResourceNotFoundException;
import org.products.model.Product;
import org.products.model.ProductType;
import org.products.security.JwtUtil;
import org.products.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    @MockBean
    private ProductService service;

    private Product product;
    private ProductType productType;
    private ProductResponse productResponse;
    private ProductRequest productRequest;
    private String token;
    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp(){
        token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9BRE1JTiIsInN1YiI6Im1hcmlvQGdtYWlsLmNvbSIsImV4cCI6MTc0OTEzMzA3MSwiaWF0IjoxNzQ4NTI4MjcxfQ.tYpiLdH5LNozZtcZ3fNiCEsXG9HHPd0cVQVJOEvf3d8";
        productType = new ProductType(1L,"HOME_APPLIANCE");
        product = new Product(1L,"Washing machine",250.99,22,productType);
        productResponse = new ProductResponse(1L,"Washing machine",250.99,22, productType.getId(), productType.getName());
        productRequest = new ProductRequest("Washing machine",250.99,22, productType.getId());
    }

    @Test
    void getProductById_validId_returnsProductResponse() throws Exception {
        // Given
        Long idProduct = 1L;
        Mockito.when(service.getProductById(idProduct)).thenReturn(productResponse);

        // When
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/product/{id}", idProduct)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ token));

        // Then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Washing machine"))
                    .andExpect(jsonPath("$.price").value(250.99))
                    .andExpect(jsonPath("$.stock").value(22))
                    .andExpect(jsonPath("$.productTypeId").value(1L))
                    .andExpect(jsonPath("$.productTypeName").value("HOME_APPLIANCE"));

    }

    @Test
    void findAll_findAllProducts_returnProductList() throws Exception {
        //given
        List<ProductResponse>productResponseList = fillProductsResponse();
        Mockito.when(service.findAll()).thenReturn(productResponseList);

        //when
        ResultActions result = mockMvc.perform(get("/api/product")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ token));

        //then
                result.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(4))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Washing machine"))
                .andExpect(jsonPath("$[0].price").value(250.99))
                .andExpect(jsonPath("$[0].stock").value(22))
                .andExpect(jsonPath("$[0].productTypeId").value(1))
                .andExpect(jsonPath("$[0].productTypeName").value("HOME_APPLIANCE"));

    }

    @Test
    void addProductById_createProduct_returnProduct() throws Exception{
        //given
        Long idProduct = 1L;
        ProductResponse expected = productResponse;
        when(service.createProduct(any(ProductRequest.class))).thenReturn(expected);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Washing machine"))
                .andExpect(jsonPath("$.price").value(250.99))
                .andExpect(jsonPath("$.stock").value(22))
                .andExpect(jsonPath("$.productTypeId").value(1))
                .andExpect(jsonPath("$.productTypeName").value("HOME_APPLIANCE"));
    }

    @Test
    void updateProduct_updatedProductInfo_returnProduct() throws Exception{
        //given
        Long idProduct = 1L;
        productRequest.setName("Table");
        productRequest.setPrice(45.0);
        productRequest.setStock(78);
        productRequest.setProductTypeId(productType.getId());
        ProductResponse updatedProduct = new ProductResponse(idProduct, productRequest.getName(), productRequest.getPrice(), productRequest.getStock(),1L,"HOME_APPLIANCE");
        when(service.updateProduct(eq(idProduct), any(ProductRequest.class))).thenReturn(updatedProduct);

        //when
        ResultActions result = mockMvc.perform(put("/api/product/{id}",idProduct)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Table"))
                .andExpect(jsonPath("$.price").value(45.0))
                .andExpect(jsonPath("$.stock").value(78))
                .andExpect(jsonPath("$.productTypeId").value(1))
                .andExpect(jsonPath("$.productTypeName").value("HOME_APPLIANCE"));
    }

    @Test
    void deleteProduct_deleteProductById_returnVoid() throws Exception{
        //given
        Long idProduct = 1L;

        //when
        ResultActions result = mockMvc.perform(delete("/api/product/{id}", idProduct)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ token));

        //then
        result.andExpect(status().isNoContent());
        verify(service).deleteProduct(idProduct);
    }

    @Test
    void getStock_findStockSuccessfully_returnStock() throws Exception{
        Long idProduct = 1L;

        when(service.getStock(idProduct)).thenReturn(22);


        ResultActions result = mockMvc.perform(get("/api/product/{id}/stock", idProduct)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ token));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(22));
    }

    @Test
    void updateStock_updateStockSuccessfully_returnStock() throws Exception {
        Long productId = 1L;
        Long userId = 1L;
        Map<String, Integer> stockUpdateRequest = new HashMap<>();
        stockUpdateRequest.put("stock", 50);

        when(userClient.getUserById(anyLong(),eq(token))).thenReturn(new UserResponse(userId, "Gabriel","gabriel@gmail.com","ADMIN"));

        doNothing().when(service).updatedStock(productId, userId, 50,token);

        ResultActions result = mockMvc.perform(patch("/api/product/stock/{id}/{userId}", productId, userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stockUpdateRequest)));

        result.andExpect(status().isOk());

    }

//    @Test
//    void decreaseStock_decreaseStockSuccessfully_returnStock() throws Exception {
//        Long productId = 1L;
//        Long userId = 1L;
//        Map<String, Integer> stockDecreaseRequest = new HashMap<>();
//        stockDecreaseRequest.put("quantity", 50);
//
//        doNothing().when(service).decreaseStock(anyLong(), anyInt(), anyString());
//
//        ResultActions result = mockMvc.perform(put("/api/product/{id}/stock/decrease", productId)
//        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ token)
//        .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(stockDecreaseRequest)));
//
//        result.andExpect(status().isNoContent());
//    }


    private List<ProductResponse> fillProductsResponse(){
        List<ProductResponse> productResponseListList = new ArrayList<>();
        productResponseListList.add(productResponse);
        productResponseListList.add( new ProductResponse(2L,"Notebook",450.00,45, 1L,productType.getName()));
        productResponseListList.add(new ProductResponse(3L,"Television",480.75,1, 1L,productType.getName()));
        productResponseListList.add(new ProductResponse(4L,"Washer machine",135.99,22, 1L,productType.getName()));
        return productResponseListList;
    }

}