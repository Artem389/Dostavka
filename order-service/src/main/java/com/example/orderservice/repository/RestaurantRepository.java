package com.example.orderservice.repository;

import com.example.orderservice.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.district) = LOWER(?1) AND LOWER(r.street) = LOWER(?2) AND r.house = ?3")
    Optional<Restaurant> findByDistrictAndStreetAndHouseIgnoreCase(String district, String street, String house);
}