package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.FacturaEntity;
import APISASA.API_sasa.Exceptions.ExceptionFacturaNoEncontrada;
import APISASA.API_sasa.Models.DTO.FacturaDTO;
import APISASA.API_sasa.Repositories.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository repo;

    public List<FacturaDTO> obtenerFacturas() {
        List<FacturaEntity> lista = repo.findAll();
        return lista.stream().map(this::convertirADTO).collect(Collectors.toList());
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
        existente.setIdEmpleado(dto.getIdEmpleado());
        existente.setIdMantenimiento(dto.getIdMantenimiento());

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
        dto.setIdEmpleado(entity.getIdEmpleado());
        dto.setIdMantenimiento(entity.getIdMantenimiento());
        return dto;
    }

    private FacturaEntity convertirAEntity(FacturaDTO dto) {
        FacturaEntity entity = new FacturaEntity();
        entity.setFecha(dto.getFecha());
        entity.setMontoTotal(dto.getMontoTotal());
        entity.setIdEmpleado(dto.getIdEmpleado());
        entity.setIdMantenimiento(dto.getIdMantenimiento());
        return entity;
    }
}
