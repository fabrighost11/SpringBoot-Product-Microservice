package org.products.service;

import org.mockito.Spy;
import org.products.api.userClient.UserClient;
import org.products.dto.request.ProductRequest;
import org.products.dto.response.UserResponse;
import org.products.exception.ProductNotFoundException;
import org.products.exception.ResourceNotFoundException;
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

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductTypeRepository productTypeRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    @Spy
    private ProductService service;

    private ProductType productType;
    private Product product;
    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        productType = new ProductType(3L,"TECHNOLOGICAL");
       product = new Product(1L,"Headphone",64.0,67, productType);
       productRequest = new ProductRequest("Headphone", 64.0, 67,productType.getId());
       productResponse = new ProductResponse(1L,"Headphone", 64.0, 67, productType.getId(), productType.getName());


    }

    @Test
    void findProductById_findProductById_returnProductData() throws Exception {
        //given
        ProductResponse expected = productResponse;
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        //when
        ProductResponse actual = service.getProductById(productId);

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
                () -> service.getProductById(idNonExistent)
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
        Assertions.assertEquals(expected.get(0).getProductTypeId(),actual.get(0).getProductTypeId());
        Assertions.assertEquals(expected.get(0).getProductTypeName(),actual.get(0).getProductTypeName());

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
        Assertions.assertNotEquals(expected.getProductTypeId(), actual.getProductTypeId());
        Assertions.assertNotEquals(expected.getProductTypeName(), actual.getProductTypeName());
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
                () -> service.getProductById(idNonExistent)
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
        Assertions.assertEquals(productResponse.getProductTypeId(),actual.getProductTypeId());
        Assertions.assertEquals(productResponse.getProductTypeName(),actual.getProductTypeName());
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

    @Test
    void getStock_getStockSuccessfully_returnStock() throws Exception{
        Long idProduct = 1L;
        product.setStock(77);
        Integer expectedStock = 77;
        when(productRepository.findById(idProduct)).thenReturn(Optional.of(product));

        Integer actualStock = service.getStock(idProduct);

        Assertions.assertEquals(expectedStock, actualStock);
        verify(productRepository).findById(idProduct);
    }

    @Test
    void decreaseStock_reduceStockSuccessfully_returnStock() throws Exception{
        Long idProduct = 1L;
        Integer expectedStock = 70;
        Integer quantity = 7;
        product.setStock(77);

        when(productRepository.findById(idProduct)).thenReturn(Optional.of(product));

        service.decreaseStock(idProduct,quantity);

        Assertions.assertEquals(expectedStock,product.getStock());
        verify(productRepository).save(product);
    }

    @Test
    void decreaseStock_notEnoughStock_returnResourceNotFoundException() {
        Long productId = 2L;
        String expected = "Not enough stock";
        Integer quantity = 10;
        product.setStock(7);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ResourceNotFoundException actual = assertThrows(ResourceNotFoundException.class, () ->
                service.decreaseStock(productId, quantity));

        Assertions.assertEquals(expected,actual.getMessage());
    }

    @Test
    void updatedStock_updateStockSuccessfully_returnStock() throws Exception{
        Long userId = 1L;
        Long idProduct = 1L;
        Integer newStock = 10;
        Integer expectedStock = 77;
        userResponse = new UserResponse(1L,"gabriel","gabriel@gmail.com","ADMIN");
        when(userClient.getUserById(userId)).thenReturn(userResponse);
        when(productRepository.findById(idProduct)).thenReturn(Optional.of(product));

        service.updatedStock(idProduct, userId, newStock);

        assertEquals(expectedStock, product.getStock());
    }

    @Test
    void updatedStock_userNotFound_throwsProductNotFoundException() {
        Long productId = 1L;
        Long userId = 999L;
        Integer newStock = 20;
        String expected = "Product with this ID not found.";

        when(userClient.getUserById(userId)).thenReturn(null);

        ProductNotFoundException actual =  assertThrows(ProductNotFoundException.class, () ->
                service.updatedStock(productId, userId, newStock));

        Assertions.assertEquals(expected,actual.getMessage());
    }

    @Test
    void updatedStock_badRequest_throwsProductNotFoundException() {
        Long productId = null;
        Long userId = null;
        Integer newStock = null;
        String expected = "id, userId or Stock quantity cannot be null";

        IllegalArgumentException actual =  assertThrows(IllegalArgumentException.class, () ->
                service.updatedStock(productId, userId, newStock));

        Assertions.assertEquals(expected,actual.getMessage());
    }
}

