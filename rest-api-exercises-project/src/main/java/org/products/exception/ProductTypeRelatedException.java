package org.products.exception;

public class ProductTypeRelatedException extends RuntimeException {
    public ProductTypeRelatedException() {
        super("Cant delete this TYPE: there is products related to it.");
    }
}
