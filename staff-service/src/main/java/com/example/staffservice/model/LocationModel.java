package com.example.staffservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class LocationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String district; // Район

    @Column(nullable = false)
    private String street; // Улица

    @Column(nullable = false)
    private String house; // Дом

    @Column(name = "location_code")
    private String locationCode;

    public LocationModel() {}

    public LocationModel(String district, String street, String house) {
        this.district = district;
        this.street = street;
        this.house = house;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getHouse() { return house; }
    public void setHouse(String house) { this.house = house; }

    public String getLocationCode() { return locationCode; }
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }

    @Override
    public String toString() {
        return "Район: " + district + ", Улица: " + street + ", Дом: " + house;
    }
}