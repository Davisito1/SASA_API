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

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtCookieAuthFilter jwtCookieAuthFilter;

    // =======================
    // 1) PasswordEncoder
    // =======================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(16, 32, 1, 1 << 13, 3);
    }

    // =======================
    // 2) Configuración de CORS
    // =======================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowCredentials(true);

        // 🔥 Permitimos localhost (front local) + emulador Android + pruebas generales
        cfg.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*",
                "http://10.0.2.2:*",
                "*" // ⚠️ permite cualquier origen temporalmente (útil para pruebas en Heroku)
        ));

        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        cfg.setAllowedHeaders(List.of("Origin","Content-Type","Accept","Authorization","X-Requested-With","*"));
        cfg.setExposedHeaders(List.of("Authorization","Set-Cookie","WWW-Authenticate"));
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    // =======================
    // 3) Handlers de errores (401 / 403)
    // =======================
    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, ex) -> {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"No autorizado\",\"status\":401}");
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"Acceso denegado\",\"status\":403}");
        };
    }

    // =======================
    // 4) Cadena de seguridad (filtros y permisos)
    // =======================
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationEntryPoint unauthorizedEntryPoint,
            AccessDeniedHandler accessDeniedHandler
    ) throws Exception {

        http
                // 🔒 Desactiva CSRF porque usamos JWT (no sesiones)
                .csrf(csrf -> csrf.disable())

                // 🌐 Habilita CORS con la config anterior
                .cors(Customizer.withDefaults())

                // 🚫 Sin sesiones (stateless)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ⚠️ Manejo de excepciones JWT
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(unauthorizedEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )

                // 🔐 Reglas de acceso
                .authorizeHttpRequests(auth -> auth
                        // Preflight de CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Endpoints públicos (login, registro, swagger, etc.)
                        .requestMatchers(
                                "/auth/**",
                                "/api/auth/**",
                                "/api/public/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )

                // 🔎 Filtro JWT personalizado (antes del UsernamePasswordAuthenticationFilter)
                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Desactivar login HTTP básico y formulario
                .httpBasic(h -> h.disable())
                .formLogin(f -> f.disable());

        return http.build();
    }

    // =======================
    // 5) AuthenticationManager
    // =======================
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
