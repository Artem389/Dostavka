package com.example.distributorservice.repository;

import com.example.distributorservice.model.AssignedOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignedOrderRepository extends JpaRepository<AssignedOrder, Long> {
    List<AssignedOrder> findByCourierId(Long courierId);
}