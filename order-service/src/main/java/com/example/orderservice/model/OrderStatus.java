package com.example.orderservice.model;

public enum OrderStatus {
    PENDING, // В ожидании
    PROCESSING, // В обработке
    DELIVERING, // Доставляется
    COMPLETED, // Завершен
    CANCELLED // Отменен
}