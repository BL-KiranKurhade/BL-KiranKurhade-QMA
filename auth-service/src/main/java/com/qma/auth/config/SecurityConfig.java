package com.qma.auth.config;

import com.qma.auth.security.JwtAuthFilter;
import com.qma.auth.security.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 6 — stateless JWT authentication (Spring Boot 3 style).
 *
 * Design decisions:
 *  - CORS is owned exclusively by the API Gateway (application.yml globalcors).
 *    Enabling it here would duplicate the header and break CORS entirely.
 *  - Sessions are STATELESS — no HttpSession, no cookies.
 *  - All unauthenticated requests to protected endpoints return JSON 401
 *    (not Spring Security's default redirect or 403).
 *  - All authenticated-but-unauthorised requests return JSON 403.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired private CustomUserDetailsService userDetailsService;
    @Autowired private JwtAuthFilter            jwtAuthFilter;

    // ── Beans ─────────────────────────────────────────────────────────────────────

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg)
            throws Exception {
        return cfg.getAuthenticationManager();
    }

    // ── Security filter chain ────────────────────────────────────────────────────

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            /*
             * CORS: disabled at service level — the API Gateway is the sole CORS
             * authority (application.yml globalcors). Enabling it here causes the
             * "multiple values" error in the browser.
             */
            .cors(cors -> cors.disable())
            .csrf(csrf -> csrf.disable())

            // Stateless — no HttpSession created or used
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            /*
             * Exception handling:
             *  - AuthenticationEntryPoint → 401 JSON when no/invalid JWT
             *  - AccessDeniedHandler      → 403 JSON when authenticated but lacks role
             */
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> {
                    res.setContentType("application/json");
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.getWriter().write(
                        "{\"success\":false,\"message\":\"Unauthorized — JWT token missing or invalid\"}");
                })
                .accessDeniedHandler((req, res, e) -> {
                    res.setContentType("application/json");
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.getWriter().write(
                        "{\"success\":false,\"message\":\"Forbidden — insufficient permissions\"}");
                })
            )

            .authenticationProvider(authenticationProvider())

            // Public endpoints — no JWT required
            .authorizeHttpRequests(auth -> auth
                // Always allow CORS pre-flight requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Auth endpoints are open — method-specific to prevent bypass
                .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/auth/health").permitAll()
                // Docs & monitoring
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/actuator/**"
                ).permitAll()
                .anyRequest().authenticated()
            )

            // JWT filter runs before Spring Security's username/password filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
