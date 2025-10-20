package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "restaurants", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"district", "street", "house"})
})

@NoArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String iconClass;

    private Integer averageCheck;

    @Enumerated(EnumType.STRING)
    private RestaurantType type;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String house;

    @Column(nullable = false)
    private String locationCode;

    public Restaurant(String name, String description, String iconClass, Integer averageCheck, RestaurantType type, String district, String street, String house) {
        this.name = name;
        this.description = description;
        this.iconClass = iconClass;
        this.averageCheck = averageCheck;
        this.type = type;
        this.district = district;
        this.street = street;
        this.house = house;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getIconClass() {return iconClass;}
    public void setIconClass(String iconClass) {this.iconClass = iconClass;}
    public Integer getAverageCheck() {return averageCheck;}
    public void setAverageCheck(Integer averageCheck) {this.averageCheck = averageCheck;}
    public RestaurantType getType() {return type;}
    public void setType(RestaurantType type) {this.type = type;}
    public String getDistrict() {return district;}
    public void setDistrict(String district) {this.district = district;}
    public String getStreet() {return street;}
    public void setStreet(String street) {this.street = street;}
    public String getHouse() {return house;}
    public void setHouse(String house) {this.house = house;}
    public String getLocationCode() {return locationCode;}
    public void setLocationCode(String locationCode) {this.locationCode = locationCode;}


    // Переопределяем equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(district.toLowerCase(), that.district.toLowerCase()) &&
                Objects.equals(street.toLowerCase(), that.street.toLowerCase()) &&
                Objects.equals(house, that.house);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, district.toLowerCase(), street.toLowerCase(), house);
    }
}