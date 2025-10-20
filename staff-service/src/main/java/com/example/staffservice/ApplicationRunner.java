package com.example.staffservice;

import com.example.staffservice.model.CourierModel;
import com.example.staffservice.model.Role;
import com.example.staffservice.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements CommandLineRunner {

    @Autowired
    private CourierService courierService;

    @Override
    public void run(String... args) throws Exception {
        // Создание админа, если не существует
        if (courierService.getCourierByPhone("admin_phone").isEmpty()) {
            CourierModel admin = new CourierModel();
            admin.setPhone("admin_phone");
            admin.setPassword("admin_password"); // Пароль хешируется в сервисе
            admin.setRole(Role.ADMIN); // Явно устанавливаем роль ADMIN
            admin.setName("Admin");
            admin.setEmail("admin@example.com");
            admin.setMaxCapacity(3);
            CourierModel savedAdmin = courierService.registerCourier(admin);
            System.out.println("Админ создан с ролью: " + savedAdmin.getRole()); // Для отладки
        }
    }
}