package service;

import org.fabridev.controller.ProductController;
import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.model.Product;
import org.fabridev.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductService service;

    @InjectMocks
    private ProductController controller;

    @BeforeEach
    void setUp() {
        controller = new ProductController(service);
    }

    //[methodUnderTest]_[Scenario]_[ExpectedResult]
    @Test
    void getProductById_findProductById_returnProductData(){
        //given
        Integer idProduct = 1;
        Product expected = new Product(idProduct,"Lavarropas",250.99);
        Mockito.when(service.findProductById(idProduct)).thenReturn(Optional.of(expected));
        //when
        Product actual = controller.getProductById(idProduct);

        //then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void getProductById_findProductById_returnNotEqualsId(){
        //given
        Integer idProduct = 1;
        Integer idProduct2 = 2;

        Product expected = new Product(idProduct,"Lavarropas",250.99);

        Product actualProduct = new Product(idProduct2,"Televisor",480.75);
        Mockito.when(service.findProductById(idProduct2)).thenReturn(Optional.of(actualProduct));

        //when
        Product actual = controller.getProductById(idProduct2);

        //then
        Assertions.assertNotEquals(expected.getId(),actual.getId());
        Assertions.assertNotEquals(expected.getName(),actual.getName());
        Assertions.assertNotEquals(expected.getPrice(),actual.getPrice());
    }

    @Test
    void getProductById_productByIdNotFound_returnNull(){
        //given
        int idNonExistent = 99;
        Mockito.when(service.findProductById(idNonExistent)).thenReturn(Optional.empty());
        //when
        
        //then
        Assertions.assertThrows(ProductNotFoundException.class,() -> { controller.getProductById(idNonExistent); });
    }

}
