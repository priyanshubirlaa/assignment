package com.example.blog.jwt;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;


@Configuration
public class JwtAuthenticationFilter implements Filter {

    private JwtUtil jwtUtil = new JwtUtil();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix

            if (jwtUtil.validateToken(token)) {
                String subject = jwtUtil.extractSubject(token);
                // Set authentication context if token is valid
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(subject, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continue filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Clean up if needed
    }
}
