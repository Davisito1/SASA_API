package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.ClienteEntity;
import APISASA.API_sasa.Entities.FacturaEntity;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository repo;
    private EmpleadoRepository repoEmpleado;
    private MantenimientoRepository repoMante;

    public Page<FacturaDTO> obtenerFacturas(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FacturaEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    public FacturaDTO insertarFactura(FacturaDTO dto) {
        FacturaEntity entity = convertirAEntity(dto);
        FacturaEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    public FacturaDTO actualizarFactura(Long id, FacturaDTO dto) {
        FacturaEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionFacturaNoEncontrada("No existe una factura con ID: " + id));

        existente.setFecha(dto.getFecha());
        existente.setMontoTotal(dto.getMontoTotal());
        if (dto.getIdEmpleado() != null) existente.setIdEmpleado(repoEmpleado.getReferenceById(dto.getIdEmpleado()));
        if (dto.getIdMantenimiento() != null) existente.setIdMantenimiento(repoMante.getReferenceById(dto.getIdMantenimiento()));

        FacturaEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    public boolean eliminarFactura(Long id) {
        try {
            FacturaEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionFacturaNoEncontrada("No se encontró la factura con ID: " + id + " para eliminar.");
        }
    }

    private FacturaDTO convertirADTO(FacturaEntity entity) {
        FacturaDTO dto = new FacturaDTO();
        dto.setId(entity.getId());
        dto.setFecha(entity.getFecha());
        dto.setMontoTotal(entity.getMontoTotal());
        if (entity.getIdEmpleado() != null) dto.setIdEmpleado(entity.getIdEmpleado().getId());
        if (entity.getIdMantenimiento() != null) dto.setIdMantenimiento(entity.getIdMantenimiento().getId());
        return dto;
    }

    private FacturaEntity convertirAEntity(FacturaDTO dto) {
        FacturaEntity entity = new FacturaEntity();
        entity.setFecha(dto.getFecha());
        entity.setMontoTotal(dto.getMontoTotal());
        if (dto.getIdEmpleado() != null) entity.setIdEmpleado(repoEmpleado.getReferenceById(dto.getIdEmpleado()));
        if (dto.getIdMantenimiento() != null) entity.setIdMantenimiento(repoMante.getReferenceById(dto.getIdMantenimiento()));
        return entity;
    }
}
