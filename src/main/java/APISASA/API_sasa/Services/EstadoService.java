package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.EstadoEntity;
import APISASA.API_sasa.Models.DTO.EstadoDTO;
import APISASA.API_sasa.Repositories.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoService {
    @Autowired
    private EstadoRepository repo;

    public List<EstadoDTO> getAllStatus() {
        List<EstadoEntity> estados = repo.findAll();
        return estados.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public EstadoDTO convertToDTO(EstadoEntity estadoEntity) {
        EstadoDTO dto = new EstadoDTO();
        dto.setId(estadoEntity.getId());
        dto.setNombreEstado(estadoEntity.getNombreEstado());
        return dto;
    }
}
