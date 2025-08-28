package APISASA.API_sasa.Services.Pagos;

import APISASA.API_sasa.Entities.Pagos.PagosEntity;
import APISASA.API_sasa.Entities.Factura.FacturaEntity;
import APISASA.API_sasa.Entities.MetodoPago.MetodoPagoEntity;
import APISASA.API_sasa.Exceptions.ExceptionPagoNoEncontrado;
import APISASA.API_sasa.Models.DTO.Pagos.PagosDTO;
import APISASA.API_sasa.Repositories.Pagos.PagosRepository;
import APISASA.API_sasa.Repositories.Factura.FacturaRepository;
import APISASA.API_sasa.Repositories.MetodoPago.MetodoPagoRepository;
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

    @Autowired
    private FacturaRepository facturaRepo;

    @Autowired
    private MetodoPagoRepository metodoPagoRepo;

    //  Obtener todos los pagos
    public List<PagosDTO> obtenerPagos() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    //  Insertar nuevo pago
    public PagosDTO insertarPago(PagosDTO dto) {
        PagosEntity entity = convertirAEntity(dto);
        entity.setIdPago(null); // Oracle maneja el ID con la secuencia
        PagosEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    //  Actualizar pago
    public PagosDTO actualizarPago(Long id, PagosDTO dto) {
        PagosEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionPagoNoEncontrado("No se encontró pago con ID: " + id));

        existente.setFecha(dto.getFecha());
        existente.setMonto(dto.getMonto());

        // Relación con factura
        if (dto.getIdFactura() != null) {
            FacturaEntity factura = facturaRepo.findById(dto.getIdFactura())
                    .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + dto.getIdFactura()));
            existente.setFactura(factura);
        }

        // Relación con método de pago
        if (dto.getIdMetodoPago() != null) {
            MetodoPagoEntity metodo = metodoPagoRepo.findById(dto.getIdMetodoPago())
                    .orElseThrow(() -> new RuntimeException("Método de pago no encontrado con ID: " + dto.getIdMetodoPago()));
            existente.setMetodoPago(metodo);
        }

        PagosEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    //  Eliminar pago
    public boolean eliminarPago(Long id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            }
            return false;
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionPagoNoEncontrado("No se encontró pago con ID: " + id + " para eliminar.");
        }
    }

    // ==========================
    // 🔹 Conversores
    // ==========================
    private PagosDTO convertirADTO(PagosEntity entity) {
        PagosDTO dto = new PagosDTO();
        dto.setId(entity.getIdPago());
        dto.setFecha(entity.getFecha());
        dto.setMonto(entity.getMonto());

        if (entity.getFactura() != null) {
            dto.setIdFactura(entity.getFactura().getIdFactura());
        }
        if (entity.getMetodoPago() != null) {
            dto.setIdMetodoPago(entity.getMetodoPago().getIdMetodoPago()); // 👈 ahora coincide con tu DTO
        }

        return dto;
    }

    private PagosEntity convertirAEntity(PagosDTO dto) {
        PagosEntity entity = new PagosEntity();
        entity.setFecha(dto.getFecha());
        entity.setMonto(dto.getMonto());

        if (dto.getIdFactura() != null) {
            FacturaEntity factura = facturaRepo.findById(dto.getIdFactura())
                    .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + dto.getIdFactura()));
            entity.setFactura(factura);
        }

        if (dto.getIdMetodoPago() != null) {
            MetodoPagoEntity metodo = metodoPagoRepo.findById(dto.getIdMetodoPago())
                    .orElseThrow(() -> new RuntimeException("Método de pago no encontrado con ID: " + dto.getIdMetodoPago()));
            entity.setMetodoPago(metodo);
        }

        return entity;
    }
}
