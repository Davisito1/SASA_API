package APISASA.API_sasa.Config.App;

import APISASA.API_sasa.Utils.JWTUtils;
import APISASA.API_sasa.Utils.JwtCookieAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
    @Bean
    public JwtCookieAuthFilter jwtCookieAuthFilter (JWTUtils jwtUtils){
        return  new JwtCookieAuthFilter(jwtUtils);
    }

}

