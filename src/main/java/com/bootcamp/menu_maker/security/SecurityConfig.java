package com.bootcamp.menu_maker.security;

import java.util.Arrays;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                /*
                 * .logout(logout -> logout
                 * .logoutUrl("/api/logout") // Endpoint personalizado
                 * //.logoutSuccessUrl("/login") // Redirección post-logout
                 * .invalidateHttpSession(true)
                 * .deleteCookies("JSESSIONID")
                 * .addLogoutHandler((request, response, auth) -> {
                 * // Lógica adicional si es necesario
                 * }))
                 */
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
                        }))

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(basic -> basic
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Acceso no autorizado");
                        }))
                .formLogin(form -> form.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/static/**",
                                "/favicon.ico",
                                "/manifest.json")
                        .permitAll()
                        // Rutas públicas
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Rutas con acceso autenticado
                        .requestMatchers(HttpMethod.GET, "/api/user-info").authenticated()
                        // Configuración para menús
                        .requestMatchers(HttpMethod.GET, "/api/menus").hasAnyRole("OWNER", "EMPLOYED")
                        .requestMatchers(HttpMethod.GET, "/api/menus/{id}").hasAnyRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/menus/pdf/{id}/{porcentajeIva}")
                        .hasAnyRole("OWNER", "EMPLOYED")
                        .requestMatchers(HttpMethod.POST, "/api/menus").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/menus/{id}", "/api/menus/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/menus/{id}", "/api/menus/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/menus/{id}/platos").hasAnyRole("OWNER", "EMPLOYED")
                        .requestMatchers(HttpMethod.POST, "/api/menus/{id}/platos").hasRole("OWNER")
                        // Configuración para platos
                        .requestMatchers(HttpMethod.GET, "/api/platos").hasAnyRole("OWNER", "EMPLOYED")
                        .requestMatchers(HttpMethod.GET, "/api/platos/{id}").hasAnyRole("OWNER", "EMPLOYED")
                        .requestMatchers(HttpMethod.POST, "/api/platos").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/platos/{id}", "/api/platos/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/platos/{id}", "/api/platos/**").hasRole("OWNER")
                        // Rutas adicionales
                        .anyRequest().authenticated())
                .httpBasic(basic -> basic.realmName("MenuMaker API"));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000", // Frontend en local
                "http://127.0.0.1:3000",
                "https://victor522m.github.io", // Frontend en producción
                "https://menumaker-production-b6ac.up.railway.app" // Frontend en producción en caso de alojarlo en
                                                                   // railway
        ));
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
