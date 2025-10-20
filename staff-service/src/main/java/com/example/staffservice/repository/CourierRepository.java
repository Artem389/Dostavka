package com.example.staffservice.repository;

import com.example.staffservice.model.CourierModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourierRepository extends JpaRepository<CourierModel, Long> {
    List<CourierModel> findByIsAvailableTrue();
    List<CourierModel> findByIsActiveTrue();
    Optional<CourierModel> findByEmail(String email);
    long countByCurrentLocationId(Long currentLocationId);
    Optional<CourierModel> findByPhone(String phone);

    @Modifying
    @Query("UPDATE CourierModel c SET c.isActive = false WHERE c.id = :id")
    void deactivateCourier(@Param("id") Long id);

    @Query("SELECT COUNT(c) FROM CourierModel c WHERE c.isActive = true")
    Long countActiveCouriers();
}