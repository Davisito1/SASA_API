package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.EmpleadoEntity;
import APISASA.API_sasa.Entities.PagosEntity;
import APISASA.API_sasa.Exceptions.ExceptionEmpleadoNoEncontrado;
import APISASA.API_sasa.Exceptions.ExceptionPagoNoEncontrado;
import APISASA.API_sasa.Models.DTO.EmpleadoDTO;
import APISASA.API_sasa.Models.DTO.PagosDTO;
import APISASA.API_sasa.Repositories.FacturaRepository;
import APISASA.API_sasa.Repositories.MetodoPagoRepository;
import APISASA.API_sasa.Repositories.PagosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PagosServices {
    @Autowired
    private PagosRepository repo;
    private MetodoPagoRepository repoMetodo;
    private FacturaRepository repoFactura;

    public List<PagosDTO> obtenerPagos() {
        List<PagosEntity> datos = repo.findAll();
        return datos.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public PagosDTO insertarPago(PagosDTO data) {
        PagosEntity entity = convertirAEntity(data);
        PagosEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    public PagosDTO actualizarPago(Long id, PagosDTO data) {
        PagosEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionPagoNoEncontrado("No se encontró pago con ID: " + id));

        existente.setFecha(data.getFecha());
        existente.setMonto(data.getMonto());
        if (data.getMetodoPago() != null) existente.setIdMetodoPago(repoMetodo.getReferenceById(data.getMetodoPago()));
        if (data.getIdFactura() != null) existente.setIdFactura(repoFactura.getReferenceById(data.getIdFactura()));

        PagosEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    public boolean eliminarPago(Long id) {
        try {
            PagosEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionEmpleadoNoEncontrado("No se encontró pago con ID: " + id + " para eliminar.");
        }
    }

    private PagosDTO convertirADTO(PagosEntity entity) {
        PagosDTO dto = new PagosDTO();
        dto.setId(entity.getId());
        dto.setFecha(entity.getFecha());
        dto.setMonto(entity.getMonto());
        if (entity.getIdMetodoPago() != null) dto.setMetodoPago(entity.getIdMetodoPago().getId());
        if (entity.getIdFactura() != null) dto.setIdFactura(entity.getIdFactura().getId());
        return dto;
    }

    private PagosEntity convertirAEntity(PagosDTO dto) {
        PagosEntity entity = new PagosEntity();
        entity.setId(dto.getId());
        entity.setFecha(dto.getFecha());
        entity.setMonto(dto.getMonto());
        if (dto.getMetodoPago() != null) entity.setIdMetodoPago(repoMetodo.getReferenceById(dto.getMetodoPago()));
        if (dto.getIdFactura() != null) entity.setIdFactura(repoFactura.getReferenceById(dto.getIdFactura()));
        return entity;
    }
}
