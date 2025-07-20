package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.CitaEntity;
import APISASA.API_sasa.Models.DTO.CitaDTO;
import APISASA.API_sasa.Repositories.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaService {
    @Autowired
    private CitaRepository repo;

    public List<CitaDTO> obtenerCitas() {
        List<CitaEntity> lista = repo.findAll();
        return lista.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    private CitaDTO convertirADTO(CitaEntity citaEntity) {
        CitaDTO dto = new CitaDTO();
        dto.setId(citaEntity.getId());
        dto.setFecha(citaEntity.getFecha());
        dto.setHora(citaEntity.getHora());
        dto.setEstado(citaEntity.getEstado());
        dto.setIdCliente(citaEntity.getIdCliente());
        return dto;
    }
}
