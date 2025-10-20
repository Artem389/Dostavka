package com.example.staffservice.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "couriers")
public class CourierModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "current_location_id")
    private LocationModel currentLocation;

    @Column(name = "active_orders")
    private Integer activeOrders = 0;

    @Column(name = "max_capacity")
    private Integer maxCapacity = 3;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER;

    @Column(name = "total_orders")
    private Integer totalOrders = 0;

    @ElementCollection
    private Set<String> visitedStreets = new HashSet<>();

    @ElementCollection
    private Set<String> currentOrderAddresses = new HashSet<>();  // Для адресов текущих заказов

    public CourierModel() {
        this.createdAt = java.time.LocalDateTime.now().toString();
    }

    public CourierModel(String name, String phone, String email, LocationModel currentLocation, Integer maxCapacity, String password) {
        this();
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.currentLocation = currentLocation;
        this.maxCapacity = maxCapacity;
        this.password = password;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocationModel getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(LocationModel currentLocation) { this.currentLocation = currentLocation; }

    public Integer getActiveOrders() { return activeOrders; }
    public void setActiveOrders(Integer activeOrders) { this.activeOrders = activeOrders; }

    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean available) { isAvailable = available; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Integer getTotalOrders() { return totalOrders; }
    public void setTotalOrders(Integer totalOrders) { this.totalOrders = totalOrders; }

    public Set<String> getVisitedStreets() { return visitedStreets; }
    public void setVisitedStreets(Set<String> visitedStreets) { this.visitedStreets = visitedStreets; }

    public Set<String> getCurrentOrderAddresses() { return currentOrderAddresses; }
    public void setCurrentOrderAddresses(Set<String> currentOrderAddresses) { this.currentOrderAddresses = currentOrderAddresses; }
}
