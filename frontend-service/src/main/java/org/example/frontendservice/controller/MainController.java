package org.example.frontendservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String home(Model model) {
        // Можно передать данные в шаблон, если нужно
//        model.addAttribute("order-service", new String[]{"order-service", "staff-service"});
        return "home";
    }

    @GetMapping("/become-courier")
    public String becomecourier(Model model) {
        return "become-courier";
    }
}