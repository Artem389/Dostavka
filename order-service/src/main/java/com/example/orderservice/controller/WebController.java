package com.example.orderservice.controller;

import com.example.orderservice.model.*;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.ProductService;
import com.example.orderservice.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("cart")
public class WebController {

    private final ProductService productService;
    private final OrderService orderService;
    private final RestaurantService restaurantService;

    @Autowired
    public WebController(ProductService productService, OrderService orderService, RestaurantService restaurantService) {
        this.productService = productService;
        this.orderService = orderService;
        this.restaurantService = restaurantService;
    }

    @ModelAttribute("cart")
    public List<OrderItem> getCart() {
        return new ArrayList<>();
    }


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        return "home";
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/restaurant/add")
    public String addRestaurantForm(Model model) {
        model.addAttribute("restaurantTypes", RestaurantType.values());
        return "add-restaurant";
    }

    @GetMapping("/restaurant/{id}")
    public String restaurantDetails(@PathVariable Long id, Model model) {
        Restaurant restaurant = restaurantService.findById(id)
                .orElseThrow(() -> new RuntimeException("Ресторан не найден!"));
        List<Product> products = productService.getProductsByRestaurantId(id);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("products", products);
        return "restaurant-details";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, @ModelAttribute("cart") List<OrderItem> cart, @RequestHeader(value = "Referer", required = false) final String referer) {
        Product product = productService.getAllProducts().stream()
                .filter(p -> p.getId().equals(productId)).findFirst().orElse(null);

        if (product != null) {
            cart.stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst()
                    .ifPresentOrElse(
                            item -> item.setQuantity(item.getQuantity() + 1),
                            () -> {
                                OrderItem newItem = new OrderItem();
                                newItem.setProduct(product);
                                newItem.setQuantity(1);
                                newItem.setPrice(product.getPrice());
                                cart.add(newItem);
                            }
                    );
        }
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long productId, @ModelAttribute("cart") List<OrderItem> cart, @RequestHeader(value = "Referer", required = false) final String referer) {
        cart.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    if (item.getQuantity() > 1) {
                        item.setQuantity(item.getQuantity() - 1);
                    } else {
                        cart.remove(item);
                    }
                });
        return "redirect:" + (referer != null ? referer : "/cart");
    }

    @GetMapping("/cart")
    public String showCart(Model model, @ModelAttribute("cart") List<OrderItem> cart) {
        BigDecimal total = cart.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("cart", cart);
        model.addAttribute("totalAmount", total);
        return "cart";
    }

    @PostMapping("/order/place")
    public String placeOrder(
            @RequestParam String customerName,
            @RequestParam String district,
            @RequestParam String street,
            @RequestParam String house,
            @RequestParam String customerPhone,
            @ModelAttribute("cart") List<OrderItem> cart,
            RedirectAttributes redirectAttributes,
            SessionStatus sessionStatus) {

        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ваша корзина пуста!");
            return "redirect:/cart";
        }

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setDistrict(district);
        order.setStreet(street);
        order.setHouse(house);
        order.setCustomerPhone(customerPhone);
        order.setItems(new ArrayList<>(cart));

        try {
            Order savedOrder = orderService.createOrder(order);
            sessionStatus.setComplete();
            return "redirect:/order/confirmation/" + savedOrder.getId();
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/order/confirmation/{id}")
    public String orderConfirmation(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Заказ не найден!"));
        model.addAttribute("order", order);
        return "order-confirmation";
    }

    @PostMapping("/restaurant/add")
    public String addRestaurant(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String iconClass,
            @RequestParam Integer averageCheck,
            @RequestParam String type,
            @RequestParam String district,
            @RequestParam String street,
            @RequestParam String house,
            RedirectAttributes redirectAttributes) {

        try {
            Restaurant restaurant = new Restaurant(name, description, iconClass, averageCheck, RestaurantType.valueOf(type.toUpperCase()), district, street, house);
            restaurantService.saveRestaurant(restaurant);
            return "redirect:/";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/restaurant/add";
        }
    }

    @GetMapping("/manage-products")
    public String manageProducts(@RequestParam(required = false) Long restaurantId, Model model) {
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        if (restaurantId != null) {
            Restaurant restaurant = restaurantService.findById(restaurantId)
                    .orElseThrow(() -> new RuntimeException("Ресторан не найден!"));
            model.addAttribute("products", productService.getProductsByRestaurantId(restaurantId));
            model.addAttribute("selectedRestaurant", restaurant);
            model.addAttribute("restaurantId", restaurantId);
        }
        return "manage-products";
    }

    @GetMapping("/product/edit/{id}")
    public String editProductForm(@PathVariable Long id, @RequestParam Long restaurantId, Model model) {
        Product product = productService.getAllProducts().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Продукт не найден!"));
        model.addAttribute("product", product);
        model.addAttribute("restaurantId", restaurantId);
        return "edit-product";
    }

    @PostMapping("/product/add")
    public String addProduct(
            @RequestParam Long restaurantId,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) Integer calories,
            @RequestParam(required = false) Double proteins,
            @RequestParam(required = false) Double fats,
            @RequestParam(required = false) Double carbohydrates,
            RedirectAttributes redirectAttributes) {

        try {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Название продукта не может быть пустым");
            }
            if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Цена должна быть больше 0");
            }
            Restaurant restaurant = restaurantService.findById(restaurantId)
                    .orElseThrow(() -> new RuntimeException("Ресторан не найден с ID: " + restaurantId));
            if (restaurant.getId() == null) {
                throw new IllegalStateException("Ресторан не сохранен в базе данных");
            }
            Product product = new Product(name, description, price, calories, proteins, fats, carbohydrates, restaurant);
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Продукт успешно добавлен");
            return "redirect:/manage-products?restaurantId=" + restaurantId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при добавлении продукта: " + e.getMessage());
            return "redirect:/manage-products?restaurantId=" + restaurantId;
        }
    }

    @PostMapping("/product/update")
    public String updateProduct(
            @RequestParam Long id,
            @RequestParam Long restaurantId,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) Integer calories,
            @RequestParam(required = false) Double proteins,
            @RequestParam(required = false) Double fats,
            @RequestParam(required = false) Double carbohydrates,
            RedirectAttributes redirectAttributes) {

        try {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Название продукта не может быть пустым");
            }
            if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Цена должна быть больше 0");
            }
            Product product = productService.getAllProducts().stream()
                    .filter(p -> p.getId().equals(id)).findFirst()
                    .orElseThrow(() -> new RuntimeException("Товар не найден!"));
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setCalories(calories);
            product.setProteins(proteins);
            product.setFats(fats);
            product.setCarbohydrates(carbohydrates);
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Продукт успешно обновлен");
            return "redirect:/manage-products?restaurantId=" + restaurantId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении продукта: " + e.getMessage());
            return "redirect:/manage-products?restaurantId=" + restaurantId;
        }
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, @RequestParam Long restaurantId, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getAllProducts().stream()
                    .filter(p -> p.getId().equals(id)).findFirst()
                    .orElseThrow(() -> new RuntimeException("Товар не найден!"));
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Продукт успешно удален");
            return "redirect:/manage-products?restaurantId=" + restaurantId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении продукта: " + e.getMessage());
            return "redirect:/manage-products?restaurantId=" + restaurantId;
        }
    }
}