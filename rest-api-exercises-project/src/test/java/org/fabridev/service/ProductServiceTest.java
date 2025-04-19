package org.fabridev.service;

import org.fabridev.controller.ProductController;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductServiceTest {

    private ProductService service;

    @BeforeEach
    void setUp() {
        service = new ProductService();
    }

    @Test
    void getProductById_findProductById_returnProductData() throws Exception {
        //given
        Product expected = new Product(1, "Lavarropas", 250.99);

        //when
        Product actual = service.findProductById(1);

        //then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void getProductById_dontFindIdMatch_returnNotEqual() throws Exception {
        //given
        Integer idProduct1 = 1;
        Integer idProduct2 = 2;
        Product expected = new Product(idProduct1,"Lavarropas",250.99);

        //when
        Product actual = service.findProductById(idProduct2);

        //then
        Assertions.assertNotEquals(expected,actual);
    }

    @Test
    void getProductById_productIdNotFound_returnException() {
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


}
