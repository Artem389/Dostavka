package com.example.apigateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    /**
     * Этот метод будет вызван, когда order-service недоступен.
     * Путь /fallback/order-service должен совпадать с fallbackUri в application.yml.
     */
    @GetMapping("/order-service")
    public ResponseEntity<Map<String, String>> orderServiceFallback() {
        // Создаем простое сообщение об ошибке
        Map<String, String> response = Map.of(
                "error", "Service Unavailable",
                "message", "Order service is currently down. Please try again later."
        );
        // Возвращаем ответ со статусом 503 Service Unavailable
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/staff-service")
    public ResponseEntity<Map<String, String>> staffFallback() {
        Map<String, String> response = Map.of("error", "Service Unavailable", "message", "Staff service down.");
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/distributor-service")
    public ResponseEntity<Map<String, String>> distributorFallback() {
        Map<String, String> response = Map.of("error", "Service Unavailable", "message", "Distributor service down.");
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
}