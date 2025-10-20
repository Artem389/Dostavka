package com.example.staffservice.controller;

import com.example.staffservice.model.CourierModel;
import com.example.staffservice.model.LocationModel;
import com.example.staffservice.service.CourierService;
import com.example.staffservice.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
//@RequestMapping("/")
public class WebController {

    private final CourierService courierService;
    private final LocationService locationService;

    @Autowired
    public WebController(CourierService courierService, LocationService locationService) {
        this.courierService = courierService;
        this.locationService = locationService;
    }

//    @GetMapping("/")
//    public String root() {
//        return "redirect:/home"; // Перенаправление с корневого URL на домашнюю страницу
//    }

    @GetMapping("/home")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/become-courier")
    public String becomecourier(Model model) {
        return "become-courier";
    }

    // --- МЕТОДЫ ИЗ AuthController ---

//    // Добавьте в index или новый метод
//    @GetMapping("/assigned-orders")
//    public String getAssignedOrders(Model model) {
//        // Вызов distributor API via Feign or RestTemplate
//        List<AssignedOrder> assigned = distributorClient.getAllAssigned();  // Добавьте FeignClient
//        model.addAttribute("assignedOrders", assigned);
//        return "assigned-orders";  // Новый template или интегрируйте в index
//    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("courier", new CourierModel());
        return "register";
    }

    @PostMapping("/register")
    public String registerCourier(CourierModel courier, RedirectAttributes redirectAttributes) {
        try {
            courierService.registerCourier(courier);
            redirectAttributes.addFlashAttribute("successMessage", "Регистрация успешна! Теперь вы можете войти.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // --- ОСТАЛЬНЫЕ МЕТОДЫ ИЗ WebController ---

//    @GetMapping
//    public String index(Model model) {
//        // Этот метод будет доступен только после аутентификации из-за настроек Security.
//        // Spring Security перенаправит на /admin или /cabinet в зависимости от роли.
//        // Прямой переход сюда для неавторизованного пользователя приведет на /login.
//        return "redirect:/admin"; // Лучше явно перенаправить на админ-панель
//    }

    @PostMapping("/couriers")
    public String createCourier(@ModelAttribute CourierModel courierModel, RedirectAttributes redirectAttributes) {
        try {
            courierService.createCourier(courierModel);
            redirectAttributes.addFlashAttribute("successMessage", "Курьер успешно добавлен!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin";
    }

    // ... (Все остальные ваши PostMapping методы для couriers и locations остаются здесь без изменений)
    @PostMapping("/couriers/{id}")
    public String updateCourier(@PathVariable Long id, @ModelAttribute CourierModel courier, RedirectAttributes redirectAttributes) {
        try {
            Optional<CourierModel> existingCourier = courierService.getCourierById(id);
            if (existingCourier.isPresent()) {
                CourierModel updatedCourier = existingCourier.get();
                updatedCourier.setName(courier.getName());
                updatedCourier.setPhone(courier.getPhone());
                updatedCourier.setEmail(courier.getEmail());
                if (courier.getCurrentLocation() != null && courier.getCurrentLocation().getId() != null) {
                    LocationModel location = locationService.getLocationById(courier.getCurrentLocation().getId())
                            .orElseThrow(() -> new RuntimeException("Локация с ID " + courier.getCurrentLocation().getId() + " не найдена"));
                    updatedCourier.setCurrentLocation(location);
                }
                updatedCourier.setMaxCapacity(courier.getMaxCapacity());
                updatedCourier.setIsAvailable(courier.getIsAvailable());
                courierService.updateCourier(updatedCourier);
                redirectAttributes.addFlashAttribute("successMessage", "Курьер успешно обновлен!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Курьер с ID " + id + " не найден");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/couriers/{id}/assign-order")
    public String assignOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<CourierModel> courierOpt = courierService.getCourierById(id);
            if (courierOpt.isPresent()) {
                CourierModel courier = courierOpt.get();
                if (!courier.getIsAvailable()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Нельзя назначить заказ занятому курьеру!");
                    return "redirect:/admin";
                }
                if (courier.getActiveOrders() >= courier.getMaxCapacity()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Превышена максимальная вместимость курьера!");
                    return "redirect:/admin";
                }
                courier.setActiveOrders(courier.getActiveOrders() + 1);
                if (courier.getActiveOrders() >= courier.getMaxCapacity()) {
                    courier.setIsAvailable(false); // Автоматически меняем статус на "Занят", если вместимость достигнута
                }
                courierService.updateCourier(courier);
                redirectAttributes.addFlashAttribute("successMessage", "Заказ успешно назначен курьеру!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Курьер с ID " + id + " не найден");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/couriers/{id}/complete-order")
    public String completeOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<CourierModel> courierOpt = courierService.getCourierById(id);
            if (courierOpt.isPresent()) {
                CourierModel courier = courierOpt.get();
                if (courier.getActiveOrders() > 0) {
                    courier.setActiveOrders(courier.getActiveOrders() - 1);
                    if (courier.getActiveOrders() < courier.getMaxCapacity()) {
                        courier.setIsAvailable(true); // Возвращаем статус "Доступен", если заказы меньше вместимости
                    }
                    courierService.updateCourier(courier);
                    redirectAttributes.addFlashAttribute("successMessage", "Заказ успешно завершен!");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "У курьера нет активных заказов для завершения!");
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Курьер с ID " + id + " не найден");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/couriers/{id}/delete")
    public String deleteCourier(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        courierService.deleteCourier(id);
        redirectAttributes.addFlashAttribute("successMessage", "Курьер деактивирован!");
        return "redirect:/admin";
    }

    @PostMapping("/couriers/{id}/update-location")
    public String updateLocation(@PathVariable Long id,
                                 @RequestParam("currentLocation.id") Long locationId,
                                 RedirectAttributes redirectAttributes) {
        try {
            CourierModel updated = courierService.updateLocation(id, locationId);
            if (updated != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Локация обновлена!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении локации");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/locations")
    public String createLocation(@ModelAttribute LocationModel locationModel, RedirectAttributes redirectAttributes) {
        try {
            locationService.createLocation(locationModel);
            redirectAttributes.addFlashAttribute("successMessage", "Локация успешно добавлена!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/locations/{id}/update")
    public String updateLocation(@PathVariable Long id, @ModelAttribute LocationModel locationModel, RedirectAttributes redirectAttributes) {
        try {
            LocationModel updated = locationService.updateLocation(id, locationModel);
            if (updated != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Локация успешно обновлена!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Локация с ID " + id + " не найдена");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/locations/{id}/delete")
    public String deleteLocation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Запрос на удаление локации с ID: " + id);
            locationService.deleteLocation(id);
            redirectAttributes.addFlashAttribute("successMessage", "Локация успешно удалена!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении локации: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}