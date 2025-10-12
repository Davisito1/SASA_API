package APISASA.API_sasa.Services.OrdenTrabajo;

import APISASA.API_sasa.Entities.OrdenTrabajo.OrdenTrabajoEntity;
import APISASA.API_sasa.Entities.Vehiculo.VehicleEntity;
import APISASA.API_sasa.Entities.DetalleOrden.DetalleOrdenEntity;
import APISASA.API_sasa.Models.DTO.OrdenTrabajo.OrdenTrabajoDTO;
import APISASA.API_sasa.Repositories.OrdenTrabajo.OrdenTrabajoRepository;
import APISASA.API_sasa.Repositories.Vehiculo.VehicleRepository;
import APISASA.API_sasa.Repositories.DetalleOrden.DetalleOrdenRepository;
import APISASA.API_sasa.Repositories.Factura.FacturaRepository; // ✅ se agrega

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrdenTrabajoService {

    @Autowired private OrdenTrabajoRepository repository;
    @Autowired private VehicleRepository vehicleRepository;
    @Autowired private DetalleOrdenRepository detalleOrdenRepository;
    @Autowired private FacturaRepository facturaRepository; // ✅ se inyecta

    // ===================================================
    // 🔹 Obtener todas las órdenes
    // ===================================================
    public List<OrdenTrabajoDTO> obtenerOrdenes() {
        return repository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ===================================================
    // 🔹 Obtener orden por ID
    // ===================================================
    public OrdenTrabajoDTO obtenerPorId(Long id) {
        Optional<OrdenTrabajoEntity> orden = repository.findById(id);
        return orden.map(this::convertirADTO).orElse(null);
    }

    // ===================================================
    // 🔹 Crear nueva orden
    // ===================================================
    public OrdenTrabajoDTO crearOrden(OrdenTrabajoDTO dto) {
        OrdenTrabajoEntity entity = convertirAEntity(dto);
        OrdenTrabajoEntity guardado = repository.save(entity);
        return convertirADTO(guardado);
    }

    // ===================================================
    // 🔹 Actualizar orden
    // ===================================================
    public OrdenTrabajoDTO actualizarOrden(Long id, OrdenTrabajoDTO dto) {
        Optional<OrdenTrabajoEntity> existe = repository.findById(id);
        if (existe.isPresent()) {
            OrdenTrabajoEntity entity = existe.get();
            entity.setFecha(dto.getFecha());

            VehicleEntity vehiculo = vehicleRepository.findById(dto.getIdVehiculo())
                    .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
            entity.setVehiculo(vehiculo);

            List<DetalleOrdenEntity> detalles = detalleOrdenRepository.findAllById(dto.getDetallesIds());
            entity.setDetalles(detalles);

            OrdenTrabajoEntity actualizado = repository.save(entity);
            return convertirADTO(actualizado);
        }
        return null;
    }

    // ===================================================
    // 🔹 Eliminar orden (ahora sin error 500)
    // ===================================================
    @Transactional
    public boolean eliminarOrden(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }

        try {
            // 🔸 1️⃣ Borrar facturas asociadas
            facturaRepository.deleteAllByOrdenTrabajo_IdOrden(id);

            // 🔸 2️⃣ Borrar detalles asociados
            detalleOrdenRepository.deleteAllByOrdenTrabajo_IdOrden(id);

            // 🔸 3️⃣ Borrar la orden
            repository.deleteById(id);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar la orden: " + e.getMessage(), e);
        }
    }

    // ===================================================
    // 🔹 Conversores DTO ↔ Entity
    // ===================================================
    private OrdenTrabajoDTO convertirADTO(OrdenTrabajoEntity entity) {
        return OrdenTrabajoDTO.builder()
                .id(entity.getIdOrden())
                .fecha(entity.getFecha())
                .idVehiculo(entity.getVehiculo().getIdVehiculo())
                .placaVehiculo(entity.getVehiculo().getPlaca())
                .marcaVehiculo(entity.getVehiculo().getMarca())
                .detallesIds(entity.getDetalles()
                        .stream()
                        .map(DetalleOrdenEntity::getIdDetalle)
                        .collect(Collectors.toList()))
                .build();
    }

    private OrdenTrabajoEntity convertirAEntity(OrdenTrabajoDTO dto) {
        VehicleEntity vehiculo = vehicleRepository.findById(dto.getIdVehiculo())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        List<DetalleOrdenEntity> detalles = dto.getDetallesIds() != null
                ? detalleOrdenRepository.findAllById(dto.getDetallesIds())
                : List.of();

        return OrdenTrabajoEntity.builder()
                .idOrden(dto.getId())
                .fecha(dto.getFecha())
                .vehiculo(vehiculo)
                .detalles(detalles)
                .build();
    }
}
