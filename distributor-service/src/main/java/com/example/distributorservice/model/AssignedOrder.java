package com.example.distributorservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "assigned_orders")
public class AssignedOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "courier_id")
    private Long courierId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_address_code")
    private String orderAddressCode;

    public AssignedOrder() {}

    public AssignedOrder(Long courierId, Long orderId, String orderAddressCode) {
        this.courierId = courierId;
        this.orderId = orderId;
        this.orderAddressCode = orderAddressCode;
    }

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourierId() { return courierId; }
    public void setCourierId(Long courierId) { this.courierId = courierId; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getOrderAddressCode() { return orderAddressCode; }
    public void setOrderAddressCode(String orderAddressCode) { this.orderAddressCode = orderAddressCode; }
}