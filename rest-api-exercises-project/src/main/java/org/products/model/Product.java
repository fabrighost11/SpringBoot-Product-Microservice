package org.products.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

}
