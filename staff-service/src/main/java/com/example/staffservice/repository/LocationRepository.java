package com.example.staffservice.repository;

import com.example.staffservice.model.LocationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationModel, Long> {
    @Query("SELECT l FROM LocationModel l WHERE LOWER(l.district) = LOWER(?1) AND LOWER(l.street) = LOWER(?2) AND LOWER(l.house) = LOWER(?3)")
    Optional<LocationModel> findByDistrictAndStreetAndHouseIgnoreCase(String district, String street, String house);
}