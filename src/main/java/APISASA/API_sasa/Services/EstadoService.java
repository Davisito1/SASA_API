package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.EstadoEntity;
import APISASA.API_sasa.Models.DTO.EstadoDTO;
import APISASA.API_sasa.Repositories.EstadoRepository;
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

    // 🔹 Consultar con paginación
    public Page<EstadoDTO> getAllEstados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EstadoEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirAEstadoDTO);
    }

    // 🔹 Consultar todos sin paginación (para catálogos)
    public List<EstadoDTO> getAllEstadosSinPaginacion() {
        return repo.findAll().stream()
                .map(this::convertirAEstadoDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Conversores
    public EstadoEntity convertirAEstadoEntity(EstadoDTO dto) {
        EstadoEntity entity = new EstadoEntity();
        entity.setId(dto.getId());
        entity.setNombreEstado(dto.getNombreEstado());
        return entity;
    }

    public EstadoDTO convertirAEstadoDTO(EstadoEntity entity) {
        EstadoDTO dto = new EstadoDTO();
        dto.setId(entity.getId());
        dto.setNombreEstado(entity.getNombreEstado());
        return dto;
    }
}
