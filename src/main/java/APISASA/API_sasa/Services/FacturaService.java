package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.EmpleadoEntity;
import APISASA.API_sasa.Entities.FacturaEntity;
import APISASA.API_sasa.Entities.MantenimientoEntity;
import APISASA.API_sasa.Exceptions.ExceptionFacturaNoEncontrada;
import APISASA.API_sasa.Models.DTO.FacturaDTO;
import APISASA.API_sasa.Repositories.EmpleadoRepository;
import APISASA.API_sasa.Repositories.FacturaRepository;
import APISASA.API_sasa.Repositories.MantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository repo;

    @Autowired
    private EmpleadoRepository empleadoRepo;

    @Autowired
    private MantenimientoRepository mantenimientoRepo;

    // ✅ Obtener facturas paginadas
    public Page<FacturaDTO> obtenerFacturas(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FacturaEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    // ✅ Insertar nueva factura
    public FacturaDTO insertarFactura(FacturaDTO dto) {
        FacturaEntity entity = convertirAEntity(dto);
        entity.setIdFactura(null); // dejar que Oracle maneje el ID con la secuencia
        FacturaEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    // ✅ Actualizar factura
    public FacturaDTO actualizarFactura(Long id, FacturaDTO dto) {
        FacturaEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionFacturaNoEncontrada("No existe una factura con ID: " + id));

        existente.setFecha(dto.getFecha());
        existente.setMontoTotal(dto.getMontoTotal());

        if (dto.getIdEmpleado() != null) {
            EmpleadoEntity emp = empleadoRepo.findById(dto.getIdEmpleado())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + dto.getIdEmpleado()));
            existente.setEmpleado(emp);
        }

        if (dto.getIdMantenimiento() != null) {
            MantenimientoEntity mant = mantenimientoRepo.findById(dto.getIdMantenimiento())
                    .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado con ID: " + dto.getIdMantenimiento()));
            existente.setMantenimiento(mant);
        }

        FacturaEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    // ✅ Eliminar factura
    public boolean eliminarFactura(Long id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            }
            return false;
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionFacturaNoEncontrada("No se encontró la factura con ID: " + id + " para eliminar.");
        }
    }

    // ==========================
    // 🔹 Conversores
    // ==========================
    private FacturaDTO convertirADTO(FacturaEntity entity) {
        FacturaDTO dto = new FacturaDTO();
        dto.setId(entity.getIdFactura()); // 👈 usar el nombre real de tu PK
        dto.setFecha(entity.getFecha());
        dto.setMontoTotal(entity.getMontoTotal());

        if (entity.getEmpleado() != null) {
            dto.setIdEmpleado(entity.getEmpleado().getIdEmpleado());
        }
        if (entity.getMantenimiento() != null) {
            dto.setIdMantenimiento(entity.getMantenimiento().getId());
        }

        return dto;
    }

    private FacturaEntity convertirAEntity(FacturaDTO dto) {
        FacturaEntity entity = new FacturaEntity();
        entity.setFecha(dto.getFecha());
        entity.setMontoTotal(dto.getMontoTotal());

        if (dto.getIdEmpleado() != null) {
            EmpleadoEntity emp = empleadoRepo.findById(dto.getIdEmpleado())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + dto.getIdEmpleado()));
            entity.setEmpleado(emp);
        }

        if (dto.getIdMantenimiento() != null) {
            MantenimientoEntity mant = mantenimientoRepo.findById(dto.getIdMantenimiento())
                    .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado con ID: " + dto.getIdMantenimiento()));
            entity.setMantenimiento(mant);
        }

        return entity;
    }

    public FacturaDTO obtenerFacturaPorId(Long id) {
        FacturaEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionFacturaNoEncontrada("Factura con ID " + id + " no encontrada"));

        return convertirADTO(entity);
    }
}
