package com.expensemanagement.backend.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
        .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simplicity in this API
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/users/register").permitAll() // Allow unauthenticated access to register
                .requestMatchers("/api/users/login").permitAll() // Allow unauthenticated access to login
                .requestMatchers("/api/users/verify-forgotpass-user").permitAll()
                .requestMatchers("/api/users/reset-password").permitAll()
                .requestMatchers("/api/users/remove-user").permitAll()
                .requestMatchers("/api/users/get-user-profile/{username}").permitAll()
                .requestMatchers("/api/users/update-user/{username}").permitAll()
                .requestMatchers("/api/users/contact-us").permitAll()
                
                .requestMatchers("/api/employee/expensesByUsername/{username}").permitAll()
                .requestMatchers("/api/employee/addexpense/{username}").permitAll()
                .requestMatchers("/api/employee/delete-expense/{username}/{id}").permitAll()
                .requestMatchers("/api/employee/update-expense/{username}").permitAll()
                .requestMatchers("/api/employee/update-expense-name").permitAll()
                
                .requestMatchers("/api/manager/expensesByUsername/{username}").permitAll()
                .requestMatchers("/api/manager/addexpense/{username}").permitAll()
                .requestMatchers("/api/manager/delete-expense/{username}/{id}").permitAll()
                .requestMatchers("/api/manager/update-expense/{username}").permitAll()
                .requestMatchers("/api/manager/expenses/allEmployee").permitAll()
                .requestMatchers("/api/manager/expense/{expenseId}/approve").permitAll()
                .requestMatchers("/api/manager/expense/{expenseId}/reject").permitAll()
                
                .anyRequest().authenticated() // All other endpoints require authentication
        )
        .httpBasic(AbstractHttpConfigurer::disable); // Disable HTTP Basic Authentication

		return http.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration=new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Allow your frontend origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow these HTTP methods
        configuration.setAllowedHeaders(Arrays.asList("*"));     // Allow all headers
        configuration.setAllowCredentials(true);       // Allow credentials
        
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
	}

}
