package APISASA.API_sasa.Services.Recuperacion;

import APISASA.API_sasa.Entities.Cliente.ClienteEntity;
import APISASA.API_sasa.Entities.Empleado.EmpleadoEntity;
import APISASA.API_sasa.Entities.Usuario.UserEntity;
import APISASA.API_sasa.Repositories.Cliente.ClientRepository;
import APISASA.API_sasa.Repositories.Empleado.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RecuperacionService {

    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final EmpleadoRepository empleadoRepository;

    // Guardar los códigos temporalmente (puedes migrarlo luego a BD)
    private final Map<String, String> codigos = new HashMap<>();

    // --- Paso 1: Enviar código al correo ---
    public ResponseEntity<?> enviarCodigo(String correo) {
        try {
            // Buscar si el correo pertenece a un cliente o empleado
            Optional<ClienteEntity> cliente = clientRepository.findByCorreo(correo);
            Optional<EmpleadoEntity> empleado = empleadoRepository.findByCorreoElectronico(correo);

            if (cliente.isEmpty() && empleado.isEmpty()) {
                return ResponseEntity.badRequest().body("No existe una cuenta registrada con ese correo.");
            }

            // Generar código de 6 dígitos
            String codigo = String.format("%06d", new Random().nextInt(999999));
            codigos.put(correo, codigo);

            // Enviar correo con el código
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(correo);
            mensaje.setSubject("Código de recuperación de contraseña - SASA");
            mensaje.setText("Tu código de verificación es: " + codigo + "\n\nNo compartas este código con nadie.");

            mailSender.send(mensaje);

            return ResponseEntity.ok("Código enviado correctamente al correo: " + correo);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al enviar el correo: " + e.getMessage());
        }
    }

    // --- Paso 2: Verificar código ---
    public ResponseEntity<?> verificarCodigo(String correo, String codigo) {
        String codigoGuardado = codigos.get(correo);

        if (codigoGuardado == null || !codigoGuardado.equals(codigo)) {
            return ResponseEntity.badRequest().body("Código inválido o expirado.");
        }

        return ResponseEntity.ok("Código verificado correctamente.");
    }

    // --- Paso 3: Restablecer contraseña ---
    public ResponseEntity<?> restablecerContrasena(String correo, String nuevaContrasena) {
        try {
            Optional<ClienteEntity> cliente = clientRepository.findByCorreo(correo);
            Optional<EmpleadoEntity> empleado = empleadoRepository.findByCorreoElectronico(correo);

            if (cliente.isPresent()) {
                // 🔐 Cliente: actualiza su propia contraseña
                ClienteEntity c = cliente.get();
                c.setContrasena(passwordEncoder.encode(nuevaContrasena));
                clientRepository.save(c);
            }
            else if (empleado.isPresent()) {
                // 🔐 Empleado: obtiene su usuario y actualiza la contraseña del usuario asociado
                EmpleadoEntity e = empleado.get();
                UserEntity usuario = e.getUsuario(); // relación OneToOne

                if (usuario == null) {
                    return ResponseEntity.badRequest().body("No se encontró usuario asociado al empleado.");
                }

                usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
                // Al guardar el empleado, se actualiza el usuario porque está cascadeado
                empleadoRepository.save(e);
            }
            else {
                return ResponseEntity.badRequest().body("No se encontró una cuenta con ese correo.");
            }

            codigos.remove(correo); // limpiar código usado
            return ResponseEntity.ok("Contraseña actualizada correctamente.");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al restablecer la contraseña: " + e.getMessage());
        }
    }
}
