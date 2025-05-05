package org.products.service;

import org.products.dto.request.ProductRequest;
import org.products.exception.ProductNotFoundException;
import org.products.model.Product;
import org.products.model.ProductType;
import org.products.repository.ProductRepository;
import org.products.repository.ProductTypeRepository;
import org.products.dto.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final ProductTypeRepository productTypeRepository;

    public ProductService(ProductRepository productRepository, ProductTypeRepository productTypeRepository) {
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
    }

    public ProductResponse findProductById(Long id) throws ProductNotFoundException{
        Product product = productRepository.findById(id)
                .orElseThrow(() ->new ProductNotFoundException());
        return convertToProductResponse(product);
    }

    public List<ProductResponse> findAll(){
        return productRepository.findAll()
                .stream()
                .map(product -> convertToProductResponse(product))
                .collect(Collectors.toList());
    }

    public ProductResponse createProduct(ProductRequest productRequest) {

        ProductType type = findTypeById(productRequest.getType());

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setProductType(type);

        Product savedProduct = productRepository.save(product);
        return convertToProductResponse(savedProduct);
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) throws ProductNotFoundException {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->new ProductNotFoundException());

        ProductType type = findTypeById(productRequest.getType());

        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setProductType(type);

        Product updatedProduct = productRepository.save(product);

        return convertToProductResponse(updatedProduct);


    }

    public void deleteProduct(Long id) throws ProductNotFoundException{

        if(!productRepository.existsById(id)){
            throw new ProductNotFoundException();
        }

        productRepository.deleteById(id);

    }

    private ProductResponse convertToProductResponse(Product product) {

        if (product == null){
            return null;
        }
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStock(), product.getProductType().getName());
    }

    private ProductType findTypeById(Long id){
        return productTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product type not found."));
    }

}