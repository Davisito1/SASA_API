package APISASA.API_sasa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiSasaApplication {

	public static void main(String[] args) {

		// Ya no usamos Dotenv, Heroku maneja las variables de entorno automáticamente.
		// Puedes accederlas con System.getenv("NOMBRE_DE_LA_VARIABLE") desde cualquier clase.

		// Ejemplo opcional de lectura (solo para probar si quieres imprimir en logs):
		// System.out.println("BD_USER: " + System.getenv("ORACLE_CLOUD_BD_USER"));

		SpringApplication.run(ApiSasaApplication.class, args);
	}
}
