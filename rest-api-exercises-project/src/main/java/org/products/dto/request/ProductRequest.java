package org.products.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;


import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;


@Schema(description = "DTO request to create and update a product")
public class ProductRequest {

    @NotBlank(message = "Name of the product cant be empty.")
    @Schema(description = "Name of the product", example = "Washing machine")
    private String name;

    @NotNull(message = "Price of the product cant be null.")
    @Positive(message = "Price of the product cant be lower than zero.")
    @Schema(description = "Price of the product", example = "250.99")
    private Double price;

    @NotNull(message = "Stock cant be null.")
    @PositiveOrZero(message = "Stock must be zero or higher.")
    @Schema(description = "Stock of the product", example = "22")
    private Integer stock;

    @NotNull(message = "Type of product cant be null.")
    @Schema(description = "Type of product", example = "HOME_APPLIANCE")
    private Long type;

    public ProductRequest() {
    }

    public ProductRequest(String name, Double price, Integer stock, Long type) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof ProductRequest)) return false;
        ProductRequest that = (ProductRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(price, that.price)
                && Objects.equals(stock, that.stock) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode(){
        return Objects.hash(name,price,stock,type);
    }
}
