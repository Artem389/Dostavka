package com.example.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    public OrderItem(Long id, Product product, int quantity, BigDecimal price, Order order) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.order = order;
    }

    public OrderItem() {}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Product getProduct() {return product;}
    public void setProduct(Product product) {this.product = product;}
    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}
    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}
    public Order getOrder() {return order;}
    public void setOrder(Order order) {this.order = order;}

}