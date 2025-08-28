package APISASA.API_sasa.Services.Estado;

import APISASA.API_sasa.Entities.Estado.EstadoVehiculoEntity;
import APISASA.API_sasa.Models.DTO.Estado.EstadoDTO;
import APISASA.API_sasa.Repositories.Estado.EstadoRepository;
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
public class EstadoService {

    @Autowired
    private EstadoRepository repo;

    //  Consultar con paginación
    public Page<EstadoDTO> getAllEstados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EstadoVehiculoEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirAEstadoDTO);
    }

    //  Consultar todos sin paginación (para catálogos)
    public List<EstadoDTO> getAllEstadosSinPaginacion() {
        return repo.findAll().stream()
                .map(this::convertirAEstadoDTO)
                .collect(Collectors.toList());
    }


    public EstadoVehiculoEntity convertirAEstadoEntity(EstadoDTO dto) {
        EstadoVehiculoEntity entity = new EstadoVehiculoEntity();
        entity.setId(dto.getId());   // 👈 si tu Entity lo tiene como idEstado
        entity.setNombreEstado(dto.getNombreEstado());
        return entity;
    }

    public EstadoDTO convertirAEstadoDTO(EstadoVehiculoEntity entity) {
        EstadoDTO dto = new EstadoDTO();
        dto.setId(entity.getId());   // 👈 si tu Entity lo tiene como idEstado
        dto.setNombreEstado(entity.getNombreEstado());
        return dto;
    }
}
