package org.products.exception;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(){
        super("Product with this ID not found.");
    }
}
