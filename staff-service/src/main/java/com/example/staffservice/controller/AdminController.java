package com.example.staffservice.controller;

import com.example.staffservice.model.CourierModel;
import com.example.staffservice.model.LocationModel;
import com.example.staffservice.service.CourierService;
import com.example.staffservice.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CourierService courierService;

    @Autowired
    private LocationService locationService;

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("couriers", courierService.getAllCouriers());
        model.addAttribute("newCourier", new CourierModel());
        model.addAttribute("newLocation", new LocationModel());
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("availableCount", courierService.getAvailableCouriersCount());
        model.addAttribute("busyCount", courierService.getActiveCouriersCount() - courierService.getAvailableCouriersCount());
        model.addAttribute("totalOrders", courierService.getTotalActiveOrders());
        return "index";
    }
}