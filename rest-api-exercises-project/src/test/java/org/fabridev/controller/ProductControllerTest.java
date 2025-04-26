package org.fabridev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fabridev.dto.ProductDto;
import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.model.Type;
import org.fabridev.response.ProductResponse;
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

    @MockBean
    ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductResponse productResponse;
    private ProductDto productDto;

    @BeforeEach
    void setUp(){
        productResponse = new ProductResponse(1L,"Byke",1200.00,21,Type.SPORTS);
        productDto = new ProductDto("Byke",1200.00,21,Type.SPORTS);
    }

    @Test
    void getProductById_validId_returnsProductResponse() throws Exception {
        // Given
        Long idProduct = 1L;
        Mockito.when(service.findProductById(idProduct)).thenReturn(productResponse);

        // When
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/product/{id}", idProduct));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Byke"))
                .andExpect(jsonPath("$.price").value(1200.00))
                .andExpect(jsonPath("$.stock").value(21))
                .andExpect(jsonPath("$.type").value("SPORTS"));
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
                .andExpect(jsonPath("$[0]name").value("Byke"))
                .andExpect(jsonPath("$[0]price").value(1200.00))
                .andExpect(jsonPath("$[0]stock").value(21))
                .andExpect(jsonPath("$[0]type").value("SPORTS"));

    }

    @Test
    void addProductById_createProduct_returnProduct() throws Exception{
        //given
        Long idProduct = 1L;
        ProductResponse createdProduct = new ProductResponse(idProduct,productDto.getName(),productDto.getPrice(), productDto.getStock(), productDto.getType());
        when(service.createProduct(any(ProductDto.class))).thenReturn(createdProduct);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)));

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Byke"))
                .andExpect(jsonPath("$.price").value(1200.00))
                .andExpect(jsonPath("$.stock").value(21))
                .andExpect(jsonPath("$.type").value("SPORTS"));
    }

    @Test
    void addProductById_createProductWithEmptyName_returnException() throws Exception{
        //given
        Long idProduct = 4L;
        productDto.setName("");

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name: Name of the product cant be empty."));

    }

    @Test
    void addProductById_createProductWithPriceLowerThanZero_returnException() throws Exception{
        //given
        productDto.setPrice(0.0);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("price: Price of the product cant be lower than zero."));

    }

    @Test
    void addProductById_createProductWithNullThanZero_returnException() throws Exception{
        //given
        productDto.setPrice(null);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("price: Price of the product cant be null."));

    }

    @Test
    void addProductById_createProductWithNullStock_returnException() throws Exception{
        //given
        productDto.setStock(null);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("stock: Stock cant be null."));

    }

    @Test
    void addProductById_createProductStockLowerThanZero_returnException() throws Exception{
        //given
        productDto.setStock(-99);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("stock: Stock must be zero or higher."));

    }

    @Test
    void addProductById_createProductWithNullType_returnException() throws Exception{
        //given
        productDto.setType(null);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("type: Type of product cant be null."));

    }

    @Test
    void updateProduct_updatedProductInfo_returnProduct() throws Exception{
        //given
        Long idProduct = 1L;
        productDto.setName("Table");
        productDto.setPrice(45.0);
        productDto.setStock(78);
        productDto.setType(Type.FURNITURE);
        ProductResponse updatedProduct = new ProductResponse(idProduct,productDto.getName(), productDto.getPrice(), productDto.getStock(), productDto.getType());
        when(service.updateProduct(eq(idProduct), any(ProductDto.class))).thenReturn(updatedProduct);

        //when
        ResultActions result = mockMvc.perform(put("/api/product/{id}",idProduct)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Table"))
                .andExpect(jsonPath("$.price").value(45.0))
                .andExpect(jsonPath("$.stock").value(78))
                .andExpect(jsonPath("$.type").value("FURNITURE"));
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
        productResponseListList.add( new ProductResponse(2L,"Notebook",450.00,45,Type.TECHNOLOGICAL));
        productResponseListList.add(new ProductResponse(3L,"Television",480.75,1,Type.TECHNOLOGICAL));
        productResponseListList.add(new ProductResponse(4L,"Washer machine",135.99,22,Type.HOME_APPLIANCE));
        return productResponseListList;
    }

}