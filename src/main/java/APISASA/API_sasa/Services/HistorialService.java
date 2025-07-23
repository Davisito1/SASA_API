package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.HistorialEntity;
import APISASA.API_sasa.Models.DTO.HistorialDTO;
import APISASA.API_sasa.Repositories.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialService {
    @Autowired
    private HistorialRepository repo;

    public List<HistorialDTO> obtenerHistorial() {
        List<HistorialEntity> datos = repo.findAll();
        return datos.stream().map(this::convertirADTO).collect(Collectors.toList());
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
