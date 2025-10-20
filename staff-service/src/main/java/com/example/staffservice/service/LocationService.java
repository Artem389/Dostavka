package com.example.staffservice.service;

import jakarta.transaction.Transactional;
import com.example.staffservice.model.LocationModel;
import com.example.staffservice.repository.CourierRepository;
import com.example.staffservice.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final CourierRepository courierRepository;
    private final StreetValidatorService streetValidatorService;

    @Autowired
    public LocationService(LocationRepository locationRepository, CourierRepository courierRepository, StreetValidatorService streetValidatorService) {
        this.locationRepository = locationRepository;
        this.courierRepository = courierRepository;
        this.streetValidatorService = streetValidatorService;
    }

    public List<LocationModel> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<LocationModel> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public LocationModel createLocation(LocationModel location) {
        // Проверка на уникальность адреса (ignore case)
        Optional<LocationModel> existing = locationRepository.findByDistrictAndStreetAndHouseIgnoreCase(
                location.getDistrict(), location.getStreet(), location.getHouse());
        if (existing.isPresent()) {
            throw new RuntimeException("Адрес уже существует (независимо от регистра): " + location.getDistrict() + ", " + location.getStreet() + ", " + location.getHouse());
        }

        try {
            String code = streetValidatorService.generateLocationCode(
                    location.getDistrict(), location.getStreet(), location.getHouse());
            location.setLocationCode(code);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage()); // Для обработки в контроллере
        }
        return locationRepository.save(location);
    }

    public LocationModel updateLocation(Long id, LocationModel locationDetails) {
        Optional<LocationModel> optionalLocation = locationRepository.findById(id);
        if (optionalLocation.isPresent()) {
            LocationModel location = optionalLocation.get();
            String currentDistrict = location.getDistrict();
            String currentStreet = location.getStreet();
            String currentHouse = location.getHouse();

            // Проверка на уникальность новых значений (ignore case), исключая текущую локацию
            Optional<LocationModel> existing = locationRepository.findByDistrictAndStreetAndHouseIgnoreCase(
                    locationDetails.getDistrict(), locationDetails.getStreet(), locationDetails.getHouse());
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                // Проверяем, изменился ли адрес
                if (!currentDistrict.equalsIgnoreCase(locationDetails.getDistrict()) ||
                        !currentStreet.equalsIgnoreCase(locationDetails.getStreet()) ||
                        !currentHouse.equalsIgnoreCase(locationDetails.getHouse())) {
                    throw new RuntimeException("Адрес уже существует в другой локации (независимо от регистра): " +
                            locationDetails.getDistrict() + ", " + locationDetails.getStreet() + ", " + locationDetails.getHouse());
                }
            }

            // Обновляем поля
            location.setDistrict(locationDetails.getDistrict());
            location.setStreet(locationDetails.getStreet());
            location.setHouse(locationDetails.getHouse());

            // Обновляем код локации, если адрес изменился
            try {
                String newCode = streetValidatorService.generateLocationCode(
                        location.getDistrict(), location.getStreet(), location.getHouse());
                location.setLocationCode(newCode);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e.getMessage());
            }
            return locationRepository.save(location);
        }
        return null;
    }

    @Transactional
    public void deleteLocation(Long id) {
        System.out.println("Начало удаления локации с ID: " + id);
        Optional<LocationModel> locationOpt = locationRepository.findById(id);
        if (locationOpt.isPresent()) {
            LocationModel location = locationOpt.get();
            long count = courierRepository.countByCurrentLocationId(id);
            System.out.println("Количество курьеров с локацией ID " + id + ": " + count);
            if (count > 0) {
                throw new IllegalStateException("Нельзя удалить локацию, так как она используется " + count + " курьером(ами). Сначала обновите или удалите связанных курьеров.");
            }
            locationRepository.delete(location);
            System.out.println("Локация с ID " + id + " удалена");
        } else {
            throw new RuntimeException("Локация с ID " + id + " не найдена");
        }
    }
}