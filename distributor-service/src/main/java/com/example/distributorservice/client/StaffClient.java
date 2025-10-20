//package com.example.distributorservice.client;
//
//import com.example.staffservice.model.CourierModel;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@FeignClient(name = "api-gateway", path = "/api/staff")
//public interface StaffClient {
//    @GetMapping("/couriers/available")
//    List<CourierModel> getAvailableCouriers();
//
//    @PostMapping("/couriers/{id}/assign-order")
//    CourierModel assignOrder(@PathVariable Long id);
//
//    @PutMapping("/couriers/{id}/location")
//    CourierModel updateCourierLocation(@PathVariable Long id, @RequestBody Map<String, Long> locationData);
//}