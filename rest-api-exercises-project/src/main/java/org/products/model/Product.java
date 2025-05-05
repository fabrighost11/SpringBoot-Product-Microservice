package org.products.model;


import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "PRODUCTS")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private Double price;
    @Column(name = "stock")
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private ProductType productType;

    public Product() {
    }

    public Product(Long id, String name, Double price, Integer stock, ProductType productType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.productType = productType;
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

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    @Override
    public String toString(){
        return "Product{id="+ id + ", name=" + name + " price=" + price + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(stock, product.stock) && Objects.equals(productType, product.productType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, stock, productType);
    }
}
