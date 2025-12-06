package com.baymak.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. AUTH endpoint'leri için JWT kontrolü yapma
        if (request.getServletPath().startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Authorization header kontrolü
        String authHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            // Bearer token yoksa, request devam eder (SecurityConfig zaten kontrol edecek)
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Token'ı al ve doğrula
        String token = authHeader.substring(7);
        
        try {
            if (jwtTokenProvider.validateToken(token)) {
                // Token geçerli, authentication set et
                String email = jwtTokenProvider.getEmailFromToken(token);
                log.debug("Valid JWT token found for user: {}", email);
                
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Authentication set for user: {}", email);
            } else {
                // Token geçersiz, log yaz
                log.warn("Invalid JWT token for request: {}", request.getServletPath());
            }
        } catch (Exception e) {
            // Herhangi bir hata durumunda detaylı log yaz
            log.error("Error processing JWT token for request: {} - Error: {}", 
                    request.getServletPath(), e.getMessage(), e);
        }

        // 4. Request'i devam ettir (hiçbir durumda 403 döndürme)
        filterChain.doFilter(request, response);
    }
}

