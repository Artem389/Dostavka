package com.example.staffservice.controller;

import lombok.extern.slf4j.Slf4j;
import com.example.staffservice.model.CourierModel;
import com.example.staffservice.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class CabinetController {

    @Autowired
    private CourierService courierService;

    @GetMapping("/cabinet")
    public String cabinet(Model model, Authentication authentication) {
        String phone = authentication.getName();
        CourierModel courier = courierService.getCourierByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Курьер не найден"));

        // Принудительно обновляем данные из БД
        courier = courierService.getCourierById(courier.getId())
                .orElseThrow(() -> new RuntimeException("Курьер не найден"));

//        log.info("Данные для кабинета: ID={}, Имя={}, Заказы={}, Улицы={}",
//                courier.getId(), courier.getName(), courier.getTotalOrders(), courier.getVisitedStreets());

        model.addAttribute("courier", courier);
        return "cabinet";
    }
}