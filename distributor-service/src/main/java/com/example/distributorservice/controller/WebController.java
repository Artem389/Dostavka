//package com.example.distributorservice.controller;
//
//import com.example.distributorservice.model.AssignedOrder;
//import com.example.distributorservice.service.DistributorService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//public class WebController {
//
//    @Autowired
//    private DistributorService distributorService;
//
//    @GetMapping("/")
//    public String index(Model model) {
//        try {
//            List<AssignedOrder> assignedOrders = distributorService.getAllAssignedOrders();
//            model.addAttribute("assignedOrders", assignedOrders);
//        } catch (Exception e) {
//            model.addAttribute("assignedOrders", new ArrayList<>());
//            model.addAttribute("errorMessage", "Не удалось загрузить данные: " + e.getMessage());
//        }
//        return "index";
//    }
//}