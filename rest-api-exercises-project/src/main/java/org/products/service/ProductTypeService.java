package org.products.service;

import org.products.api.userClient.UserClient;
import org.products.dto.request.ProductTypeRequest;
import org.products.dto.response.ProductTypeResponse;
import org.products.exception.ProductTypeNotFoundException;
import org.products.exception.ProductTypeRelatedException;
import org.products.model.ProductType;
import org.products.repository.ProductRepository;
import org.products.repository.ProductTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductTypeService {


    private final ProductTypeRepository productTypeRepository;

    private final ProductRepository productRepository;

    private final UserClient userClient;

    public ProductTypeService(ProductTypeRepository productTypeRepository, ProductRepository productRepository, UserClient userClient) {
        this.productTypeRepository = productTypeRepository;
        this.productRepository = productRepository;
        this.userClient = userClient;
    }

    public ProductTypeResponse findProductTypeById(Long id) throws ProductTypeNotFoundException {
        return convertProductTypeToProductTypeResponse(productTypeRepository.findById(id)
                .orElseThrow(ProductTypeNotFoundException::new));
    }

    public List<ProductTypeResponse> findAll(){
        return productTypeRepository.findAll()
                .stream()
                .map(this::convertProductTypeToProductTypeResponse)
                .collect(Collectors.toList());
    }

    public ProductTypeResponse createProductType(ProductTypeRequest productTypeRequest, String token) throws IllegalArgumentException{

        Optional<ProductType> existingProductType = productTypeRepository.findByName(productTypeRequest.getName());

        if (existingProductType.isPresent()){
            throw new IllegalArgumentException("This product type already exists.");
        }

        ProductType productType = new ProductType();
        productType.setName(productTypeRequest.getName());
        ProductType productCreated = productTypeRepository.save(productType);
        return convertProductTypeToProductTypeResponse(productCreated);
    }

    public ProductTypeResponse updateProductType(Long id, ProductTypeRequest productTypeRequest) throws ProductTypeNotFoundException, IllegalArgumentException {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(()-> new ProductTypeNotFoundException());

        productTypeRepository.findByName(productTypeRequest.getName()).ifPresent(
                productType1 -> {
                    if(!productType1.getId().equals(id)) throw new IllegalArgumentException("There's another type with this name.");
                }
        );

        productType.setName(productTypeRequest.getName());
        ProductType productUpdated = productTypeRepository.save(productType);
        return convertProductTypeToProductTypeResponse(productUpdated);
    }

    public void deleteProductType(Long id) throws ProductTypeNotFoundException, ProductTypeRelatedException {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new ProductTypeNotFoundException());

        if(productRepository.existsByProductType(productType)){
            throw new ProductTypeRelatedException();
        }

        productTypeRepository.deleteById(id);
    }

    private ProductTypeResponse convertProductTypeToProductTypeResponse(ProductType productType){
        return new ProductTypeResponse(productType.getId(), productType.getName());
    }

}
