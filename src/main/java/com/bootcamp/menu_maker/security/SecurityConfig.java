package com.bootcamp.menu_maker.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;





@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }
/*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer -> 
        configurer
        // Permitir acceso público a la raíz
        .requestMatchers("/").permitAll()
        // Permitir acceso a obtener todos los menús para OWNER y USER --- Funciona en postman
        .requestMatchers(HttpMethod.GET, "/api/menus").hasAnyRole("OWNER", "USER")
        
        // Permitir acceso a obtener un menú específico solo para OWNER--- Funciona en postman
        .requestMatchers(HttpMethod.GET, "/api/menus/{id}").hasAnyRole("OWNER")
        
        // Permitir acceso al endpoint para generar PDF del menú solo para OWNER--- Funciona en navegador
        .requestMatchers(HttpMethod.GET, "/api/menus/pdf/{id}/{porcentajeIva}").hasAnyRole("OWNER", "USER")
        
        // Permitir creación de menús solo para OWNER --funciona en postman, ver json.txt
        .requestMatchers(HttpMethod.POST, "/api/menus").hasRole("OWNER")
        
        // Permitir actualización de menús solo para OWNER --funciona en postman, ver json.txt
        .requestMatchers(HttpMethod.PUT, "/api/menus/{id}").hasRole("OWNER")
        
        // Permitir eliminación de menús solo para OWNER--funciona en postman
        .requestMatchers(HttpMethod.DELETE, "/api/menus/{id}").hasRole("OWNER")
        
        // Permitir acceso a los platos de un menú solo para OWNER y USER--- Funciona en postman
        .requestMatchers(HttpMethod.GET, "/api/menus/{id}/platos").hasAnyRole("OWNER", "USER")
        
        // Permitir agregar platos a un menú solo para OWNER--Funciona en postman, ver json.txt
        .requestMatchers(HttpMethod.POST, "/api/menus/{id}/platos").hasRole("OWNER")

        // Obtener todos los platos: permitido para OWNER y USER--- Funciona en postman
        .requestMatchers(HttpMethod.GET, "/api/platos").hasAnyRole("OWNER", "USER")

        // Obtener un plato por ID: permitido para OWNER y USER --- Funciona en postman
        .requestMatchers(HttpMethod.GET, "/api/platos/{id}").hasAnyRole("OWNER", "USER")

        // Crear un plato: permitido solo para OWNER--Funciona en postman ver json.txt
        .requestMatchers(HttpMethod.POST, "/api/platos").hasRole("OWNER")

        // Actualizar un plato: permitido solo para OWNER--Funciona en postman ver json.txt
        .requestMatchers(HttpMethod.PUT, "/api/platos/{id}").hasRole("OWNER")

        // Eliminar un plato: permitido solo para OWNER--funciona en postman
        .requestMatchers(HttpMethod.DELETE, "/api/platos/{id}").hasRole("OWNER")

        );

        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
