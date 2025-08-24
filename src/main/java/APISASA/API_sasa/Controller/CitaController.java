package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.CitaDTO;
import APISASA.API_sasa.Services.CitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiCitas")
public class CitaController {

    @Autowired
    private CitaService service;

    // ✅ Listar todas
    @GetMapping("/consultar")
    public List<CitaDTO> obtenerTodas() {
        return service.obtenerCitas();
    }

    // ✅ Listar paginado
    @GetMapping("/consultarPaginado")
    public Page<CitaDTO> obtenerPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.obtenerCitas(PageRequest.of(page, size));
    }

    // ✅ Buscar por ID
    @GetMapping("/{id}")
    public CitaDTO obtenerPorId(@PathVariable Long id) {
        return service.obtenerCitaPorId(id);
    }

    // ✅ Crear nueva
    @PostMapping("/registrar")
    public CitaDTO crear(@Valid @RequestBody CitaDTO dto) {
        return service.insertarCita(dto);
    }

    // ✅ Actualizar
    @PutMapping("/{id}")
    public CitaDTO actualizar(@PathVariable Long id, @Valid @RequestBody CitaDTO dto) {
        return service.actualizarCita(id, dto);
    }

    // ✅ Eliminar
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        boolean eliminado = service.eliminarCita(id);
        return eliminado ? "Cita eliminada con éxito" : "No se encontró la cita para eliminar";
    }
}
