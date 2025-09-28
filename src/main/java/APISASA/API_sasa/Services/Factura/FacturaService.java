package APISASA.API_sasa.Services.Factura;

import APISASA.API_sasa.Entities.Empleado.EmpleadoEntity;
import APISASA.API_sasa.Entities.Factura.FacturaEntity;
import APISASA.API_sasa.Entities.MetodoPago.MetodoPagoEntity;
import APISASA.API_sasa.Entities.OrdenTrabajo.OrdenTrabajoEntity;
import APISASA.API_sasa.Exceptions.ExceptionFacturaNoEncontrada;
import APISASA.API_sasa.Models.DTO.Factura.FacturaDTO;
import APISASA.API_sasa.Repositories.Empleado.EmpleadoRepository;
import APISASA.API_sasa.Repositories.Factura.FacturaRepository;
import APISASA.API_sasa.Repositories.MetodoPago.MetodoPagoRepository;
import APISASA.API_sasa.Repositories.OrdenTrabajo.OrdenTrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private MetodoPagoRepository metodoPagoRepo;

    @Autowired
    private OrdenTrabajoRepository ordenRepo;

    // =======================
    // Obtener facturas paginadas
    // =======================
    public Page<FacturaDTO> obtenerFacturas(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FacturaEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    // =======================
    // Insertar nueva factura
    // =======================
    public FacturaDTO insertarFactura(FacturaDTO dto) {
        FacturaEntity entity = convertirAEntity(dto);
        entity.setIdFactura(null); // dejar que Oracle maneje la secuencia
        FacturaEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    // =======================
    // Actualizar factura
    // =======================
    public FacturaDTO actualizarFactura(Long id, FacturaDTO dto) {
        FacturaEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionFacturaNoEncontrada("No existe una factura con ID: " + id));

        existente.setFecha(dto.getFecha());
        existente.setMontoTotal(dto.getMontoTotal());
        existente.setEstado(dto.getEstado());
        existente.setDescripcion(dto.getDescripcion());
        existente.setReferenciaPago(dto.getReferenciaPago());

        if (dto.getIdEmpleado() != null) {
            EmpleadoEntity emp = empleadoRepo.findById(dto.getIdEmpleado())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + dto.getIdEmpleado()));
            existente.setEmpleado(emp);
        }

        if (dto.getIdMetodoPago() != null) {
            MetodoPagoEntity metodo = metodoPagoRepo.findById(dto.getIdMetodoPago())
                    .orElseThrow(() -> new RuntimeException("Método de pago no encontrado con ID: " + dto.getIdMetodoPago()));
            existente.setMetodoPago(metodo);
        }

        if (dto.getIdOrden() != null) {
            OrdenTrabajoEntity orden = ordenRepo.findById(dto.getIdOrden())
                    .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + dto.getIdOrden()));
            existente.setOrdenTrabajo(orden);
        }

        FacturaEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    // =======================
    // Convertir Entity → DTO
    // =======================
    private FacturaDTO convertirADTO(FacturaEntity entity) {
        FacturaDTO dto = new FacturaDTO();
        dto.setId(entity.getIdFactura());
        dto.setFecha(entity.getFecha());
        dto.setMontoTotal(entity.getMontoTotal());
        dto.setEstado(entity.getEstado());
        dto.setDescripcion(entity.getDescripcion());
        dto.setReferenciaPago(entity.getReferenciaPago());

        if (entity.getEmpleado() != null) {
            dto.setIdEmpleado(entity.getEmpleado().getIdEmpleado());
        }
        if (entity.getMetodoPago() != null) {
            dto.setIdMetodoPago(entity.getMetodoPago().getIdMetodoPago());
        }
        if (entity.getOrdenTrabajo() != null) {
            dto.setIdOrden(entity.getOrdenTrabajo().getIdOrden());
        }

        return dto;
    }

    // =======================
    // Convertir DTO → Entity
    // =======================
    private FacturaEntity convertirAEntity(FacturaDTO dto) {
        FacturaEntity entity = new FacturaEntity();
        entity.setFecha(dto.getFecha());
        entity.setMontoTotal(dto.getMontoTotal());
        entity.setEstado(dto.getEstado());
        entity.setDescripcion(dto.getDescripcion());
        entity.setReferenciaPago(dto.getReferenciaPago());

        if (dto.getIdEmpleado() != null) {
            EmpleadoEntity emp = empleadoRepo.findById(dto.getIdEmpleado())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + dto.getIdEmpleado()));
            entity.setEmpleado(emp);
        }

        if (dto.getIdMetodoPago() != null) {
            MetodoPagoEntity metodo = metodoPagoRepo.findById(dto.getIdMetodoPago())
                    .orElseThrow(() -> new RuntimeException("Método de pago no encontrado con ID: " + dto.getIdMetodoPago()));
            entity.setMetodoPago(metodo);
        }

        if (dto.getIdOrden() != null) {
            OrdenTrabajoEntity orden = ordenRepo.findById(dto.getIdOrden())
                    .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + dto.getIdOrden()));
            entity.setOrdenTrabajo(orden);
        }

        return entity;
    }

    // =======================
    // Obtener por ID
    // =======================
    public FacturaDTO obtenerFacturaPorId(Long id) {
        return repo.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new ExceptionFacturaNoEncontrada("No existe una factura con ID: " + id));
    }

    // =======================
    // Anular factura
    // =======================
    public FacturaDTO anularFactura(Long id) {
        FacturaEntity factura = repo.findById(id)
                .orElseThrow(() -> new ExceptionFacturaNoEncontrada("Factura no encontrada con ID " + id));

        factura.setEstado("Cancelada");
        FacturaEntity guardada = repo.save(factura);

        return convertirADTO(guardada);
    }
}
