package APISASA.API_sasa.Services.DetalleMantenimiento;

import APISASA.API_sasa.Entities.DetalleMantenimiento.DetalleMantenimientoEntity;
import APISASA.API_sasa.Entities.Mantenimiento.MantenimientoEntity;
import APISASA.API_sasa.Entities.Servicio.ServicioEntity;
import APISASA.API_sasa.Entities.TipoMantenimiento.TipoMantenimientoEntity;
import APISASA.API_sasa.Exceptions.ExceptionDetalleNoEncontrado;
import APISASA.API_sasa.Models.DTO.DetalleMantenimiento.DetalleMantenimientoDTO;
import APISASA.API_sasa.Repositories.DetalleMantenimiento.DetalleMantenimientoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetalleMantenimientoService {

    @Autowired
    private DetalleMantenimientoRepository repo;

    @PersistenceContext
    private EntityManager em;

    // ✅ Consultar todos
    public List<DetalleMantenimientoDTO> obtenerDetalles() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ✅ Consultar por idMantenimiento
    public List<DetalleMantenimientoDTO> obtenerPorMantenimiento(Long idMantenimiento) {
        return repo.findByMantenimiento_Id(idMantenimiento).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ✅ Consultar con paginación + búsqueda
    public Page<DetalleMantenimientoDTO> obtenerDetallesPaginado(String q, Pageable pageable) {
        if (q == null || q.isBlank()) {
            return repo.findAll(pageable).map(this::convertirADTO);
        }
        String term = q.trim();
        try {
            Long id = Long.parseLong(term);
            // 🔹 Busca por coincidencia de cualquiera de los 3 IDs
            return repo.findByMantenimiento_IdOrServicio_IdServicioOrTipoMantenimiento_IdTipoMantenimiento(
                    id, id, id, pageable
            ).map(this::convertirADTO);
        } catch (NumberFormatException ignore) {
            // 🔹 Busca por estado
            return repo.findByEstadoContainingIgnoreCase(term, pageable)
                    .map(this::convertirADTO);
        }
    }

    // ✅ Insertar nuevo detalle
    public DetalleMantenimientoDTO insertarDetalle(DetalleMantenimientoDTO dto) {
        DetalleMantenimientoEntity entity = convertirAEntity(dto);
        return convertirADTO(repo.save(entity));
    }

    // ✅ Actualizar detalle
    public DetalleMantenimientoDTO actualizarDetalle(Long id, DetalleMantenimientoDTO dto) {
        DetalleMantenimientoEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionDetalleNoEncontrado("No existe un detalle con ID: " + id));

        existente.setEstado(dto.getEstado());
        existente.setMantenimiento(em.getReference(MantenimientoEntity.class, dto.getIdMantenimiento()));
        existente.setServicio(em.getReference(ServicioEntity.class, dto.getIdServicio()));
        existente.setTipoMantenimiento(em.getReference(TipoMantenimientoEntity.class, dto.getIdTipoMantenimiento()));

        return convertirADTO(repo.save(existente));
    }

    // ================= Conversores =================
    private DetalleMantenimientoDTO convertirADTO(DetalleMantenimientoEntity entity) {
        DetalleMantenimientoDTO dto = new DetalleMantenimientoDTO();
        dto.setId(entity.getIdDetalleMantenimiento());
        dto.setEstado(entity.getEstado());
        dto.setIdMantenimiento(entity.getMantenimiento().getId()); // ✅ ahora correcto
        dto.setIdServicio(entity.getServicio().getIdServicio());   // ✅ correcto
        dto.setIdTipoMantenimiento(entity.getTipoMantenimiento().getIdTipoMantenimiento()); // ✅ correcto
        return dto;
    }

    private DetalleMantenimientoEntity convertirAEntity(DetalleMantenimientoDTO dto) {
        DetalleMantenimientoEntity entity = new DetalleMantenimientoEntity();
        entity.setEstado(dto.getEstado());
        entity.setMantenimiento(em.getReference(MantenimientoEntity.class, dto.getIdMantenimiento()));
        entity.setServicio(em.getReference(ServicioEntity.class, dto.getIdServicio()));
        entity.setTipoMantenimiento(em.getReference(TipoMantenimientoEntity.class, dto.getIdTipoMantenimiento()));
        return entity;
    }
}
