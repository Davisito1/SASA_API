package APISASA.API_sasa.Config.App;

import APISASA.API_sasa.Utils.JWTUtils;
import APISASA.API_sasa.Utils.JwtCookieAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public JwtCookieAuthFilter jwtCookieAuthFilter (JWTUtils jwtUtils){
        return  new JwtCookieAuthFilter(jwtUtils);
    }

}
