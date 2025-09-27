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
    private final Argon2Password argon2; // servicio para encriptar/verificar contraseñas

    /**
     * Registrar un nuevo usuario
     */
    public UserEntity register(UserEntity user) {
        // Verificar si ya existe usuario con mismo nombre o correo
        if (repo.findByNombreUsuario(user.getNombreUsuario()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        if (user.getCorreo() != null && repo.findByCorreo(user.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // Encriptar contraseña antes de guardar
        String hash = argon2.EncryptPassword(user.getContrasena());
        user.setContrasena(hash);

        // Estado y rol por defecto si no vienen
        if (user.getEstado() == null) {
            user.setEstado("ACTIVO");
        }
        if (user.getRol() == null) {
            user.setRol("EMPLEADO"); // por defecto empleados
        }

        return repo.save(user);
    }

    /**
     * Autenticar usuario (login)
     * Puede usar nombreUsuario o correo como identificador
     */
    public Optional<UserEntity> authenticate(String identificador, String contrasena) {
        // Buscar por correo o nombreUsuario
        Optional<UserEntity> userOpt = repo.findByCorreo(identificador);
        if (userOpt.isEmpty()) {
            userOpt = repo.findByNombreUsuario(identificador);
        }

        // Verificar contraseña si existe el usuario
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            if (argon2.VerifyPassword(user.getContrasena(), contrasena)) {
                return userOpt;
            }
        }

        return Optional.empty(); // usuario no encontrado o contraseña incorrecta
    }

    /**
     * Obtener usuario por correo (para uso en generación de tokens)
     */
    public Optional<UserEntity> obtenerUsuario(String correo) {
        return repo.findByCorreo(correo);
    }
}
