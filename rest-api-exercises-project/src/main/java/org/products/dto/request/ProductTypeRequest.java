package org.products.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.products.model.Product;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Schema(description = "DTO request to create and update a type of product")
public class ProductTypeRequest {

    @NotBlank(message = "Name cant be empty.")
    private String name;

    public ProductTypeRequest() {
    }

    public ProductTypeRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductTypeRequest that = (ProductTypeRequest) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
