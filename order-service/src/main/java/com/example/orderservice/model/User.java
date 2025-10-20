package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User() {

    }

    public enum Role {
        CLIENT, ADMIN
    }

    public User (String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Long getId() {return id;}
    public String getLogin() {return login;}
    public String getPassword() {return password;}
    public Role getRole() {return role;}
    public void setId(Long id) {this.id = id;}
    public void setLogin(String login) {this.login = login;}
    public void setPassword(String password) {this.password = password;}
    public void setRole(Role role) {this.role = role;}

}