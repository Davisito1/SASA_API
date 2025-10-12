package APISASA.API_sasa.Controller.Recuperacion;

import APISASA.API_sasa.Services.Recuperacion.RecuperacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recuperacion")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ControllerRecuperacion {

    private final RecuperacionService recuperacionService;

    // --- Paso 1: Enviar código al correo ---
    @PostMapping("/enviar")
    public ResponseEntity<?> enviarCodigo(@RequestParam String correo) {
        return recuperacionService.enviarCodigo(correo);
    }

    // --- Paso 2: Verificar el código recibido ---
    @PostMapping("/verificar")
    public ResponseEntity<?> verificarCodigo(@RequestParam String correo, @RequestParam String codigo) {
        return recuperacionService.verificarCodigo(correo, codigo);
    }

    // --- Paso 3: Restablecer la contraseña ---
    @PostMapping("/restablecer")
    public ResponseEntity<?> restablecerContrasena(
            @RequestParam String correo,
            @RequestParam String nuevaContrasena) {
        return recuperacionService.restablecerContrasena(correo, nuevaContrasena);
    }
}
