// src/main/java/APISASA/API_sasa/Config/SecurityConfig.java
package APISASA.API_sasa.Config;

import APISASA.API_sasa.Utils.JwtCookieAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtCookieAuthFilter jwtCookieAuthFilter;

    // ========= 1) PasswordEncoder ARGON2 (evita que Spring intente BCrypt) =========
    @Bean
    public PasswordEncoder passwordEncoder() {
        // saltLength=16, hashLength=32, parallelism=1, memory=8MB, iterations=3 (ajusta si quieres)
        return new Argon2PasswordEncoder(16, 32, 1, 1 << 13, 3);
    }

    // ========= 2) CORS con credenciales para cookie httpOnly =========
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        // ⚠️ PON AQUÍ EL ORIGEN REAL DE TU FRONT:
        cfg.setAllowedOrigins(List.of(
                "http://localhost:5500",      // Live Server / static
                "http://127.0.0.1:5500",
                "http://localhost:5173",      // Vite
                "http://localhost:3000"       // React/Next
        ));
        cfg.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        cfg.setAllowedHeaders(Arrays.asList("Content-Type","Authorization","X-Requested-With"));
        cfg.setAllowCredentials(true); // ← necesario para cookies
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    // ========= 3) Opcional: Entrypoints para 401/403 “limpios” =========
    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(401);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("No autorizado");
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(403);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("Acceso denegado");
        };
    }

    // ========= Cadena 1: protege SOLO /api/** =========
    @Bean
    @Order(1)
    public SecurityFilterChain apiChain(HttpSecurity http,
                                        AuthenticationEntryPoint unauthorizedEntryPoint,
                                        AccessDeniedHandler accessDeniedHandler) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(unauthorizedEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos:
                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/logout").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                        // OJO con hasRole: internamente espera "ROLE_..." en las autoridades.
                        // Si tu JWT trae "Administrador" / "Cliente" sin prefijo,
                        // usa hasAuthority("Administrador") en lugar de hasRole("Administrador").
                        .requestMatchers("/api/test/admin-only").hasAuthority("Administrador")
                        .requestMatchers("/api/test/cliente-only").hasAuthority("Cliente")
                        // Resto autenticado:
                        .anyRequest().authenticated()
                )
                // Añade tu filtro que lee la cookie "authToken" y setea SecurityContext
                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Opcional: desactivar login form/basic
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable());

        return http.build();
    }

    // ========= Cadena 2: todo lo demás público (recursos estáticos etc.) =========
    @Bean
    @Order(2)
    public SecurityFilterChain othersChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception {
        return c.getAuthenticationManager();
    }
}
