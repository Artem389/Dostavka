package com.example.orderservice.service;

import com.example.orderservice.model.Restaurant;
import com.example.orderservice.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final StreetValidatorService streetValidatorService;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, StreetValidatorService streetValidatorService) {
        this.restaurantRepository = restaurantRepository;
        this.streetValidatorService = streetValidatorService;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    // Метод для получения одного ресторана по его ID
    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    public Restaurant saveRestaurant(Restaurant restaurant) {
        // Проверка уникальности адреса
        Optional<Restaurant> existing = restaurantRepository.findByDistrictAndStreetAndHouseIgnoreCase(
                restaurant.getDistrict(), restaurant.getStreet(), restaurant.getHouse());
        if (existing.isPresent()) {
            throw new RuntimeException("Ресторан с таким адресом уже существует: " + restaurant.getDistrict() + ", " + restaurant.getStreet() + ", " + restaurant.getHouse());
        }

        // Генерация кода локации
        String locationCode = streetValidatorService.generateLocationCode(restaurant.getDistrict(), restaurant.getStreet(), restaurant.getHouse());
        restaurant.setLocationCode(locationCode);

        return restaurantRepository.save(restaurant);
    }
}