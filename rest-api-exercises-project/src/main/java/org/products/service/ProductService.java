package org.products.service;

import org.products.api.userClient.UserClient;
import org.products.dto.request.ProductRequest;
import org.products.dto.response.UserResponse;
import org.products.exception.ProductNotFoundException;
import org.products.exception.ResourceNotFoundException;
import org.products.model.Product;
import org.products.model.ProductType;
import org.products.repository.ProductRepository;
import org.products.repository.ProductTypeRepository;
import org.products.dto.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductTypeRepository productTypeRepository;

    private final UserClient userClient;

    public ProductService(ProductRepository productRepository, ProductTypeRepository productTypeRepository, UserClient userClient) {
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
        this.userClient = userClient;
    }

    public ProductResponse getProductById(Long id) throws ProductNotFoundException{
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        return convertToProductResponse(product);
    }

    public List<ProductResponse> findAll(){
        return productRepository.findAll()
                .stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse createProduct(ProductRequest productRequest) {

        ProductType type = findTypeById(productRequest.getProductTypeId());

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
                .orElseThrow(ProductNotFoundException::new);

        ProductType type = findTypeById(productRequest.getProductTypeId());

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

    public Integer getStock(Long id) throws ProductNotFoundException {
        return findProduct(id).getStock();
    }

    public void updatedStock(Long id, Long userId, Integer newStock) throws ProductNotFoundException {

        if(id == null || userId == null || newStock == null) throw new IllegalArgumentException("id, userId or Stock quantity cannot be null");

        if (userClient.getUserById(userId) == null) throw new ProductNotFoundException();

        if (!userClient.getUserById(userId).getRole().equals("ADMIN")) throw new ResourceNotFoundException("Forbidden access, only admin");

        Product product = findProduct(id);
        product.setStock(newStock + product.getStock());
        productRepository.save(product);
    }

    public void decreaseStock(Long id, Integer quantity) throws ResourceNotFoundException, ProductNotFoundException {
        Product product = findProduct(id);

        if(product.getStock() < quantity) throw new ResourceNotFoundException("Not enough stock");

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

    }
    
    private Product findProduct(Long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }



    private ProductResponse convertToProductResponse(Product product) {

        if (product == null){
            return null;
        }
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStock(), product.getProductType().getId(),product.getProductType().getName());
    }

    private ProductType findTypeById(Long id){
        return productTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product type not found."));
    }

}