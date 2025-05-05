package org.products.exception;

public class ProductTypeNotFoundException extends RuntimeException {
    public ProductTypeNotFoundException() {
        super("Type of product not found.");
    }
}
