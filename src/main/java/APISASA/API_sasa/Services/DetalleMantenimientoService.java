package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.DetalleMantenimientoEntity;
import APISASA.API_sasa.Models.DTO.DetalleMantenimientoDTO;
import APISASA.API_sasa.Repositories.DetalleMantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetalleMantenimientoService {
    @Autowired
    private DetalleMantenimientoRepository repo;

    public List<DetalleMantenimientoDTO> obtenerDetalles() {
        List<DetalleMantenimientoEntity> lista = repo.findAll();
        return lista.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    private DetalleMantenimientoDTO convertirADTO(DetalleMantenimientoEntity entity) {
        DetalleMantenimientoDTO dto = new DetalleMantenimientoDTO();
        dto.setId(entity.getId());
        dto.setEstado(entity.getEstado());
        dto.setIdMantenimiento(entity.getIdMantenimiento());
        dto.setIdServicio(entity.getIdServicio());
        dto.setIdTipoMantenimiento(entity.getIdTipoMantenimiento());
        return dto;
    }
}
