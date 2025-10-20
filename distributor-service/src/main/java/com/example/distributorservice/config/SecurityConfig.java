package com.example.distributorservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 1. Указывает, что это конфигурационный класс
@EnableWebSecurity // 2. Включает веб-безопасность Spring
public class SecurityConfig {

    @Bean // 3. Определяет бин, который будет управляться Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 4. Отключаем CSRF-защиту. Это часто делают для REST API.
                .csrf(AbstractHttpConfigurer::disable)

                // 5. Настраиваем правила авторизации запросов
                .authorizeHttpRequests(auth -> auth
                        // Временно разрешаем все запросы, чтобы убедиться, что приложение запускается.
                        // Позже вы сможете настроить более строгие правила.
                        .anyRequest().permitAll()
                );

        // 6. Собираем и возвращаем сконфигурированный объект SecurityFilterChain
        return http.build();
    }
}