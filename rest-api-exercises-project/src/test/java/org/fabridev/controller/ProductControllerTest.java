package org.fabridev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fabridev.dto.ProductDto;
import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.model.Product;
import org.fabridev.model.Type;
import org.fabridev.repository.ProductRepository;
import org.fabridev.response.ProductResponse;
import org.fabridev.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
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

    @BeforeEach
    void setUp(){
        productResponse = new ProductResponse(1L,"Byke",1200.00,21,Type.SPORTS);
    }

    @Test
    void getProductById_validId_returnsProductResponse() throws Exception {
        // Given
        Long idProduct = 1L;
//        ProductResponse productResponse = new ProductResponse(idProduct,"Horno",234.99,44,Type.HOME_APPLIANCE);
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
    void getProductById_invalidId_returnsProductNotFoundException() throws Exception {
        // Given
        Mockito.when(service.findProductById(anyLong())).thenThrow(new ProductNotFoundException());

        // When
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/product/{id}", productResponse.getId()));

        // Then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product with this ID not found."));
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
        Long idProduct = 4L;
        ProductDto productDto = new ProductDto("Table",30.50,34,Type.FURNITURE);
        ProductResponse createdProduct = new ProductResponse(idProduct,productDto.getName(),productDto.getPrice(), productDto.getStock(), productDto.getType());
        when(service.createProduct(any(ProductDto.class))).thenReturn(createdProduct);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)));

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("Table"))
                .andExpect(jsonPath("$.price").value(30.50))
                .andExpect(jsonPath("$.stock").value(34))
                .andExpect(jsonPath("$.type").value("FURNITURE"));
    }

//    @Test
//    void addProductById_createProductWithEmptyName_returnException() throws Exception{
//        //given
//        Long idProduct = 4L;
//        ProductDto productDto = new ProductDto("",30.50,34,Type.FURNITURE);
//        ProductResponse createdProduct = new ProductResponse(idProduct,productDto.getName(),productDto.getPrice(), productDto.getStock(), productDto.getType());
//        when(service.createProduct(any(ProductDto.class))).thenThrow(new MethodArgumentNotValidException("Name of the product cant be empty."));
//
//        //when
//        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api-rest/product")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{ \"name\": \"\", \"price\": 30.50 }"));
//
//        //then
//        result.andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Name of the product cant be empty."));
//
//    }
//
//    @Test
//    void addProductById_createProductWithPriceLowerThanZero_returnException() throws Exception{
//        //given
//        when(service.save(any(Product.class))).thenThrow(new InvalidProductException("Price of the product must be higher than 0."));
//
//        //when
//        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api-rest/product")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{ \"name\": \"Mesa\", \"price\": 0 }"));
//
//        //then
//        result.andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Price of the product must be higher than 0."));
//
//    }
//
//    @Test
//    void addProductById_createExistingProduct_returnException() throws Exception{
//        //given
//        when(service.save(any(Product.class))).thenThrow(new ProductAlreadyExistsException("Product already exists."));
//
//        //when
//        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api-rest/product")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{ \"name\": \"Mesa\", \"price\": 30.50 }"));
//
//        //then
//        result.andExpect(status().isConflict())
//                .andExpect(jsonPath("$.message").value("Product already exists."));
//
//    }
//
//    @Test
//    void updateProduct_updatedProductInfo_returnProduct() throws Exception{
//        //given
//        Integer idProduct = 3;
//        Product updatedProduct = new Product(idProduct,"Laptop",350.00);
//        when(service.updateProduct(eq(idProduct), any(Product.class))).thenReturn(updatedProduct);
//
//        //when
//        ResultActions result = mockMvc.perform(put("/api-rest/product/{id}",idProduct)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{ \"name\": \"Laptop\", \"price\": 350.00 }"));
//
//        //then
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(3))
//                .andExpect(jsonPath("$.name").value("Laptop"))
//                .andExpect(jsonPath("$.price").value(350.00));
//    }
//
//    @Test
//    void updateProduct_updatedProductWithNoName_returnException() throws Exception{
//        //given
//        when(service.save(any(Product.class))).thenThrow(new InvalidProductException("Name of the product cant be empty."));
//
//        //when
//        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api-rest/product")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{ \"name\": \"Mesa\", \"price\": 30.50 }"));
//
//        //then
//        result.andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Name of the product cant be empty."));
//
//    }
//
//    @Test
//    void updateProduct_updatedProductWithPriceLowerThanZero_returnException() throws Exception{
//        //given
//        when(service.save(any(Product.class))).thenThrow(new InvalidProductException("Price of the product must be higher than 0."));
//
//        //when
//        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api-rest/product")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{ \"name\": \"Mesa\", \"price\": 30.50 }"));
//
//        //then
//        result.andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Price of the product must be higher than 0."));
//
//    }
//
//    @Test
//    void deleteProduct_deleteProductById_returnVoid() throws Exception{
//        //given
//        Integer idProduct = 3;
//
//        //when
//        ResultActions result = mockMvc.perform(delete("/api-rest/product/{id}", idProduct));
//
//        //then
//        result.andExpect(status().isNoContent());
//        verify(service).deleteProduct(idProduct);
//    }
//
//    @Test
//    void deleteProduct_deleteProductThatNotExists_returnException() throws Exception{
//        //given
//        Integer idNonExistent = 333;
//        doThrow(new ProductNotFoundException()).when(service).deleteProduct(idNonExistent);
//
//        //when
//        ResultActions result = mockMvc.perform(delete("/api-rest/product/{id}", idNonExistent));
//
//        //then
//        result.andExpect(status().isNotFound());
//        verify(service).deleteProduct(idNonExistent);
//    }
//
    private List<ProductResponse> fillProductsResponse(){
        List<ProductResponse> productResponseListList = new ArrayList<>();
        productResponseListList.add(productResponse);
        productResponseListList.add( new ProductResponse(2L,"Notebook",450.00,45,Type.TECHNOLOGICAL));
        productResponseListList.add(new ProductResponse(3L,"Television",480.75,1,Type.TECHNOLOGICAL));
        productResponseListList.add(new ProductResponse(4L,"Washer machine",135.99,22,Type.HOME_APPLIANCE));
        return productResponseListList;
    }

}