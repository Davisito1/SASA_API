package APISASA.API_sasa.Services.Historial;

import APISASA.API_sasa.Entities.Historial.HistorialEntity;
import APISASA.API_sasa.Entities.Vehiculo.VehicleEntity;
import APISASA.API_sasa.Models.DTO.Historial.HistorialDTO;
import APISASA.API_sasa.Repositories.Historial.HistorialRepository;
import APISASA.API_sasa.Repositories.Vehiculo.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HistorialService {

    @Autowired
    private HistorialRepository repo;

    @Autowired
    private VehicleRepository vehicleRepo;

    // ✅ Consultar historial con paginación
    public Page<HistorialDTO> obtenerHistorial(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HistorialEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    // ✅ Consultar historial por vehículo
    public List<HistorialDTO> obtenerPorVehiculo(Long idVehiculo) {
        return repo.findByVehiculo_IdVehiculo(idVehiculo)  // 👈 buscar por FK
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ✅ Registrar un nuevo historial
    public HistorialDTO insertarHistorial(HistorialDTO dto) {
        try {
            HistorialEntity entity = convertirAEntity(dto);
            HistorialEntity guardado = repo.save(entity);
            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar historial: {}", e.getMessage());
            throw new RuntimeException("No se pudo registrar el historial.");
        }
    }

    // 🔁 Conversores
    private HistorialEntity convertirAEntity(HistorialDTO dto) {
        HistorialEntity entity = new HistorialEntity();
        entity.setIdHistorial(dto.getId());
        entity.setFechaIngreso(dto.getFechaIngreso());
        entity.setFechaSalida(dto.getFechaSalida());
        entity.setTrabajoRealizado(dto.getTrabajoRealizado());
        entity.setObservaciones(dto.getObservaciones());

        if (dto.getIdVehiculo() != null) {
            VehicleEntity vehiculo = vehicleRepo.findById(dto.getIdVehiculo())
                    .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con ID: " + dto.getIdVehiculo()));
            entity.setVehiculo(vehiculo);
        }

        return entity;
    }

    private HistorialDTO convertirADTO(HistorialEntity entity) {
        HistorialDTO dto = new HistorialDTO();
        dto.setId(entity.getIdHistorial());
        dto.setFechaIngreso(entity.getFechaIngreso());
        dto.setFechaSalida(entity.getFechaSalida());
        dto.setTrabajoRealizado(entity.getTrabajoRealizado());
        dto.setObservaciones(entity.getObservaciones());

        if (entity.getVehiculo() != null) {
            dto.setIdVehiculo(entity.getVehiculo().getIdVehiculo()); // 👈 devolvemos el id en el DTO
        }

        return dto;
    }

    public void eliminarHistorial(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Historial no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }
}
