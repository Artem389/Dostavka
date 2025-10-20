package com.example.staffservice.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import com.example.staffservice.model.CourierModel;
import com.example.staffservice.model.LocationModel;
import com.example.staffservice.model.Role;
import com.example.staffservice.repository.CourierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;


@Slf4j
@Service
public class CourierService {

    private final CourierRepository courierRepository;
    private final LocationService locationService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CourierService(CourierRepository courierRepository, LocationService locationService, PasswordEncoder passwordEncoder) {
        this.courierRepository = courierRepository;
        this.locationService = locationService;
        this.passwordEncoder = passwordEncoder;
    }

    public CourierModel registerCourier(CourierModel courier) {
        if (courierRepository.findByPhone(courier.getPhone()).isPresent()) {
            throw new RuntimeException("Курьер с таким номером телефона уже существует");
        }

        // Устанавливаем значения по умолчанию
//        courier.setRole(Role.USER);
        courier.setRole(courier.getRole() != null ? courier.getRole() : Role.USER);
        courier.setCurrentLocation(null); // Локация по умолчанию не выбрана
        courier.setMaxCapacity(courier.getMaxCapacity() != null ? courier.getMaxCapacity() : 3); // Вместимость 3 по умолчанию
        courier.setPassword(passwordEncoder.encode(courier.getPassword()));
        return courierRepository.save(courier);
    }

    public Optional<CourierModel> getCourierByPhone(String phone) {
        return courierRepository.findByPhone(phone);
    }


//    public List<CourierModel> getAllCouriers() {
//        List<CourierModel> couriers = courierRepository.findByIsActiveTrue();
//        if (couriers == null) {
//            log.info("CourierRepository returned null for findByIsActiveTrue");
//            return new ArrayList<>();
//        }
//        return couriers;
//    }

    public List<CourierModel> getAllCouriers() {
        return courierRepository.findAll();
    }

    public Optional<CourierModel> getCourierById(Long id) {
        return courierRepository.findById(id);
    }

    public CourierModel createCourier(CourierModel courier) {
        // Проверяем уникальность email
        if (courierRepository.findByEmail(courier.getEmail()).isPresent()) {
            throw new RuntimeException("Курьер с таким email уже существует");
        }
        // Устанавливаем локацию по ID, если передан
        if (courier.getCurrentLocation() != null && courier.getCurrentLocation().getId() != null) {
            Optional<LocationModel> location = locationService.getLocationById(courier.getCurrentLocation().getId());
            location.ifPresent(courier::setCurrentLocation);
        }
        return courierRepository.save(courier);
    }

    public CourierModel updateCourier(Long id, CourierModel courierDetails) {
        Optional<CourierModel> optionalCourier = courierRepository.findById(id);
        if (optionalCourier.isPresent()) {
            CourierModel courier = optionalCourier.get();
            courier.setName(courierDetails.getName());
            courier.setPhone(courierDetails.getPhone());
            courier.setEmail(courierDetails.getEmail());
            // Обновление локации
            if (courierDetails.getCurrentLocation() != null && courierDetails.getCurrentLocation().getId() != null) {
                Optional<LocationModel> location = locationService.getLocationById(courierDetails.getCurrentLocation().getId());
                location.ifPresent(courier::setCurrentLocation);
            }
            courier.setMaxCapacity(courierDetails.getMaxCapacity());
            return courierRepository.save(courier);
        }
        return null;
    }

    public CourierModel updateCourier(CourierModel courier) {
        return courierRepository.save(courier);
    }

    @Transactional
    public void deleteCourier(Long id) {
        courierRepository.deleteById(id);
    }

    public List<CourierModel> getAvailableCouriers() {
        return courierRepository.findByIsAvailableTrue();
    }

//    public List<CourierModel> getAvailableCouriersByLocation(Integer location) {
//        return courierRepository.findByCurrentLocationAndIsAvailableTrue(location);
//    }

    public CourierModel assignOrder(Long courierId) {
        Optional<CourierModel> optionalCourier = courierRepository.findById(courierId);
        if (optionalCourier.isPresent()) {
            CourierModel courier = optionalCourier.get();
            courier.setActiveOrders(courier.getActiveOrders() + 1);
            if (courier.getActiveOrders() >= courier.getMaxCapacity()) {
                courier.setIsAvailable(false);
            }
            return courierRepository.save(courier);
        }
        return null;
    }

    public CourierModel completeOrder(Long courierId) {
//        log.info("Вызов completeOrder для курьера ID: {}", courierId);

        Optional<CourierModel> optionalCourier = courierRepository.findById(courierId);
        if (optionalCourier.isPresent()) {
            CourierModel courier = optionalCourier.get();
//            log.info("Курьер найден: {}, текущие заказы: {}, общие заказы: {}",
//                    courier.getName(), courier.getActiveOrders(), courier.getTotalOrders());

            if (courier.getActiveOrders() > 0) {
                courier.setActiveOrders(courier.getActiveOrders() - 1);
                courier.setTotalOrders(courier.getTotalOrders() + 1);

                // Сохраняем улицу в историю посещений
                if (courier.getCurrentLocation() != null && courier.getCurrentLocation().getStreet() != null) {
                    String street = courier.getCurrentLocation().getStreet();
//                    log.info("Добавляем улицу в историю: {}", street);

                    Set<String> updatedStreets = new HashSet<>(courier.getVisitedStreets());
                    updatedStreets.add(street);
                    courier.setVisitedStreets(updatedStreets);
                }

                if (courier.getActiveOrders() < courier.getMaxCapacity()) {
                    courier.setIsAvailable(true);
                }

                CourierModel saved = courierRepository.save(courier);
//                log.info("После сохранения - общие заказы: {}, улицы: {}",
//                        saved.getTotalOrders(), saved.getVisitedStreets());
                return saved;
            }
        }
//        log.warn("Курьер с ID {} не найден или нет активных заказов", courierId);
        return null;
    }

    public CourierModel updateLocation(Long courierId, Long locationId) {
        Optional<CourierModel> optionalCourier = courierRepository.findById(courierId);
        if (optionalCourier.isPresent()) {
            CourierModel courier = optionalCourier.get();
            Optional<LocationModel> location = locationService.getLocationById(locationId);
            if (location.isPresent()) {
                courier.setCurrentLocation(location.get());
                return courierRepository.save(courier);
            } else {
                throw new RuntimeException("Локация с ID " + locationId + " не найдена");
            }
        }
        return null;
    }

    public Long getActiveCouriersCount() {
        return courierRepository.countActiveCouriers();
    }

    public Long getAvailableCouriersCount() {
        return (long) courierRepository.findByIsAvailableTrue().size();
    }

    public Integer getTotalActiveOrders() {
        return courierRepository.findByIsActiveTrue()
                .stream()
                .mapToInt(CourierModel::getActiveOrders)
                .sum();
    }
}