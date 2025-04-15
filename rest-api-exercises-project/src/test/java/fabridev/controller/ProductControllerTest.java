package fabridev.controller;

import org.fabridev.controller.ProductController;
import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductControllerTest {

    private ProductController controller;

    @BeforeEach
    void setUp(){
        controller = new ProductController();
    }

    //[methodUnderTest]_[Scenario]_[ExpectedResult]
    @Test
    void getProductById_findProductById_returnProductData(){
        //given
        Product expected = new Product(1,"Lavarropas",250.99);

        //when
        Product actual = controller.getProductById(1);

        //then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void getProductById_dontFindIdMatch_returnNotEqual(){
        //given
        Integer idProduct1 = 1;
        Integer idProduct2 = 2;
        Product expected = new Product(idProduct1,"Lavarropas",250.99);

        //when
        Product actual = controller.getProductById(idProduct2);

        //then
        Assertions.assertNotEquals(expected,actual);
    }

    @Test
    void getProductById_productIdNotFound_returnException(){
        //given
        int idNonExistent = 99;
        String expected = "Product with ID: 99 not found.";

        //when
        ProductNotFoundException actualException = assertThrows(
                ProductNotFoundException.class,
                () -> controller.getProductById(idNonExistent)
        );

        //then
        Assertions.assertEquals(expected,actualException.getMessage());
    }
}
