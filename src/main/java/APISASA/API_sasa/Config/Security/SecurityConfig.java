package APISASA.API_sasa.Config.Security;

import APISASA.API_sasa.Utils.JwtCookieAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtCookieAuthFilter jwtCookieAuthFilter;

    // ========================================
    // 🔒 Password Encoder (Argon2)
    // ========================================
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Parámetros seguros recomendados por OWASP
        int saltLength = 16; // bytes
        int hashLength = 32; // bytes
        int parallelism = 1;
        int memory = 1 << 13; // 8 MB
        int iterations = 3;
        return new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memory, iterations);
    }

    // ========================================
    // 🌐 Configuración CORS
    // ========================================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();

        cfg.setAllowCredentials(true);
        cfg.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*",
                "http://10.0.2.2:*",
                "https://*.vercel.app",
                "https://*.herokuapp.com",
                "*" // fallback global
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With"));
        cfg.setExposedHeaders(List.of("Authorization", "Set-Cookie", "WWW-Authenticate"));
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    // ========================================
    // 🚫 Handler: No Autorizado (401)
    // ========================================
    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, ex) -> {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("""
                {
                  "error": "No autorizado",
                  "status": 401,
                  "path": "%s"
                }
            """.formatted(request.getRequestURI()));
        };
    }

    // ========================================
    // 🚫 Handler: Acceso Denegado (403)
    // ========================================
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("""
                {
                  "error": "Acceso denegado",
                  "status": 403,
                  "path": "%s"
                }
            """.formatted(request.getRequestURI()));
        };
    }

    // ========================================
    // 🛡️ Configuración principal HTTP Security
    // ========================================
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationEntryPoint unauthorizedEntryPoint,
            AccessDeniedHandler accessDeniedHandler
    ) throws Exception {

        http
                // 🚫 Desactivar CSRF (usamos JWT)
                .csrf(csrf -> csrf.disable())

                // 🌍 CORS global
                .cors(Customizer.withDefaults())

                // ⚙️ Stateless (sin sesiones)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ⚠️ Manejo de errores personalizados
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(unauthorizedEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )

                // ✅ Rutas permitidas sin autenticación
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/", "/error", "/favicon.ico",
                                "/auth/**", "/api/auth/**",
                                "/api/public/**",
                                "/apiEmpleados/registrar", // permitir registro de empleados
                                "/swagger-ui/**", "/v3/api-docs/**"
                        ).permitAll()
                        // 🔒 Todo lo demás requiere token JWT
                        .anyRequest().authenticated()
                )

                // 🔑 Filtro JWT antes del filtro de login
                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // 🚫 Sin HTTP Basic ni formularios
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable());

        return http.build();
    }

    // ========================================
    // ⚙️ Authentication Manager (para login)
    // ========================================
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
