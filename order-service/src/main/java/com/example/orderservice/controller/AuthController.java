package com.example.orderservice.controller;

import com.example.orderservice.model.User;
import com.example.orderservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String login,
                               @RequestParam String password,
                               RedirectAttributes redirectAttributes) {
        if (password.length() < 6) {
            redirectAttributes.addFlashAttribute("errorMessage", "Пароль должен быть минимум 6 символов");
            return "redirect:/register";
        }
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setRole(User.Role.CLIENT);
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Регистрация успешна! Войдите.");
        return "redirect:/login";
    }

}