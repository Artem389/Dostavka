//package com.example.distributorservice.client;
//
//import com.example.orderservice.model.Order;
//import com.example.orderservice.model.Restaurant;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@FeignClient(name = "api-gateway", path = "/api/orders")
//public interface OrderClient {
//    @GetMapping("/restaurants")
//    List<Restaurant> getAllRestaurants();
//
//    @GetMapping("/{id}")
//    Order getOrderById(@PathVariable Long id);
//
//    @PutMapping("/{id}/assign-courier")
//    Order assignCourierToOrder(@PathVariable Long id, @RequestBody Map<String, Long> courierData);  // { "courierId": id }
//
//    @PutMapping("/{id}/assign")
//    Order assignToOrder(@PathVariable Long id, @RequestBody Map<String, Object> data);
//}