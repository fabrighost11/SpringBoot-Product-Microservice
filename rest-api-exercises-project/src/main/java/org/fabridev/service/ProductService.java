package org.fabridev.service;

import org.fabridev.exception.ProductAlreadyExistsException;
import org.fabridev.exception.ProductNotFoundException;
import org.fabridev.exception.InvalidProductException;
import org.fabridev.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final List<Product> productList = new ArrayList<>();

    private int incrementId;

    public ProductService() {
        productList.add(new Product(1,"Lavarropas",250.99));
        productList.add(new Product(2,"Televisor",480.75));
        productList.add(new Product(3,"Telefono móvil",135.99));

        this.incrementId = productList.stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(0) +1;
    }

    public Product findProductById(Integer id) throws ProductNotFoundException{
        return productList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException());
    }

    public Product save(Product product) throws InvalidProductException, ProductAlreadyExistsException {

        checkProductIsValid(product);

        if(productExists(product.getId())){
            throw  new ProductAlreadyExistsException("Product already exists.");
        }

        if(product.getId() == null){
            product.setId(incrementId++);
        }

        productList.add(product);
        return product;

    }

    public Product updateProduct(Integer id, Product newProduct) throws ProductNotFoundException, InvalidProductException{

        Product originalProduct = findProductById(id);

        checkProductIsValid(newProduct);
        originalProduct.setName(newProduct.getName());
        originalProduct.setPrice(newProduct.getPrice());

        return originalProduct;

    }

    public void deleteProduct(Integer id) throws ProductNotFoundException, InvalidProductException{

        Product product = findProductById(id);
        productList.remove(product);

    }

    private void checkProductIsValid(Product product){
        if (product.getName() == null || product.getName().trim().isEmpty()){
            throw new InvalidProductException("Name of the product cant be empty.");
        }
        if(product.getPrice() == null || product.getPrice() <= 0){
            throw new InvalidProductException("Price of the product cant be null and must be higher than 0.");
        }
    }

    public boolean productExists(Integer id){

        if(id == null){
            return false;
        }

        return productList.stream()
                .anyMatch(p -> id.equals(p.getId()));
    }

}