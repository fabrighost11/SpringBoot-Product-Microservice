package org.fabridev.service;

import org.fabridev.controller.ProductController;
import org.fabridev.exception.InvalidProductException;
import org.fabridev.exception.ProductAlreadyExistsException;
import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductServiceTest {

    private ProductService service;

    private Product product;

    @BeforeEach
    void setUp() {
        service = new ProductService();
    }

    @Test
    void findProductById_findProductById_returnProductData() throws Exception {
        //given
        Product expected = new Product(1, "Lavarropas", 250.99);

        //when
        Product actual = service.findProductById(1);

        //then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findProductById_productIdNotFound_returnException() {
        //given
        int idNonExistent = 99;
        String expected = "Product with this ID not found.";

        //when
        ProductNotFoundException actualException = assertThrows(
                ProductNotFoundException.class,
                () -> service.findProductById(idNonExistent)
        );

        //then
        Assertions.assertEquals(expected,actualException.getMessage());
    }

    @Test
    void findAll_findAllProductsInTheList_returnProductList(){
        //given
        List<Product> expected = fillProducts();

        //when
        List<Product> actual = service.findAll();

        //then
        Assertions.assertEquals(expected,actual);

    }

    @Test
    void save_createProducts_returnProductCreated() throws Exception {
        //given
        Integer idProduct = 4;
        Product expected = new Product(idProduct,"Mesa",30.50);
        Product product2 = new Product(4,"Mesa",30.50);

        //when
        Product actual = service.save(product2);

        //then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void save_failToCreateProductWithNoName_returnInvalidProductException() {
        //given
        String expected = "Name of the product cant be empty.";
        Product product = new Product(4,"",30.50);

        //when
        InvalidProductException actualException = assertThrows(
                InvalidProductException.class,
                () -> service.save(product)
        );

        //then
        Assertions.assertEquals(expected,actualException.getMessage());
    }

    @Test
    void save_failToCreateProductWithNullName_returnInvalidProductException() {
        //given
        String expected = "Name of the product cant be empty.";
        Product product = new Product(4,null,30.50);

        //when
        InvalidProductException actualException = assertThrows(
                InvalidProductException.class,
                () -> service.save(product)
        );

        //then
        Assertions.assertEquals(expected,actualException.getMessage());
    }

    @Test
    void save_failToCreateProductWithNullPrice_returnInvalidProductException() {
        //given
        String expected = "Price of the product cant be null and must be higher than 0.";
        Product product = new Product(4,"Mesa",null);

        //when
        InvalidProductException actualException = assertThrows(
                InvalidProductException.class,
                () -> service.save(product)
        );

        //then
        Assertions.assertEquals(expected,actualException.getMessage());
    }

    @Test
    void save_failToCreateProductWithPriceEqualsZero_returnInvalidProductException() {
        //given
        String expected = "Price of the product cant be null and must be higher than 0.";
        Product product = new Product(0,"Mesa",0.0);

        //when
        InvalidProductException actualException = assertThrows(
                InvalidProductException.class,
                () -> service.save(product)
        );

        //then
        Assertions.assertEquals(expected,actualException.getMessage());
    }

    @Test
    void save_failToCreateProductWithLowerThanZero_returnInvalidProductException() {
        //given
        String expected = "Price of the product cant be null and must be higher than 0.";
        Product product = new Product(-4,"Mesa",null);

        //when
        InvalidProductException actualException = assertThrows(
                InvalidProductException.class,
                () -> service.save(product)
        );

        //then
        Assertions.assertEquals(expected,actualException.getMessage());
    }

    @Test
    void save_failToCreateExistingProduct_returnProductAlreadyExistsException() {
        //given
        String expected = "Product already exists.";

        //when
        ProductAlreadyExistsException actualException = assertThrows(
                ProductAlreadyExistsException.class,
                () -> service.save(service.findProductById(1))
        );

        //then
        Assertions.assertEquals(expected,actualException.getMessage());
    }

    @Test
    void save_createProductsWithAutoincrementId_returnProductCreated() throws Exception {
        //given
        Integer idProduct = 4;
        Product expected = new Product(idProduct,"Mesa",30.50);
        Product product2 = new Product(null,"Mesa",30.50);

        //when
        Product actual = service.save(product2);

        //then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void updateProduct_updateProduct_returnProductUpdated() throws Exception {
        //given
        Integer idProduct = 3;
        Product expected = new Product(idProduct,"Mesa",30.50);

        //when
        Product actual = service.updateProduct(idProduct,expected);

        //then
        Assertions.assertEquals(expected,actual);

    }

    @Test
    void deleteProduct_deleteProduct_returnTrue() throws Exception {
        //given
        Integer idProduct = 3;

        //when
        service.deleteProduct(idProduct);

        //then
        Assertions.assertThrows(ProductNotFoundException.class, () -> service.findProductById(idProduct));

    }

    private List<Product> fillProducts(){
            List<Product> productList = new ArrayList<>();
            productList.add(new Product(1,"Lavarropas",250.99));
            productList.add(new Product(2,"Televisor",480.75));
            productList.add(new Product(3,"Telefono móvil",135.99));
            return productList;
    }
}
