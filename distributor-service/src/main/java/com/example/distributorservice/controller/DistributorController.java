//package com.example.distributorservice.controller;
//
//import com.example.distributorservice.service.DistributorService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/distribute")
//public class DistributorController {
//
//    @Autowired
//    private DistributorService distributorService;
//
//    @PostMapping("/order/{id}")
//    public ResponseEntity<String> distribute(@PathVariable Long id) {
//        distributorService.distributeOrder(id);
//        return ResponseEntity.ok("Order distributed successfully");
//    }
//}