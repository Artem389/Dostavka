package com.example.staffservice.controller;

import com.example.staffservice.model.CourierModel;
import com.example.staffservice.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/couriers")
public class CourierController {

    private final CourierService courierService;

    @Autowired
    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }

    @GetMapping
    public List<CourierModel> getAllCouriers() {
        return courierService.getAllCouriers();
    }



    @GetMapping("/{id}")
    public ResponseEntity<CourierModel> getCourierById(@PathVariable Long id) {
        return courierService.getCourierById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CourierModel createCourier(@RequestBody CourierModel couriermodel) {
        return courierService.createCourier(couriermodel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourierModel> updateCourier(@PathVariable Long id, @RequestBody CourierModel courierDetails) {
        CourierModel updatedCourier = courierService.updateCourier(id, courierDetails);
        return updatedCourier != null ? ResponseEntity.ok(updatedCourier) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourier(@PathVariable Long id) {
        courierService.deleteCourier(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/available")
    public List<CourierModel> getAvailableCouriers() {
        return courierService.getAvailableCouriers();
    }

//    @GetMapping("/available/location/{location}")
//    public List<CourierModel> getAvailableCouriersByLocation(@PathVariable Integer location) {
//        return courierService.getAvailableCouriersByLocation(location);
//    }

    @PostMapping("/{id}/assign-order")
    public ResponseEntity<CourierModel> assignOrder(@PathVariable Long id) {
        CourierModel updatedCourier = courierService.assignOrder(id);
        return updatedCourier != null ? ResponseEntity.ok(updatedCourier) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/complete-order")
    public ResponseEntity<CourierModel> completeOrder(@PathVariable Long id) {
        CourierModel updatedCourier = courierService.completeOrder(id);
        return updatedCourier != null ? ResponseEntity.ok(updatedCourier) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<CourierModel> updateLocation(@PathVariable Long id,
                                                       @RequestBody Map<String, Long> locationData) {
        Long locationId = locationData.get("locationId");
        if (locationId == null) {
            return ResponseEntity.badRequest().build(); // Проверка на null
        }
        CourierModel updatedCourier = courierService.updateLocation(id, locationId);
        return updatedCourier != null ? ResponseEntity.ok(updatedCourier) : ResponseEntity.notFound().build();
    }
}
