package APISASA.API_sasa.Services.Auth;

import APISASA.API_sasa.Entities.Usuario.UserEntity;
import APISASA.API_sasa.Repositories.Usuario.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    // Comparador mágico de contraseñas
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

        // 2. Si existe, comprobar contraseña
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();

            // La contraseña en BD está guardada con hash
            if (passwordEncoder.matches(contrasena, user.getContrasena())) {
                return userOpt;
            }
        }

        return Optional.empty(); //No existe o contraseña incorrecta
    }

    /**
     * Obtener un usuario por correo (útil para el AuthController al generar el token)
     */
    public Optional<UserEntity> obtenerUsuario(String correo) {
        return repo.findByCorreo(correo);
    }
}
