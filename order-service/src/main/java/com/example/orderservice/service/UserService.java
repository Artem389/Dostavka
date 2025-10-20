package com.example.orderservice.service;

import com.example.orderservice.model.User;

import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    Optional<User> findByLogin(String login);
}