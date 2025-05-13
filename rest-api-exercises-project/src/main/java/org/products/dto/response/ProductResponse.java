package org.products.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(description = "Response to products requests made")
public class ProductResponse {

    @Schema(description = "ID of the product", example = "2")
    private Long id;

    @Schema(description = "Name of the product", example = "television")
    private String name;

    @Schema(description = "Price of the product", example = "480.75")
    private Double price;

    @Schema(description = "Stock of the product", example = "38")
    private Integer stock;

    @Schema(description = "Id of the Type of product", example = "1")
    private Long productTypeId;

    @Schema(description = "Type of product", example = "TECHNOLOGICAL")
    private String productTypeName;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, Double price, Integer stock, Long productTypeId,String productTypeName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.productTypeId = productTypeId;
        this.productTypeName = productTypeName;
    }


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

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductResponse that = (ProductResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(stock, that.stock) && Objects.equals(productTypeId, that.productTypeId) && Objects.equals(productTypeName, that.productTypeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, stock, productTypeId, productTypeName);
    }
}
