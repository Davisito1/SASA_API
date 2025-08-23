package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.HistorialEntity;
import APISASA.API_sasa.Models.DTO.HistorialDTO;
import APISASA.API_sasa.Repositories.HistorialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HistorialService {

    @Autowired
    private HistorialRepository repo;

    // ✅ Consultar historial con paginación
    public Page<HistorialDTO> obtenerHistorial(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HistorialEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    // ✅ Consultar historial por vehículo
    public List<HistorialDTO> obtenerPorVehiculo(Long idVehiculo) {
        return repo.findByIdVehiculo(idVehiculo)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ✅ Registrar un nuevo historial (manual o desde un mantenimiento)
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
        entity.setId(dto.getId());
        entity.setFechaIngreso(dto.getFechaIngreso());
        entity.setFechaSalida(dto.getFechaSalida());
        entity.setTrabajoRealizado(dto.getTrabajoRealizado());
        entity.setObservaciones(dto.getObservaciones());
        entity.setIdVehiculo(dto.getIdVehiculo());
        return entity;
    }

    private HistorialDTO convertirADTO(HistorialEntity entity) {
        HistorialDTO dto = new HistorialDTO();
        dto.setId(entity.getId());
        dto.setFechaIngreso(entity.getFechaIngreso());
        dto.setFechaSalida(entity.getFechaSalida());
        dto.setTrabajoRealizado(entity.getTrabajoRealizado());
        dto.setObservaciones(entity.getObservaciones());
        dto.setIdVehiculo(entity.getIdVehiculo());
        return dto;
    }
}
