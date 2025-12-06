package com.baymak.backend.config;

import com.baymak.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .authorizeHttpRequests(auth -> auth
                        // PUBLIC ENDPOINTS (herkese açık)
                        .requestMatchers(
                                "/api/auth/**",           // Register & Login
                                "/api/weather",          // Weather API
                                "/v3/api-docs/**",       // Swagger docs
                                "/swagger-ui/**",        // Swagger UI
                                "/swagger-ui.html"       // Swagger UI
                        ).permitAll()
                        
                        // ADMIN ENDPOINTS (en spesifik olanlar önce)
                        .requestMatchers("/api/appointments/all").hasRole("ADMIN")
                        .requestMatchers("/api/appointments/status/**").hasRole("ADMIN")
                        .requestMatchers("/api/appointments/*/assign").hasRole("ADMIN")
                        .requestMatchers("/api/technicians/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        
                        // TECHNICIAN ENDPOINTS
                        .requestMatchers("/api/appointments/assigned").hasRole("TECHNICIAN")
                        .requestMatchers("/api/appointments/*/status").hasRole("TECHNICIAN")
                        .requestMatchers("/api/service-reports/**").hasRole("TECHNICIAN")
                        
                        // CUSTOMER ENDPOINTS (spesifik olanlar önce)
                        .requestMatchers("/api/appointments/my/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/appointments/my").hasRole("CUSTOMER")
                        .requestMatchers("/api/devices/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/requests/**").hasRole("CUSTOMER")    // Service requests
                        .requestMatchers("/api/appointments").hasRole("CUSTOMER")  // POST - randevu oluşturma (en son, genel)
                        
                        // Diğer tüm endpoint'ler için authentication gerekli
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                    // 401 Unauthorized döndür (403 değil)
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"message\":\"Unauthorized: Authentication required\",\"status\":401}");
                }))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}