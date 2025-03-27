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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer -> 
            configurer
                // Permitir acceso público a la raíz
                .requestMatchers("/").permitAll()
                // Permitir acceso a los detalles del usuario autenticado (para React)
                .requestMatchers("/api/user").authenticated()
                // Configuración de permisos para rutas específicas
                .requestMatchers(HttpMethod.GET, "/api/menus").hasAnyRole("OWNER", "USER")
                .requestMatchers(HttpMethod.GET, "/api/menus/{id}").hasRole("OWNER")
                .requestMatchers(HttpMethod.GET, "/api/menus/pdf/{id}/{porcentajeIva}").hasAnyRole("OWNER", "USER")
                .requestMatchers(HttpMethod.POST, "/api/menus").hasRole("OWNER")
                .requestMatchers(HttpMethod.PUT, "/api/menus/{id}").hasRole("OWNER")
                .requestMatchers(HttpMethod.DELETE, "/api/menus/{id}").hasRole("OWNER")
                .requestMatchers(HttpMethod.GET, "/api/menus/{id}/platos").hasAnyRole("OWNER", "USER")
                .requestMatchers(HttpMethod.POST, "/api/menus/{id}/platos").hasRole("OWNER")
                .requestMatchers(HttpMethod.GET, "/api/platos").hasAnyRole("OWNER", "USER")
                .requestMatchers(HttpMethod.GET, "/api/platos/{id}").hasAnyRole("OWNER", "USER")
                .requestMatchers(HttpMethod.POST, "/api/platos").hasRole("OWNER")
                .requestMatchers(HttpMethod.PUT, "/api/platos/{id}").hasRole("OWNER")
                .requestMatchers(HttpMethod.DELETE, "/api/platos/{id}").hasRole("OWNER")
        );

        http
            .httpBasic(Customizer.withDefaults()) // Autenticación básica (para pruebas)
            .formLogin(form -> 
                form
                    .loginProcessingUrl("/login") // URL para el login
                    .permitAll()
            )
            .logout(logout -> 
                logout
                    .logoutUrl("/logout") // URL para logout
                    .logoutSuccessUrl("/")
            )
            .csrf(csrf -> csrf.disable()); // CSRF desactivado (útil para APIs REST)

        return http.build();
    }
}

