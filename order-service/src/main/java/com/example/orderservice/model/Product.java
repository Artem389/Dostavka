package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "products")
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    private Integer calories;
    private Double proteins;
    private Double fats;
    private Double carbohydrates;

    public Product(String name, String description, BigDecimal price, Integer calories, Double proteins, Double fats, Double carbohydrates, Restaurant restaurant) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.restaurant = restaurant;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}
    public Integer getCalories() {return calories;}
    public void setCalories(Integer calories) {this.calories = calories;}
    public Double getProteins() {return proteins;}
    public void setProteins(Double proteins) {this.proteins = proteins;}
    public Double getFats() {return fats;}
    public void setFats(Double fats) {this.fats = fats;}
    public Double getCarbohydrates() {return carbohydrates;}
    public void setCarbohydrates(Double carbohydrates) {this.carbohydrates = this.carbohydrates;}


    // Переопределяем equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}