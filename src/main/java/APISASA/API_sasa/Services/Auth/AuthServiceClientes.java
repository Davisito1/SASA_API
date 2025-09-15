package APISASA.API_sasa.Services.Auth;

import APISASA.API_sasa.Entities.Cliente.ClienteEntity;
import APISASA.API_sasa.Repositories.Cliente.ClientRepository;
import APISASA.API_sasa.Config.Argon2.Argon2Password;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceClientes {

    private final ClientRepository clienteRepo;
    private final Argon2Password argon2Password;

    // 🔹 Registro con contraseña cifrada
    public ClienteEntity registrar(ClienteEntity cliente) {
        if (cliente.getContrasena() == null || cliente.getContrasena().isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula ni vacía");
        }

        // Cifrar antes de guardar
        cliente.setContrasena(argon2Password.EncryptPassword(cliente.getContrasena()));
        return clienteRepo.save(cliente);
    }

    // 🔹 Autenticación (login)
    public Optional<ClienteEntity> authenticate(String correo, String contrasena) {
        if (correo == null || correo.isBlank() || contrasena == null || contrasena.isBlank()) {
            return Optional.empty();
        }

        Optional<ClienteEntity> clienteOpt = clienteRepo.findByCorreo(correo);

        if (clienteOpt.isEmpty()) return Optional.empty();

        ClienteEntity cliente = clienteOpt.get();

        String hashed = cliente.getContrasena();
        if (hashed == null || hashed.isBlank()) {
            return Optional.empty();
        }

        // Verificar contraseña (hash vs plain)
        if (argon2Password.VerifyPassword(hashed, contrasena)) {
            return Optional.of(cliente);
        } else {
            return Optional.empty();
        }
    }
}
