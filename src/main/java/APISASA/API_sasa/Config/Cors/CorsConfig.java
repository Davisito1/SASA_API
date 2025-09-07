// src/main/java/APISASA/API_sasa/Config/Cors/CorsConfig.java
package APISASA.API_sasa.Config.Cors;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowCredentials(true);

        // Usa ORÍGENES EXACTOS o patrones. Elige una de las dos opciones:

        // Opción A: lista exacta (recomendada si ya sabes tus puertos):
        cfg.setAllowedOrigins(List.of(
                "http://127.0.0.7:5501",  // Tu Live Server
                "http://localhost:5500",
                "http://localhost:5501",
                "http://localhost:5173",  // Vite
                "http://localhost:4200",  // Angular
                "http://localhost:3000"   // React
        ));

        // Opción B (alternativa): patrones (útil si cambias de puerto seguido)
        // cfg.setAllowedOriginPatterns(List.of(
        //     "http://localhost:*",
        //     "http://127.0.0.*:*"
        // ));

        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        cfg.setAllowedHeaders(List.of("Origin","Content-Type","Accept","Authorization"));
        cfg.setExposedHeaders(List.of("Authorization"));
        cfg.setMaxAge(3600L); // cachea preflight 1h

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // ¡clave! que corra antes que todo
        return bean;
    }
}
