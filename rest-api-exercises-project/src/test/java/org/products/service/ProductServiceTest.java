package org.products.service;

import org.products.dto.request.ProductRequest;
import org.products.exception.ProductNotFoundException;
import org.products.model.Product;
import org.products.model.ProductType;
import org.products.repository.ProductRepository;
import org.products.dto.response.ProductResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.products.repository.ProductTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductTypeRepository productTypeRepository;

    @InjectMocks
    private ProductService service;

    private ProductType productType;
    private Product product;
    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productType = new ProductType(3L,"TECHNOLOGICAL");
       product = new Product(1L,"Headphone",64.0,67, productType);
       productRequest = new ProductRequest("Headphone", 64.0, 67,productType.getId());
       productResponse = new ProductResponse(1L,"Headphone", 64.0, 67,productType.getName());


    }

    @Test
    void findProductById_findProductById_returnProductData() throws Exception {
        //given
        ProductResponse expected = productResponse;
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        //when
        ProductResponse actual = service.findProductById(productId);

        //then
        Assertions.assertEquals(expected,actual);

    }

    @Test
    void findProductById_productIdNotFound_returnException(){
        //given
        Long idNonExistent = 99L;
        String expected = "Product with this ID not found.";
        when(productRepository.findById(idNonExistent)).thenReturn(Optional.empty());

        //when
        ProductNotFoundException actualException = assertThrows(
                ProductNotFoundException.class,
                () -> service.findProductById(idNonExistent)
        );

        //then
        Assertions.assertEquals(expected,actualException.getMessage());
    }

    @Test
    void findAll_findAllProductsInTheList_returnProductList() throws Exception{
        //given
        List<ProductResponse> expected = new ArrayList<>();
        expected.add(productResponse);
        when(productRepository.findAll()).thenReturn((List.of(product)));

        //when
        List<ProductResponse> actual = service.findAll();

        //then
        Assertions.assertEquals(expected.size(),actual.size());
        Assertions.assertEquals(expected.get(0).getId(),actual.get(0).getId());
        Assertions.assertEquals(expected.get(0).getName(),actual.get(0).getName());
        Assertions.assertEquals(expected.get(0).getPrice(),actual.get(0).getPrice());
        Assertions.assertEquals(expected.get(0).getStock(),actual.get(0).getStock());
        Assertions.assertEquals(expected.get(0).getType(),actual.get(0).getType());

    }

    @Test
    void save_createProducts_returnProductCreated() throws Exception {
        //given
        ProductResponse expected = productResponse;
        Long productTypeId = 3L;
        when(productTypeRepository.findById(productTypeId)).thenReturn(Optional.of(productType));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        //when
        ProductResponse actual = service.createProduct(productRequest);

        //then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void updateProduct_updateProduct_returnProductUpdated() throws Exception {
        //given
        ProductResponse expected = productResponse;
        ProductType newType = new ProductType(2L,"FURNITURE");
        Long productTypeId = 2L;
        Long productId = 1L;
        ProductRequest request = new ProductRequest("home",454.00,21,2L);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productTypeRepository.findById(productTypeId)).thenReturn(Optional.of(productType));
        when(productRepository.save(any(Product.class))).thenReturn(new Product(1L,"home",454.00,21,newType));

        //when
        ProductResponse actual = service.updateProduct(productId, request);

        //then
        Assertions.assertEquals(1L,actual.getId());
        Assertions.assertNotEquals(expected.getName(), actual.getName());
        Assertions.assertNotEquals(expected.getPrice(), actual.getPrice());
        Assertions.assertNotEquals(expected.getStock(), actual.getStock());
        Assertions.assertNotEquals(expected.getType(), actual.getType());
    }

    @Test
    void updateProduct_updateProductWithNoExistingId_returnProductNotFoundException() throws Exception {
        //given
        Long idNonExistent = 99L;
        String expected = "Product with this ID not found.";
        when(productRepository.findById(idNonExistent)).thenReturn(Optional.empty());

        //when
        ProductNotFoundException actualException = assertThrows(
                ProductNotFoundException.class,
                () -> service.findProductById(idNonExistent)
        );

        //then
        Assertions.assertEquals(expected,actualException.getMessage());
    }

    @Test
    void deleteProduct_deleteProduct_returnTrue() throws Exception {
        //given
        Long idProduct = 1L;

        when(productRepository.existsById(idProduct)).thenReturn(true);

        //when
        service.deleteProduct(idProduct);

        //then
        verify(productRepository).deleteById(idProduct);
    }

    @Test
    void deleteProduct_deleteProductThatNoExists_returnException(){
        //given
        Long idProduct = 99L;
        when(productRepository.existsById(idProduct)).thenReturn(false);

        //when
        assertThrows(ProductNotFoundException.class, () -> {
            service.deleteProduct(idProduct);
        });

        //then
        verify(productRepository).existsById(idProduct);
    }

    @Test
    void save_createProducts_returnCorrectProductResponse() throws Exception{
        //given

        when(productTypeRepository.findById(3L)).thenReturn(Optional.of(productType));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        //when
        ProductResponse actual = service.createProduct(productRequest);

        //then
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(productResponse.getId(),actual.getId());
        Assertions.assertEquals(productResponse.getName(),actual.getName());
        Assertions.assertEquals(productResponse.getPrice(),actual.getPrice());
        Assertions.assertEquals(productResponse.getStock(),actual.getStock());
        Assertions.assertEquals(productResponse.getType(),actual.getType());
    }

    @Test
    void save_createProducts_returnNull(){
        //given
        when(productTypeRepository.findById(3L)).thenReturn(Optional.of(productType));
        when(productRepository.save(any(Product.class))).thenReturn(null);

        //when
        ProductResponse actual = service.createProduct(productRequest);

        //then
        Assertions.assertNull(actual);
    }
}

