package com.example.orderservice.service;

import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final StreetValidatorService streetValidatorService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public OrderService(OrderRepository orderRepository, StreetValidatorService streetValidatorService, ApplicationEventPublisher applicationEventPublisher) {
        this.orderRepository = orderRepository;
        this.streetValidatorService = streetValidatorService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    public Order createOrder(Order order) {
        order.getItems().forEach(item -> item.setOrder(order)); // Связываем позиции с заказом
        BigDecimal total = order.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);

        // Генерация и установка кода локации
        try {
            String locationCode = streetValidatorService.generateLocationCode(order.getDistrict(), order.getStreet(), order.getHouse());
            order.setLocationCode(locationCode);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage());
        }

        System.out.println("Новый заказ создан! ID: " + order.getId() + ". Уведомляем Распределитель.");
        orderRepository.save(order);
        applicationEventPublisher.publishEvent(new OrderCreatedEvent(this, order.getId()));
        return order;
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Событие
    public static class OrderCreatedEvent {
        private final Object source;
        private final Long orderId;

        public OrderCreatedEvent(Object source, Long orderId) {
            this.source = source;
            this.orderId = orderId;
        }

        public Long getOrderId() { return orderId; }
    }
}