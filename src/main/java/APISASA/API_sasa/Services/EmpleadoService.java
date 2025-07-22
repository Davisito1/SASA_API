package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.EmpleadoEntity;
import APISASA.API_sasa.Exceptions.ExceptionEmpleadoNoEncontrado;
import APISASA.API_sasa.Models.DTO.EmpleadoDTO;
import APISASA.API_sasa.Repositories.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository repo;

    // Obtener todos los empleados
    public List<EmpleadoDTO> obtenerEmpleados() {
        List<EmpleadoEntity> lista = repo.findAll();
        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Insertar nuevo empleado
    public EmpleadoDTO insertarEmpleado(EmpleadoDTO dto) {
        EmpleadoEntity entity = convertirAEntity(dto);
        EmpleadoEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    // Actualizar empleado existente
    public EmpleadoDTO actualizarEmpleado(Long id, EmpleadoDTO dto) {
        EmpleadoEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionEmpleadoNoEncontrado("No existe un empleado con ID: " + id));

        existente.setNombres(dto.getNombres());
        existente.setApellidos(dto.getApellidos());
        existente.setCargo(dto.getCargo());
        existente.setDui(dto.getDui());
        existente.setTelefono(dto.getTelefono());
        existente.setDireccion(dto.getDireccion());
        existente.setFechaContratacion(dto.getFechaContratacion());
        existente.setCorreo(dto.getCorreo());
        existente.setIdUsuario(dto.getIdUsuario());

        EmpleadoEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    // Eliminar empleado por ID
    public boolean eliminarEmpleado(Long id) {
        try {
            EmpleadoEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionEmpleadoNoEncontrado("No se encontró el empleado con ID: " + id + " para eliminar.");
        }
    }

    // Convertir entidad a DTO
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
        dto.setCorreo(entity.getCorreo());
        dto.setIdUsuario(entity.getIdUsuario());
        return dto;
    }

    // Convertir DTO a entidad
    private EmpleadoEntity convertirAEntity(EmpleadoDTO dto) {
        EmpleadoEntity entity = new EmpleadoEntity();
        entity.setNombres(dto.getNombres());
        entity.setApellidos(dto.getApellidos());
        entity.setCargo(dto.getCargo());
        entity.setDui(dto.getDui());
        entity.setTelefono(dto.getTelefono());
        entity.setDireccion(dto.getDireccion());
        entity.setFechaContratacion(dto.getFechaContratacion());
        entity.setCorreo(dto.getCorreo());
        entity.setIdUsuario(dto.getIdUsuario());
        return entity;
    }
}
