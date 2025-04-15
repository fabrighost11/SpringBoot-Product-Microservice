package org.fabridev.model;


import java.util.Objects;

public class Product {

    private Integer id;

    private String name;

    private double price;

    public Product() {
    }

    public Product(Integer id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString(){
        return "Product{id="+ id + ", name=" + name + " price=" + price + "}";
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product that = (Product) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id,name,price);
    }
}
