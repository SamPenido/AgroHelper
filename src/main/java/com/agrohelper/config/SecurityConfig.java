package com.agrohelper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// Use WebSecurityConfigurerAdapter for Spring Boot 2.7.x
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow requests from typical frontend development server origins
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000", "null")); // "null" for file:// origin if testing locally
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // Allow all headers (Use Arrays.asList for Java 8)
        configuration.setAllowCredentials(true); // Allow cookies/credentials
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply CORS to all paths
        return source;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors() // Enable CORS using the corsConfigurationSource bean
            .and()
            .csrf().disable() // Disable CSRF - common for APIs, ensure frontend handles CSRF if needed for session auth

            // Exception handling for authentication entry point
            .exceptionHandling()
                // Return 401 Unauthorized instead of redirecting to a login page for API calls
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .and()

            // Authorization rules
            .authorizeRequests()
                // Allow public access to authentication endpoints and OPTIONS requests (for CORS preflight)
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/api/auth/register", "/api/auth/login", "/api/auth/status").permitAll()
                // Allow public access to view products (GET requests)
                .antMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                // Allow access to static frontend files if served by Spring Boot (adjust if needed)
                .antMatchers("/", "/index.html", "/assets/**", "/pages/**", "/components/**").permitAll()
                // Require authentication for all other requests
                .anyRequest().authenticated()
            .and()

            // Form Login Configuration (for session-based auth)
            .formLogin()
                .loginProcessingUrl("/api/auth/login") // Endpoint Spring Security listens to
                // Send 200 OK on success instead of redirect
                .successHandler((req, res, auth) -> res.setStatus(HttpStatus.OK.value()))
                // Send 401 Unauthorized on failure
                .failureHandler(new SimpleUrlAuthenticationFailureHandler()) // Default sends 401
                .permitAll() // Allow access to the login processing URL
            .and()

            // Logout Configuration
            .logout()
                .logoutUrl("/api/auth/logout")
                // Send 200 OK on successful logout
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .deleteCookies("JSESSIONID") // Delete session cookie on logout
                .invalidateHttpSession(true) // Invalidate the session
                .permitAll(); // Allow access to the logout URL
    }
}
