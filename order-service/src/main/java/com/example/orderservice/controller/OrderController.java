package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.model.Restaurant;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "API для управления заказами")
public class OrderController {

    private final OrderService orderService;
    private final RestaurantService restaurantService;

    @Autowired
    public OrderController(OrderService orderService, RestaurantService restaurantService) {
        this.orderService = orderService;
        this.restaurantService = restaurantService;
    }

    @PostMapping
    @Operation(summary = "Создать новый заказ")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        try {
            Order newOrder = orderService.createOrder(order);
            return ResponseEntity.ok(newOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Или кастомный объект ошибки
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заказ по ID")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<Order> assignToOrder(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        Optional<Order> optionalOrder = orderService.getOrderById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (data.containsKey("restaurantId")) {
                Long restaurantId = (Long) data.get("restaurantId");
                // Загрузить ресторан (предполагаем RestaurantService имеет findById)
                Restaurant restaurant = restaurantService.findById(restaurantId)
                        .orElseThrow(() -> new RuntimeException("Restaurant not found"));
                order.setRestaurant(restaurant);
            }
            if (data.containsKey("courierId")) {
                order.setCourierId((Long) data.get("courierId"));
            }
            Order updated = orderService.updateOrder(order);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
}