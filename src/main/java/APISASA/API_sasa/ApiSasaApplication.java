package APISASA.API_sasa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiSasaApplication {

	public static void main(String[] args) {

		// Las variables de entorno ya están disponibles en Heroku,
		// no es necesario cargar ningún archivo .env ni usar Dotenv.

		// Opcional: puedes verificar que se estén leyendo correctamente
		String dbUrl = System.getenv("ORACLE_CLOUD_BD_URL");
		String dbUser = System.getenv("ORACLE_CLOUD_BD_USER");
		String jwtSecret = System.getenv("SECURITY_JWT_SECRET");

		System.out.println("🔹 ORACLE_CLOUD_BD_URL: " + (dbUrl != null));
		System.out.println("🔹 ORACLE_CLOUD_BD_USER: " + (dbUser != null));
		System.out.println("🔹 SECURITY_JWT_SECRET: " + (jwtSecret != null));

		SpringApplication.run(ApiSasaApplication.class, args);
	}
}
