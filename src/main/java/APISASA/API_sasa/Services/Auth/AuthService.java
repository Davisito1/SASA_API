package APISASA.API_sasa.Services.Auth;

import APISASA.API_sasa.Config.Argon2.Argon2Password;
import APISASA.API_sasa.Entities.Usuario.UserEntity;
import APISASA.API_sasa.Repositories.Usuario.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repo;
    private final Argon2Password argon2; // inyecta tu servicio de Argon2

    /**
     * Autenticar: busca al usuario por correo o nombreUsuario
     * y valida que la contraseña sea correcta.
     */
    public Optional<UserEntity> authenticate(String identificador, String contrasena) {
        // 1. Buscar usuario por correo o nombreUsuario
        Optional<UserEntity> userOpt = repo.findByCorreo(identificador);
        if (userOpt.isEmpty()) {
            userOpt = repo.findByNombreUsuario(identificador);
        }

        // 2. Si existe, comprobar contraseña con Argon2
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();

            if (argon2.VerifyPassword(user.getContrasena(), contrasena)) {
                return userOpt;
            }
        }

        return Optional.empty(); // No existe o contraseña incorrecta
    }

    /**
     * Obtener un usuario por correo (útil para el AuthController al generar el token)
     */
    public Optional<UserEntity> obtenerUsuario(String correo) {
        return repo.findByCorreo(correo);
    }
}
