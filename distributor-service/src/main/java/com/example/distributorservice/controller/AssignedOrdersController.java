package com.example.distributorservice.controller;

import com.example.distributorservice.model.AssignedOrder;
import com.example.distributorservice.repository.AssignedOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
public class AssignedOrdersController {

    @Autowired
    private AssignedOrderRepository assignedOrderRepository;

    @GetMapping("/assigned-orders")
    public String viewAssignedOrders(Model model) {
        List<AssignedOrder> orders = assignedOrderRepository.findAll();
//        log.info("Получено {} назначенных заказов", orders.size());
        model.addAttribute("assignedOrders", orders);
        return "assigned-orders";
    }
}