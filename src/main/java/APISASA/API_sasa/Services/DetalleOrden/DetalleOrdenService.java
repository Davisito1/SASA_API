package APISASA.API_sasa.Services.DetalleOrden;

import APISASA.API_sasa.Entities.DetalleOrden.DetalleOrdenEntity;
import APISASA.API_sasa.Entities.Mantenimiento.MantenimientoEntity;
import APISASA.API_sasa.Entities.OrdenTrabajo.OrdenTrabajoEntity;
import APISASA.API_sasa.Exceptions.ExceptionDetalleOrdenNoEncontrado;
import APISASA.API_sasa.Models.DTO.DetalleOrden.DetalleOrdenDTO;
import APISASA.API_sasa.Repositories.DetalleOrden.DetalleOrdenRepository;
import APISASA.API_sasa.Repositories.Mantenimiento.MantenimientoRepository;
import APISASA.API_sasa.Repositories.OrdenTrabajo.OrdenTrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetalleOrdenService {

    @Autowired
    private DetalleOrdenRepository repo;

    @Autowired
    private OrdenTrabajoRepository ordenRepo;

    @Autowired
    private MantenimientoRepository mantRepo;

    // 🔹 Obtener todos los detalles
    public List<DetalleOrdenDTO> obtenerDetalles() {
        return repo.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // 🔹 Obtener detalle por ID
    public DetalleOrdenDTO obtenerPorId(Long id) {
        DetalleOrdenEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionDetalleOrdenNoEncontrado(
                        "No existe un detalle con ID: " + id
                ));
        return convertirADTO(entity);
    }

    // 🔹 Insertar detalle
    public DetalleOrdenDTO insertarDetalle(DetalleOrdenDTO dto) {
        // calcular subtotal en backend si no viene
        if (dto.getSubtotal() == null) {
            dto.setSubtotal(dto.getCantidad() * dto.getPrecioUnitario());
        }

        DetalleOrdenEntity entity = convertirAEntity(dto);
        entity.setIdDetalle(null); // dejar que la secuencia lo maneje
        DetalleOrdenEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    // 🔹 Actualizar detalle
    public DetalleOrdenDTO actualizarDetalle(Long id, DetalleOrdenDTO dto) {
        DetalleOrdenEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionDetalleOrdenNoEncontrado(
                        "No existe un detalle con ID: " + id
                ));

        existente.setCantidad(dto.getCantidad());
        existente.setPrecioUnitario(dto.getPrecioUnitario());

        // recalcular subtotal
        existente.setSubtotal(dto.getCantidad() * dto.getPrecioUnitario());

        if (dto.getIdOrden() != null) {
            OrdenTrabajoEntity orden = ordenRepo.findById(dto.getIdOrden())
                    .orElseThrow(() -> new RuntimeException(
                            "Orden no encontrada con ID: " + dto.getIdOrden()));
            existente.setOrdenTrabajo(orden);
        }

        if (dto.getIdMantenimiento() != null) {
            MantenimientoEntity mant = mantRepo.findById(dto.getIdMantenimiento())
                    .orElseThrow(() -> new RuntimeException(
                            "Mantenimiento no encontrado con ID: " + dto.getIdMantenimiento()));
            existente.setMantenimiento(mant);
        }

        DetalleOrdenEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    // 🔹 Eliminar detalle
    public boolean eliminarDetalle(Long id) {
        if (!repo.existsById(id)) {
            throw new ExceptionDetalleOrdenNoEncontrado("No existe un detalle con ID: " + id);
        }
        repo.deleteById(id);
        return true;
    }

    // ==========================
    // 🔹 Conversores DTO ↔ Entity
    // ==========================

    private DetalleOrdenDTO convertirADTO(DetalleOrdenEntity entity) {
        return DetalleOrdenDTO.builder()
                .id(entity.getIdDetalle())
                .idOrden(entity.getOrdenTrabajo() != null ? entity.getOrdenTrabajo().getIdOrden() : null)
                .idMantenimiento(entity.getMantenimiento() != null ? entity.getMantenimiento().getId() : null)
                .cantidad(entity.getCantidad())
                .precioUnitario(entity.getPrecioUnitario())
                .subtotal(entity.getSubtotal() != null
                        ? entity.getSubtotal()
                        : entity.getCantidad() * entity.getPrecioUnitario())
                .build();
    }


    private DetalleOrdenEntity convertirAEntity(DetalleOrdenDTO dto) {
        DetalleOrdenEntity entity = new DetalleOrdenEntity();
        entity.setCantidad(dto.getCantidad());
        entity.setPrecioUnitario(dto.getPrecioUnitario());
        entity.setSubtotal(dto.getSubtotal() != null
                ? dto.getSubtotal()
                : dto.getCantidad() * dto.getPrecioUnitario());

        if (dto.getIdOrden() != null) {
            OrdenTrabajoEntity orden = ordenRepo.findById(dto.getIdOrden())
                    .orElseThrow(() -> new RuntimeException(
                            "Orden no encontrada con ID: " + dto.getIdOrden()));
            entity.setOrdenTrabajo(orden);
        }

        if (dto.getIdMantenimiento() != null) {
            MantenimientoEntity mant = mantRepo.findById(dto.getIdMantenimiento())
                    .orElseThrow(() -> new RuntimeException(
                            "Mantenimiento no encontrado con ID: " + dto.getIdMantenimiento()));
            entity.setMantenimiento(mant);
        }

        return entity;
    }

    public List<DetalleOrdenDTO> obtenerPorOrden(Long idOrden) {
        return repo.findByOrdenTrabajo_IdOrden(idOrden).stream()
                .map(e -> DetalleOrdenDTO.builder()
                        .id(e.getIdDetalle())                              // PK del detalle
                        .idOrden(e.getOrdenTrabajo().getIdOrden())         // FK orden
                        .idMantenimiento(e.getMantenimiento().getId())    // 👈 aquí va el Long
                        .mantenimientoNombre(e.getMantenimiento().getDescripcionTrabajo()) // 👈 aquí va el String
                        .cantidad(e.getCantidad())
                        .precioUnitario(e.getPrecioUnitario())
                        .subtotal(e.getSubtotal())
                        .build()
                ).toList();
    }





}
