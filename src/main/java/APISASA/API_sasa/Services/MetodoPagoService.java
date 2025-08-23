package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.MetodoPagoEntity;
import APISASA.API_sasa.Models.DTO.MetodoPagoDTO;
import APISASA.API_sasa.Repositories.MetodoPagoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository repo;

    // ✅ Obtener todos los métodos de pago (catálogo fijo)
    public List<MetodoPagoDTO> obtenerTodos() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // 🔁 Conversor Entity → DTO
    private MetodoPagoDTO convertirADTO(MetodoPagoEntity entity) {
        MetodoPagoDTO dto = new MetodoPagoDTO();
        dto.setId(entity.getId());
        dto.setMetodo(entity.getMetodo());
        return dto;
    }
}
