package org.fabridev.exception;

public class ProductNullFieldFoundException extends RuntimeException {
    public ProductNullFieldFoundException() {
        super("No se pueden enviar campos nulos.");
    }
}
