package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.EmpleadoEntity;
import APISASA.API_sasa.Models.DTO.EmpleadoDTO;
import APISASA.API_sasa.Repositories.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoService {
    @Autowired
    private EmpleadoRepository repo;

    public List<EmpleadoDTO> obtenerEmpleados() {
        List<EmpleadoEntity> lista = repo.findAll();
        return lista.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    private EmpleadoDTO convertirADTO(EmpleadoEntity entity) {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setId(entity.getId());
        dto.setNombres(entity.getNombres());
        dto.setApellidos(entity.getApellidos());
        dto.setCargo(entity.getCargo());
        dto.setDui(entity.getDui());
        dto.setTelefono(entity.getTelefono());
        dto.setDireccion(entity.getDireccion());
        dto.setFechaContratacion(entity.getFechaContratacion());
        dto.setCorreo(dto.getCorreo());
        dto.setIdUsuario(entity.getIdUsuario());
        return dto;
    }
}