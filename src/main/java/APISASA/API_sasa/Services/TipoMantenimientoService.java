package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.TipoMantenimientoEntity;
import APISASA.API_sasa.Models.DTO.TipoMantenimientoDTO;
import APISASA.API_sasa.Repositories.TipoMantenimientoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TipoMantenimientoService {

    @Autowired
    private TipoMantenimientoRepository repo;

    // ✅ Obtener todos los tipos de mantenimiento (catálogo fijo)
    public List<TipoMantenimientoDTO> obtenerTipos() {
        List<TipoMantenimientoEntity> datos = repo.findAll();
        return datos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ✅ Insertar nuevo tipo (opcional)
    public TipoMantenimientoDTO insertarTipo(TipoMantenimientoDTO dto) {
        TipoMantenimientoEntity entity = convertirAEntity(dto);
        entity.setIdTipoMantenimiento(null); // dejar que Oracle maneje el ID con la secuencia
        TipoMantenimientoEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    // ✅ Actualizar tipo existente
    public TipoMantenimientoDTO actualizarTipo(Long id, TipoMantenimientoDTO dto) {
        TipoMantenimientoEntity existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró TipoMantenimiento con ID: " + id));

        existente.setTipoMantenimiento(dto.getTipoMantenimiento());
        return convertirADTO(repo.save(existente));
    }

    // ✅ Eliminar tipo
    public boolean eliminarTipo(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    // ==========================
    // 🔹 Conversores
    // ==========================
    private TipoMantenimientoDTO convertirADTO(TipoMantenimientoEntity entity) {
        TipoMantenimientoDTO dto = new TipoMantenimientoDTO();
        dto.setId(entity.getIdTipoMantenimiento());  // 👈 usar el nombre real del campo en tu entity
        dto.setTipoMantenimiento(entity.getTipoMantenimiento());
        return dto;
    }

    private TipoMantenimientoEntity convertirAEntity(TipoMantenimientoDTO dto) {
        TipoMantenimientoEntity entity = new TipoMantenimientoEntity();
        entity.setIdTipoMantenimiento(dto.getId());  // 👈 usar el nombre real del campo en tu entity
        entity.setTipoMantenimiento(dto.getTipoMantenimiento());
        return entity;
    }
}
