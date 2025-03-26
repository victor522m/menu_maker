package com.bootcamp.menu_maker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;





@SpringBootApplication
public class MenuMakerApplication {

	public static void main(String[] args) {

		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("123");
        System.out.println("Encoded password: " + encodedPassword); // Ejemplo para el password "123"*/

		SpringApplication.run(MenuMakerApplication.class, args);
	}

}
