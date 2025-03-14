package com.example.blog.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.blog.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	   @Autowired
	   private final JwtAuthenticationFilter jwtAuthenticationFilter;

	    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
	        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	    }


	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/blogs/generate-token", "/v3/api-docs", "/swagger-ui/**", "/swagger-ui.html", "/v1/api/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html").permitAll() // Allow public access to token generation
                .requestMatchers("/api/blogs/{id}/summary","api/blogs//{id}", "/api/blogs").authenticated() // Require authentication for summary
                .anyRequest().authenticated() // Require authentication for any other request
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Register JWT filter
            .httpBasic(httpBasic -> httpBasic.disable()) // Disable HTTP Basic Authentication
            .formLogin(formLogin -> formLogin.disable())// Disable Form Login
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //configure session management.

        return http.build();
    }
}
