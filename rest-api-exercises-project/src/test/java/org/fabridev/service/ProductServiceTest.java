package org.fabridev.service;

import org.fabridev.controller.ProductController;
import org.fabridev.dto.ProductDto;
import org.fabridev.exception.InvalidProductException;
import org.fabridev.exception.ProductAlreadyExistsException;
import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.model.Product;
import org.fabridev.model.Type;
import org.fabridev.repository.ProductRepository;
import org.fabridev.response.ProductResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.fabridev.model.Type.TECHNOLOGICAL;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
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
       product = new Product(1l,"Headphone",64.0,67,TECHNOLOGICAL);
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
//        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getName(),actual.getName());

        verify(productRepository.findById(productId));

    }
////
//    @Test
//    void findProductById_productIdNotFound_returnException() {
//        //given
//        int idNonExistent = 99;
//        String expected = "Product with this ID not found.";
//
//        //when
//        ProductNotFoundException actualException = assertThrows(
//                ProductNotFoundException.class,
//                () -> service.findProductById(idNonExistent)
//        );
//
//        //then
//        Assertions.assertEquals(expected,actualException.getMessage());
//    }
//
//    @Test
//    void findAll_findAllProductsInTheList_returnProductList(){
//        //given
//        List<Product> expected = fillProducts();
//
//        //when
//        List<Product> actual = service.findAll();
//
//        //then
//        Assertions.assertEquals(expected,actual);
//
//    }
//
//    @Test
//    void save_createProducts_returnProductCreated() throws Exception {
//        //given
//        Integer idProduct = 4;
//        Product expected = new Product(idProduct,"Mesa",30.50);
//        Product product2 = new Product(4,"Mesa",30.50);
//
//        //when
//        Product actual = service.save(product2);
//
//        //then
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    void save_failToCreateProductWithNoName_returnInvalidProductException() {
//        //given
//        String expected = "Name of the product cant be empty.";
//        Product product = new Product(4,"",30.50);
//
//        //when
//        InvalidProductException actualException = assertThrows(
//                InvalidProductException.class,
//                () -> service.save(product)
//        );
//
//        //then
//        Assertions.assertEquals(expected,actualException.getMessage());
//    }
//
//    @Test
//    void save_failToCreateProductWithNullName_returnInvalidProductException() {
//        //given
//        String expected = "Name of the product cant be empty.";
//        Product product = new Product(4,null,30.50);
//
//        //when
//        InvalidProductException actualException = assertThrows(
//                InvalidProductException.class,
//                () -> service.save(product)
//        );
//
//        //then
//        Assertions.assertEquals(expected,actualException.getMessage());
//    }
//
//    @Test
//    void save_failToCreateProductWithNullPrice_returnInvalidProductException() {
//        //given
//        String expected = "Price of the product cant be null and must be higher than 0.";
//        Product product = new Product(4,"Mesa",null);
//
//        //when
//        InvalidProductException actualException = assertThrows(
//                InvalidProductException.class,
//                () -> service.save(product)
//        );
//
//        //then
//        Assertions.assertEquals(expected,actualException.getMessage());
//    }
//
//    @Test
//    void save_failToCreateProductWithPriceEqualsZero_returnInvalidProductException() {
//        //given
//        String expected = "Price of the product cant be null and must be higher than 0.";
//        Product product = new Product(0,"Mesa",0.0);
//
//        //when
//        InvalidProductException actualException = assertThrows(
//                InvalidProductException.class,
//                () -> service.save(product)
//        );
//
//        //then
//        Assertions.assertEquals(expected,actualException.getMessage());
//    }
//
//    @Test
//    void save_failToCreateProductWithLowerThanZero_returnInvalidProductException() {
//        //given
//        String expected = "Price of the product cant be null and must be higher than 0.";
//        Product product = new Product(-4,"Mesa",null);
//
//        //when
//        InvalidProductException actualException = assertThrows(
//                InvalidProductException.class,
//                () -> service.save(product)
//        );
//
//        //then
//        Assertions.assertEquals(expected,actualException.getMessage());
//    }
//
//    @Test
//    void save_failToCreateExistingProduct_returnProductAlreadyExistsException() {
//        //given
//        String expected = "Product already exists.";
//
//        //when
//        ProductAlreadyExistsException actualException = assertThrows(
//                ProductAlreadyExistsException.class,
//                () -> service.save(service.findProductById(1))
//        );
//
//        //then
//        Assertions.assertEquals(expected,actualException.getMessage());
//    }
//
//    @Test
//    void save_createProductsWithAutoincrementId_returnProductCreated() throws Exception {
//        //given
//        Integer idProduct = 4;
//        Product expected = new Product(idProduct,"Mesa",30.50);
//        Product product2 = new Product(null,"Mesa",30.50);
//
//        //when
//        Product actual = service.save(product2);
//
//        //then
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    void updateProduct_updateProduct_returnProductUpdated() throws Exception {
//        //given
//        Integer idProduct = 3;
//        Product expected = new Product(idProduct,"Mesa",30.50);
//
//        //when
//        Product actual = service.updateProduct(idProduct,expected);
//
//        //then
//        Assertions.assertEquals(expected,actual);
//
//    }
//
//    @Test
//    void deleteProduct_deleteProduct_returnTrue() throws Exception {
//        //given
//        Integer idProduct = 3;
//
//        //when
//        service.deleteProduct(idProduct);
//
//        //then
//        Assertions.assertThrows(ProductNotFoundException.class, () -> service.findProductById(idProduct));
//
//    }
//
//    private List<Product> fillProducts(){
//            List<Product> productList = new ArrayList<>();
//            productList.add(new Product(1,"Lavarropas",250.99));
//            productList.add(new Product(2,"Televisor",480.75));
//            productList.add(new Product(3,"Telefono móvil",135.99));
//            return productList;
//    }
}
