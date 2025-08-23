package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.TipoMantenimientoEntity;
import APISASA.API_sasa.Models.DTO.TipoMantenimientoDTO;
import APISASA.API_sasa.Repositories.TipoMantenimientoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TipoMantenimientoService {

    @Autowired
    private TipoMantenimientoRepository repo;

    // 🔹 Obtener todos los tipos de mantenimiento (catálogo fijo)
    public List<TipoMantenimientoDTO> obtenerTipos() {
        List<TipoMantenimientoEntity> datos = repo.findAll();
        return datos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // 🔹 Conversores
    private TipoMantenimientoDTO convertirADTO(TipoMantenimientoEntity entity) {
        TipoMantenimientoDTO dto = new TipoMantenimientoDTO();
        dto.setId(entity.getId());
        dto.setTipoMantenimiento(entity.getTipoMantenimiento());
        return dto;
    }

    private TipoMantenimientoEntity convertirAEntity(TipoMantenimientoDTO dto) {
        TipoMantenimientoEntity entity = new TipoMantenimientoEntity();
        entity.setId(dto.getId());
        entity.setTipoMantenimiento(dto.getTipoMantenimiento());
        return entity;
    }
}
