package org.fabridev.service;

import org.fabridev.dto.ProductDto;
import org.fabridev.exception.InvalidProductException;
import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.model.Product;
import org.fabridev.model.Type;
import org.fabridev.repository.ProductRepository;
import org.fabridev.response.ProductResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fabridev.model.Type.TECHNOLOGICAL;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService service;

    private Product product;
    private ProductDto productDto;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
       product = new Product(1L,"Headphone",64.0,67, TECHNOLOGICAL);
       productDto = new ProductDto("Headphone", 64.0, 67,TECHNOLOGICAL);
       productResponse = new ProductResponse(1L,"Headphone", 64.0, 67,TECHNOLOGICAL);


    }

    @Test
    void findProductById_findProductById_returnProductData() throws Exception {
        //given
        ProductResponse expected = productResponse;

        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));

        //when
        ProductResponse actual = service.findProductById(productId);

        //then
        Assertions.assertEquals(expected,actual);

    }
//
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
        when(productRepository.save(new Product(null, productDto.getName(), productDto.getPrice(), productDto.getStock(), productDto.getType()))).thenReturn(product);

        //when
        ProductResponse actual = service.createProduct(productDto);

        //then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void updateProduct_updateProduct_returnProductUpdated() throws Exception {
        //given
        ProductResponse expected = productResponse;
        ProductDto productDto1 = new ProductDto("name", 24.0, 2, Type.FURNITURE);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        //when
        ProductResponse actual = service.updateProduct(1L, productDto1);

        //then
        Assertions.assertNotEquals(expected, actual); // Check for reference inequality
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
        when(productRepository.save(any(Product.class))).thenReturn(product);

        //when
        ProductResponse actual = service.createProduct(productDto);

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
        when(productRepository.save(any(Product.class))).thenReturn(null);

        //when
        ProductResponse actual = service.createProduct(productDto);

        //then
        Assertions.assertNull(actual);
    }
}

