//package com.example.distributorservice.service;
//
//import com.example.distributorservice.client.OrderClient;
//import com.example.distributorservice.client.StaffClient;
//import com.example.distributorservice.model.AssignedOrder;
//import com.example.distributorservice.repository.AssignedOrderRepository;
//import com.example.orderservice.model.Order;
//import com.example.orderservice.model.Restaurant;
//import com.example.orderservice.service.OrderService.OrderCreatedEvent;
//import com.example.staffservice.model.CourierModel;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Service;
//
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@Service
//public class DistributorService {
//
//    private final StaffClient staffClient;
//    private final OrderClient orderClient;
//    private final AssignedOrderRepository assignedOrderRepository;
//
//    @Autowired
//    public DistributorService(StaffClient staffClient, OrderClient orderClient, AssignedOrderRepository assignedOrderRepository) {
//        this.staffClient = staffClient;
//        this.orderClient = orderClient;
//        this.assignedOrderRepository = assignedOrderRepository;
//    }
//
//    private int[] parseLocationCode(String code) {
//        if (code == null || code.length() != 6) {
////            log.error("Некорректный код адреса: {}", code);
//            throw new IllegalArgumentException("Некорректный код адреса: " + code);
//        }
//        try {
//            return new int[]{
//                    Integer.parseInt(code.substring(0, 2)),
//                    Integer.parseInt(code.substring(2, 4)),
//                    Integer.parseInt(code.substring(4, 6))
//            };
//        } catch (NumberFormatException e) {
////            log.error("Ошибка парсинга кода адреса: {}", code, e);
//            throw new IllegalArgumentException("Ошибка парсинга кода адреса: " + code, e);
//        }
//    }
//
//    private double calculateDistance(String code1, String code2) {
//        int[] pos1 = parseLocationCode(code1);
//        int[] pos2 = parseLocationCode(code2);
//        return Math.sqrt(Math.pow(pos1[0] - pos2[0], 2) + Math.pow(pos1[1] - pos2[1], 2) + Math.pow(pos1[2] - pos2[2], 2));
//    }
//
//    public void distributeOrder(Long orderId) {
////        log.info("Начало распределения заказа с ID: {}", orderId);
//        try {
//            // Шаг 1: Получение заказа
//            Order order = orderClient.getOrderById(orderId);
//            if (order == null) {
////                log.error("Заказ с ID {} не найден", orderId);
//                throw new RuntimeException("Заказ не найден");
//            }
////            log.info("Заказ с ID {} получен: {}", orderId, order);
//            String clientCode = order.getLocationCode();
//            if (clientCode == null) {
////                log.error("Код адреса клиента для заказа {} отсутствует", orderId);
//                throw new RuntimeException("Код адреса клиента отсутствует");
//            }
//
//            // Шаг 2: Поиск ближайшего ресторана
//            List<Restaurant> restaurants = getCachedRestaurants();
//            if (restaurants.isEmpty()) {
////                log.error("Нет доступных ресторанов для заказа {}", orderId);
//                throw new RuntimeException("Нет доступных ресторанов");
//            }
//            Restaurant nearestRestaurant = restaurants.stream()
//                    .min(Comparator.comparingDouble(r -> calculateDistance(clientCode, r.getLocationCode())))
//                    .orElseThrow(() -> new RuntimeException("Не удалось найти подходящий ресторан"));
////            log.info("Найден ближайший ресторан: ID {}, код адреса {}", nearestRestaurant.getId(), nearestRestaurant.getLocationCode());
//
//            // Шаг 3: Назначение ресторана заказу
//            Map<String, Object> assignData = new HashMap<>();
//            assignData.put("restaurantId", nearestRestaurant.getId());
//            Order assignedOrder = orderClient.assignToOrder(orderId, assignData);
////            log.info("Ресторан {} назначен заказу {}", nearestRestaurant.getId(), orderId);
//
//            // Шаг 4: Поиск ближайшего курьера
//            String restaurantCode = nearestRestaurant.getLocationCode();
//            List<CourierModel> availableCouriers = getCachedAvailableCouriers();
//            if (availableCouriers.isEmpty()) {
////                log.warn("Нет доступных курьеров для заказа {}", orderId);
//                throw new RuntimeException("Нет доступных курьеров");
//            }
//            CourierModel nearestCourier = availableCouriers.stream()
//                    .filter(c -> c.getActiveOrders() < c.getMaxCapacity())
//                    .min(Comparator.comparingDouble(c -> calculateDistance(restaurantCode, c.getCurrentLocation().getLocationCode())))
//                    .orElseThrow(() -> new RuntimeException("Нет доступных курьеров с подходящей загрузкой"));
////            log.info("Найден ближайший курьер: ID {}, текущий адрес {}", nearestCourier.getId(), nearestCourier.getCurrentLocation().getLocationCode());
//
//            // Шаг 5: Назначение заказа курьеру
//            staffClient.assignOrder(nearestCourier.getId());
//            assignData.clear();
//            assignData.put("courierId", nearestCourier.getId());
//            orderClient.assignToOrder(orderId, assignData);
////            log.info("Курьер {} назначен заказу {}", nearestCourier.getId(), orderId);
//
//            // Шаг 6: Сохранение назначенного заказа
//            AssignedOrder assigned = new AssignedOrder();
//            assigned.setCourierId(nearestCourier.getId());
//            assigned.setOrderId(orderId);
//            assigned.setOrderAddressCode(clientCode);
//            assignedOrderRepository.save(assigned);
////            log.info("Назначенный заказ сохранен: ID {}, курьер {}, заказ {}, адрес {}", assigned.getId(), nearestCourier.getId(), orderId, clientCode);
//
//        } catch (Exception e) {
////            log.error("Ошибка при распределении заказа {}: {}", orderId, e.getMessage(), e);
//            throw new RuntimeException("Ошибка распределения заказа: " + e.getMessage(), e);
//        }
//    }
//
//    @Cacheable("restaurants")
//    public List<Restaurant> getCachedRestaurants() {
////        log.info("Получение списка ресторанов из order-service");
//        return orderClient.getAllRestaurants();
//    }
//
//    @Cacheable("availableCouriers")
//    public List<CourierModel> getCachedAvailableCouriers() {
////        log.info("Получение списка доступных курьеров из staff-service");
//        return staffClient.getAvailableCouriers();
//    }
//
//    public List<AssignedOrder> getAllAssignedOrders() {
////        log.info("Получение всех назначенных заказов");
//        return assignedOrderRepository.findAll();
//    }
//
//    @EventListener
//    public void handleOrderCreated(OrderCreatedEvent event) {
////        log.info("Получено событие создания заказа с ID {}", event.getOrderId());
//        distributeOrder(event.getOrderId());
//    }
//}
