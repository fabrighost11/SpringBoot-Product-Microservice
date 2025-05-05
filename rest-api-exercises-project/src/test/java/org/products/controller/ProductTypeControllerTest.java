package org.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.products.dto.request.ProductTypeRequest;
import org.products.dto.response.ProductTypeResponse;
import org.products.model.ProductType;
import org.products.service.ProductTypeService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductTypeService service;

    private ProductType productType;
    private ProductTypeResponse productTypeResponse;
    private ProductTypeRequest productTypeRequest;

    @BeforeEach
    void setUp(){
        productType = new ProductType(1L,"HOME_APPLIANCE");
        productTypeResponse = new ProductTypeResponse(1L,"HOME_APPLIANCE");
        productTypeRequest = new ProductTypeRequest("HOME_APPLIANCE");
    }

    @Test
    void getProductTypeById_validId_returnsProductTypeResponse() throws Exception {
        // Given
        Long idProduct = 1L;
        productTypeResponse.setId(idProduct);
        Mockito.when(service.findProductTypeById(idProduct)).thenReturn(productTypeResponse);

        // When
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/product-type/{id}", idProduct));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("HOME_APPLIANCE"));

    }

    @Test
    void findAll_findAllProductTypes_returnProductList() throws Exception {
        //given
        List<ProductTypeResponse> productTypeResponseList = fillProductTypeResponse();
        Mockito.when(service.findAll()).thenReturn(productTypeResponseList);

        //when
        ResultActions result = mockMvc.perform(get("/api/product-type"))

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(4))
                .andExpect(jsonPath("$[0]id").value(1))
                .andExpect(jsonPath("$[0]name").value("HOME_APPLIANCE"));

    }

    @Test
    void addProductById_createProductTypeSuccessfully_returnProduct() throws Exception{
        //given
        Long idProduct = 1L;
        ProductTypeResponse expected = productTypeResponse;
        when(service.createProductType(any(ProductTypeRequest.class))).thenReturn(expected);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productType)));

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("HOME_APPLIANCE"));
    }

    @Test
    void addProductById_createProductTypeWithEmptyName_returnException() throws Exception{
        //given
        Long idProduct = 4L;
        productTypeRequest.setName("");

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/product-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productTypeRequest)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name: Name cant be empty."));

    }

    @Test
    void updateProductType_updatedProductTypeInfo_returnProduct() throws Exception{
        //given
        Long idProduct = 1L;
        productTypeRequest.setName("ELECTRICAL");
        ProductTypeResponse updatedProductType = new ProductTypeResponse(idProduct, productTypeRequest.getName());
        when(service.updateProductType(eq(idProduct), any(ProductTypeRequest.class))).thenReturn(updatedProductType);

        //when
        ResultActions result = mockMvc.perform(put("/api/product-type/{id}",idProduct)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productTypeRequest)));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("ELECTRICAL"));
    }

    @Test
    void deleteProductType_deleteProductTypeById_returnVoid() throws Exception{
        //given
        Long idProduct = 1L;

        //when
        ResultActions result = mockMvc.perform(delete("/api/product-type/{id}", idProduct));

        //then
        result.andExpect(status().isNoContent());
        verify(service).deleteProductType(idProduct);
    }

    private List<ProductTypeResponse> fillProductTypeResponse(){
        List<ProductTypeResponse> productTypeResponseList = new ArrayList<>();
        productTypeResponseList.add(productTypeResponse);
        productTypeResponseList.add( new ProductTypeResponse(2L,"HOME_APPLIANCE"));
        productTypeResponseList.add(new ProductTypeResponse(3L,"HOME_APPLIANCE"));
        productTypeResponseList.add(new ProductTypeResponse(4L,"Washer HOME_APPLIANCE"));
        return productTypeResponseList;
    }
}
