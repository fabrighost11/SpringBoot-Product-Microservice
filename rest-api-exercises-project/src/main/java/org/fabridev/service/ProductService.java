package org.fabridev.service;

import org.fabridev.dto.ProductDto;
import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.exception.InvalidProductException;
import org.fabridev.model.Product;
import org.fabridev.repository.ProductRepository;
import org.fabridev.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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

    public ProductResponse createProduct(ProductDto productDto) {

//        checkProductIsValid(productDto);

        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setType(productDto.getType());

        Product savedProduct = productRepository.save(product);
        return convertToProductResponse(savedProduct);
    }

    public ProductResponse updateProduct(Long id, ProductDto productDto) throws ProductNotFoundException {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->new ProductNotFoundException());

//        checkProductIsValid(productDto);

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setType(productDto.getType());

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
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStock(), product.getType());
    }

}