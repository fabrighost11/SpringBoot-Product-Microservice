package org.products.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.products.api.userClient.UserClient;
import org.products.dto.request.ProductTypeRequest;
import org.products.dto.response.ProductTypeResponse;
import org.products.dto.response.UserResponse;
import org.products.exception.ProductTypeNotFoundException;
import org.products.exception.ProductTypeRelatedException;
import org.products.exception.ResourceNotFoundException;
import org.products.model.ProductType;
import org.products.repository.ProductRepository;
import org.products.repository.ProductTypeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductTypeServiceTest {

    @Mock
    private ProductTypeRepository productTypeRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    ProductTypeService service;

    private ProductType productType;
    private ProductTypeRequest productTypeRequest;
    private ProductTypeResponse productTypeResponse;
    private UserResponse userResponse;

    @BeforeEach
    void setUp(){

        productType = new ProductType(1L,"HOME_APPLIANCE");
        productTypeRequest = new ProductTypeRequest("HOME_APPLIANCE");
        productTypeResponse = new ProductTypeResponse(1L,"HOME_APPLIANCE");

        userResponse = new UserResponse();
        userResponse.setId(1L); userResponse.setName("User Name"); userResponse.setEmail("User@Email.com"); userResponse.setRole("ADMIN");
    }

    @Test
    void findProductTypeById_findTypeSuccessfully_returnTypeResponse(){
        //given
        ProductTypeResponse expected = productTypeResponse;
        Long productId = 1L;
        when(productTypeRepository.findById(productId)).thenReturn(Optional.of(productType));

        //when
        ProductTypeResponse actual = service.findProductTypeById(productId);

        //then
        Assertions.assertEquals(expected,actual);

    }

    @Test
    void findProductTypeById_idNotFound_returnNotFoundException(){
        //given
        Long nonExistingId = 99L;
        String expected = "Type of product not found.";
        when(productTypeRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        //when
        ProductTypeNotFoundException actualException = assertThrows(
                ProductTypeNotFoundException.class,
                () -> service.findProductTypeById(nonExistingId)
        );

        //then
        Assertions.assertEquals(expected,actualException.getMessage());
    }

    @Test
    void findAll_findAllProductsTypeInTheList_returnProductTypeList(){
        //given
        when(productTypeRepository.findAll()).thenReturn(List.of(productType));

        //when
        List<ProductTypeResponse> actual = service.findAll();

        //then
        Assertions.assertEquals(1,actual.size());
        Assertions.assertEquals(1,actual.get(0).getId());
        Assertions.assertEquals("HOME_APPLIANCE",actual.get(0).getName());
    }

    @Test
    void createProductType_createTypeSuccessfully_returnTypeCreated(){
        //given
        Long userId = 1L;

        when(userClient.getUserById(userId)).thenReturn(userResponse);
        when(productTypeRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(productTypeRepository.save(any(ProductType.class))).thenReturn(productType);

        //when
        ProductTypeResponse actual = service.createProductType(userId, productTypeRequest);

        //then
        Assertions.assertEquals(1,actual.getId());
        Assertions.assertEquals("HOME_APPLIANCE",actual.getName());
    }

    @Test
    void createProductType_createTypeNameExists_returnNameExistsException(){
        //given
        Long userId = 2L;
        String productTypeName = "HOME_APPLIANCE";
        String expected = "This product type already exists.";
        when(userClient.getUserById(userId)).thenReturn(userResponse);
        when(productTypeRepository.findByName(productTypeName)).thenReturn(Optional.of(productType));

        //when
        IllegalArgumentException actualException = assertThrows(
                IllegalArgumentException.class,
                () -> service.createProductType(userId, productTypeRequest)
        );

        //then
        Assertions.assertEquals(expected,actualException.getMessage());
    }

    @Test
    void createProductType_userIdNull_returnUserIdNullException(){
        Long userId = null;
        String expected = "User not found";
        when(userClient.getUserById(userId)).thenReturn(null);
        IllegalArgumentException actualException = assertThrows(
                IllegalArgumentException.class,
                () -> service.createProductType(userId, productTypeRequest)
        );

        Assertions.assertEquals(expected,actualException.getMessage());
    }

    @Test
    void createProductType_createTypeInvalidUserNotAdmin_returnForbiddenException(){
        //given
        Long userId = 2L;
        String expected = "Forbidden access, only admin";
        userResponse.setRole("DEFAULT_USER");
        when(userClient.getUserById(userId)).thenReturn(userResponse);

        //when
        IllegalArgumentException actualException = assertThrows(
                IllegalArgumentException.class,
                () -> service.createProductType(userId, productTypeRequest)
        );

        //then
        Assertions.assertEquals(expected,actualException.getMessage());
    }

    @Test
    void updateProductType_updateTypeSuccessfully_returnProductTypeResponse(){
        //given
        Long productTypeId = 1L;
        ProductTypeResponse expected = productTypeResponse;
        when(productTypeRepository.findById(productTypeId)).thenReturn(Optional.of(productType));
        when(productTypeRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(productTypeRepository.save(any(ProductType.class))).thenReturn(productType);

        //when
        ProductTypeResponse actual = service.updateProductType(productTypeId,productTypeRequest);

        //then
        Assertions.assertEquals(expected,actual);

    }

    @Test
    void updateProductType_updateTypeNameExistsAlready_returnException(){
        //given
        String expected = "There's another type with this name.";
        ProductType existingName = new ProductType(2L,"HOME_APPLIANCE");
        when(productTypeRepository.findById(productType.getId())).thenReturn(Optional.of(productType));
        when(productTypeRepository.findByName(productTypeRequest.getName())).thenReturn(Optional.of(existingName));

        //when
        IllegalArgumentException actualException = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateProductType(1L,productTypeRequest)
        );

        //then
        Assertions.assertEquals(expected,actualException.getMessage());
    }

    @Test
    void deleteById_deleteTypeSuccessfully_returnVoid(){
        //given
        Long productId = 1L;
        when(productTypeRepository.findById(productId)).thenReturn(Optional.of(productType));
        when(productRepository.existsByProductType(productType)).thenReturn(false);

        //when
        service.deleteProductType(productId);

        //then
        verify(productTypeRepository).deleteById(productId);
    }

    @Test
    void deleteById_deleteTypeIdNotFound_returnException(){
        //given
        Long productId = 99L;
        when(productTypeRepository.findById(productId)).thenReturn(Optional.empty());
        //when
        assertThrows(ProductTypeNotFoundException.class, () -> {
            service.deleteProductType(productId);
        });

        //then
        verify(productTypeRepository).findById(productId);
    }

    @Test
    void deleteById_existsProductRelatedToType_returnException(){
        //given
        Long productId = 1L;
        when(productTypeRepository.findById(productId)).thenReturn(Optional.of(productType));
        when(productRepository.existsByProductType(productType)).thenReturn(true);

        //when
        assertThrows(ProductTypeRelatedException.class, () -> {
            service.deleteProductType(productId);
        });

        //then
        verify(productTypeRepository).findById(productId);
    }

}
