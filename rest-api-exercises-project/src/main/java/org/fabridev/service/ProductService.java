package org.fabridev.service;

import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.exception.ProductNullFieldFoundException;
import org.fabridev.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final List<Product> productList = new ArrayList<>();

    public ProductService() {
        productList.add(new Product(1,"Lavarropas",250.99));
        productList.add(new Product(2,"Televisor",480.75));
        productList.add(new Product(3,"Telefono móvil",135.99));
    }

    public Optional<Product> findProductById(Integer id) {
        return productList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Product save(Product product){

        productList.add(product);
        System.out.println( "Has guardado correctamente a " + product.getName());
        return product;
    }

    public Product updateProduct(Integer id, Product productUpdated){

        Product product = findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if(productUpdated.getName() != null && productUpdated.getPrice() != null){

            product.setName(productUpdated.getName());
            product.setPrice(productUpdated.getPrice());

        }else{

            throw new ProductNullFieldFoundException();

        }

        return product;

    }

//    public Product partialUpdateProduct(Integer id, Product productPartialUpdate){
//
//        Product product = findProductById(id)
//                .orElseThrow(() -> new ProductNotFoundException(id));
//        if(productPartialUpdate.getName() != null){
//            product.setName(productPartialUpdate.getName());
//        }
//        if (productPartialUpdate.getPrice() != null){
//            product.setPrice(productPartialUpdate.getPrice());
//        }
//
//        return product;
//    }

    public void deleteProduct(Integer id){
        Product product = findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productList.remove(product);
    }
}