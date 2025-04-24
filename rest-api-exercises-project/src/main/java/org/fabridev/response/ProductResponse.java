package org.fabridev.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.fabridev.model.Product;
import org.fabridev.model.Type;

import java.util.Objects;

@Schema(description = "Response to requests made")
public class ProductResponse {

    @Schema(description = "ID of the product", example = "2")
    private Long id;

    @Schema(description = "Name of the product", example = "television")
    private String name;

    @Schema(description = "Price of the product", example = "480.75")
    private Double price;

    @Schema(description = "Stock of the product", example = "38")
    private Integer stock;

    @Schema(description = "Type of product", example = "TECHNOLOGICAL")
    private Type type;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, Double price, Integer stock, Type type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.type = type;
    }

//    public ProductResponse(Product product) {
//        if (product != null) {
//            this.id = product.getId();
//            this.name = product.getName();
//            this.price = product.getPrice();
//            this.stock = product.getStock();
//            this.type = product.getType();
//        }
//    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof ProductResponse)) return false;
        ProductResponse that = (ProductResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price)
                && Objects.equals(stock, that.stock) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id,name,price,stock,type);
    }
}
