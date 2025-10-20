package com.example.orderservice.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class StreetValidatorService {

    @Value("classpath:streets.txt")
    private Resource streetsFile;

    private Map<String, Integer> districtIds = new HashMap<>(); // Район -> ID
    private Map<String, Map<String, Integer>> streetIds = new HashMap<>(); // Район -> (Улица -> ID)
    private Map<String, Map<String, Map<String, Integer>>> houseIds = new HashMap<>(); // Район -> (Улица -> (Дом -> ID))

    @PostConstruct
    public void init() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(streetsFile.getInputStream()))) {
            String line;
            String currentDistrict = null;
            int districtCounter = 1;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String district = parts[0].trim().toLowerCase();
                    String street = parts[1].trim().toLowerCase();
                    String house = parts[2].trim();

                    // Инициализация нового района
                    if (!districtIds.containsKey(district)) {
                        districtIds.put(district, districtCounter++);
                        streetIds.put(district, new HashMap<>());
                        houseIds.put(district, new HashMap<>());
                    }

                    // Инициализация новой улицы
                    Map<String, Integer> streets = streetIds.get(district);
                    if (!streets.containsKey(street)) {
                        streets.put(street, streets.size() + 1); // ID улицы — порядковый номер в районе
                        houseIds.get(district).put(street, new HashMap<>());
                    }

                    // Добавление дома
                    Map<String, Integer> houses = houseIds.get(district).get(street);
                    if (!houses.containsKey(house)) {
                        houses.put(house, houses.size() + 1); // ID дома — порядковый номер на улице
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка парсинга файла улиц: " + e.getMessage());
        }
    }

    public String generateLocationCode(String district, String street, String house) {
        district = district.trim().toLowerCase();
        street = street.trim().toLowerCase();
        house = house.trim();

        Integer distId = districtIds.get(district);
        if (distId == null) {
            throw new IllegalArgumentException("Район не существует: " + district);
        }

        Map<String, Integer> streets = streetIds.get(district);
        if (streets == null || !streets.containsKey(street)) {
            throw new IllegalArgumentException("Улица не существует в районе " + district + ": " + street);
        }
        Integer strId = streets.get(street);

        Map<String, Map<String, Integer>> districtHouses = houseIds.get(district);
        if (districtHouses == null) {
            throw new IllegalArgumentException("Данные о домах для района " + district + " отсутствуют");
        }
        Map<String, Integer> houses = districtHouses.get(street);
        if (houses == null || !houses.containsKey(house)) {
            throw new IllegalArgumentException("Дом не существует на улице " + street + ": " + house);
        }
        Integer hseId = houses.get(house);

        return String.format("%02d%02d%02d", distId, strId, hseId);
    }
}