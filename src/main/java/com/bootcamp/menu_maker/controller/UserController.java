package com.bootcamp.menu_maker.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication; // Import añadido
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-info")
public class UserController {
    
    @GetMapping
    public Map<String, Object> getUserInfo(Authentication authentication) {
        return Map.of(
            "username", authentication.getName(),
            "roles", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", "")) 
                .collect(Collectors.toList())
        );
    }
}
