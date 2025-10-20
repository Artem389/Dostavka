package com.example.staffservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CourierService courierService;

    @Autowired
    public CustomUserDetailsService(CourierService courierService) {
        this.courierService = courierService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return courierService.getCourierByPhone(username)
                .map(courier -> new org.springframework.security.core.userdetails.User(
                        courier.getPhone(),
                        courier.getPassword(),
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_" + courier.getRole().name()))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Курьер с номером телефона " + username + " не найден"));
    }
}