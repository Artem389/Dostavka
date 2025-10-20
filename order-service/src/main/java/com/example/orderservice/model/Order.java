package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String district; // Новый разделённый адрес: район

    @Column(nullable = false)
    private String street;   // Новый разделённый адрес: улица

    @Column(nullable = false)
    private String house;    // Новый разделённый адрес: дом

    @Column(nullable = false)
    private String locationCode; // Шестизначный код локации

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;  // Присвоенный ресторан

    @Column(name = "courier_id")
    private Long courierId;  // Присвоенный курьер

    @Column(nullable = false)
    private String customerPhone;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderItem> items;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal totalAmount;

    private LocalDateTime createdAt;

    public Order(Long id, String customerName, String district, String street, String house, String locationCode, Restaurant restaurant, Long courierId, String customerPhone, LocalDateTime createdAt) {
        this.id = id;
        this.customerName = customerName;
        this.district = district;
        this.street = street;
        this.house = house;
        this.locationCode = locationCode;
        this.restaurant = restaurant;
        this.courierId = courierId;
        this.customerPhone = customerPhone;
        this.createdAt = createdAt;
    }

    public Order() {

    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getCustomerName() {return customerName;}
    public void setCustomerName(String customerName) {this.customerName = customerName;}
    public String getDistrict() {return district;}
    public void setDistrict(String district) {this.district = district;}
    public String getStreet() {return street;}
    public void setStreet(String street) {this.street = street;}
    public String getHouse() {return house;}
    public void setHouse(String house) {this.house = house;}
    public String getLocationCode() {return locationCode;}
    public void setLocationCode(String locationCode) {this.locationCode = locationCode;}
    public Restaurant getRestaurant() {return restaurant;}
    public void setRestaurant(Restaurant restaurant) {this.restaurant = restaurant;}
    public Long getCourierId() {return courierId;}
    public void setCourierId(Long id) {this.courierId = id;}
    public String getCustomerPhone() {return customerPhone;}
    public void setCustomerPhone(String customerPhone) {this.customerPhone = customerPhone;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
    public List<OrderItem> getItems() {return items;}
    public void setItems(List<OrderItem> items) {this.items = items;}
    public OrderStatus getStatus() {return status;}
    public void setStatus(OrderStatus status) {this.status = status;}
    public BigDecimal getTotalAmount() {return totalAmount;}
    public void setTotalAmount(BigDecimal totalAmount) {this.totalAmount = totalAmount;}



    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        status = OrderStatus.PENDING;
    }
}