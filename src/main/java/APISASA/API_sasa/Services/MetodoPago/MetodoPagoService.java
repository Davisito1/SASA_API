package APISASA.API_sasa.Services.MetodoPago;

import APISASA.API_sasa.Entities.MetodoPago.MetodoPagoEntity;
import APISASA.API_sasa.Models.DTO.MetodoPago.MetodoPagoDTO;
import APISASA.API_sasa.Repositories.MetodoPago.MetodoPagoRepository;
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

    //  Obtener todos los métodos de pago (catálogo fijo)
    public List<MetodoPagoDTO> obtenerTodos() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    //  Insertar nuevo
    public MetodoPagoDTO insertarMetodo(MetodoPagoDTO dto) {
        MetodoPagoEntity entity = new MetodoPagoEntity();
        entity.setMetodo(dto.getMetodo());

        MetodoPagoEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    //  Eliminar método de pago
    public boolean eliminarMetodo(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }


    private MetodoPagoDTO convertirADTO(MetodoPagoEntity entity) {
        MetodoPagoDTO dto = new MetodoPagoDTO();
        dto.setId(entity.getIdMetodoPago()); // 👈 corregido
        dto.setMetodo(entity.getMetodo());
        return dto;
    }
}
