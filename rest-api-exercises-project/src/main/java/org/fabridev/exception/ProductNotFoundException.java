package org.fabridev.exception;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(){
        super("Product with this ID not found.");
    }
}
