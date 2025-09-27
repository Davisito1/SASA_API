package APISASA.API_sasa.Services.Empleado;

import APISASA.API_sasa.Config.Argon2.Argon2Password;
import APISASA.API_sasa.Entities.Empleado.EmpleadoEntity;
import APISASA.API_sasa.Entities.Usuario.UserEntity;
import APISASA.API_sasa.Exceptions.ExceptionEmpleadoNoEncontrado;
import APISASA.API_sasa.Models.DTO.Empleado.EmpleadoDTO;
import APISASA.API_sasa.Repositories.Empleado.EmpleadoRepository;
import APISASA.API_sasa.Repositories.Usuario.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository repo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private Argon2Password argon2;

    // Obtener todos los empleados
    public List<EmpleadoDTO> obtenerEmpleados() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener empleados paginados + búsqueda opcional
    public Page<EmpleadoDTO> obtenerEmpleadosPaginado(String q, Pageable pageable) {
        if (q == null || q.isBlank()) {
            return repo.findAll(pageable).map(this::convertirADTO);
        }
        String term = q.trim();
        return repo.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrDuiContainingIgnoreCaseOrCorreoElectronicoContainingIgnoreCase(
                        term, term, term, term, pageable
                )
                .map(this::convertirADTO);
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
        existente.setFechaNacimiento(dto.getFechaNacimiento());
        existente.setCorreoElectronico(dto.getCorreo());

        return convertirADTO(repo.save(existente));
    }

    // Eliminar empleado por ID
    public boolean eliminarEmpleado(Long id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionEmpleadoNoEncontrado("No se encontró el empleado con ID: " + id + " para eliminar.");
        }
    }

    // ================== Conversores ==================
    private EmpleadoDTO convertirADTO(EmpleadoEntity entity) {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setId(entity.getIdEmpleado());
        dto.setNombres(entity.getNombres());
        dto.setApellidos(entity.getApellidos());
        dto.setCargo(entity.getCargo());
        dto.setDui(entity.getDui());
        dto.setTelefono(entity.getTelefono());
        dto.setDireccion(entity.getDireccion());
        dto.setFechaContratacion(entity.getFechaContratacion());
        dto.setFechaNacimiento(entity.getFechaNacimiento());
        dto.setCorreo(entity.getCorreoElectronico());

        if (entity.getUsuario() != null) {
            dto.setNombreUsuario(entity.getUsuario().getNombreUsuario());
        }
        return dto;
    }

    private EmpleadoEntity convertirAEntity(EmpleadoDTO dto) {
        EmpleadoEntity entity = new EmpleadoEntity();
        entity.setNombres(dto.getNombres());
        entity.setApellidos(dto.getApellidos());
        entity.setCargo(dto.getCargo());
        entity.setDui(dto.getDui());
        entity.setTelefono(dto.getTelefono());
        entity.setDireccion(dto.getDireccion());
        entity.setFechaContratacion(dto.getFechaContratacion());
        entity.setFechaNacimiento(dto.getFechaNacimiento());
        entity.setCorreoElectronico(dto.getCorreo());
        return entity;
    }

    // 🔹 Registrar Empleado + Usuario en un solo paso
    @Transactional
    public EmpleadoDTO registrarEmpleadoConUsuario(@Valid EmpleadoDTO dto) {
        // 1. Crear y guardar Usuario
        UserEntity user = new UserEntity();
        user.setNombreUsuario(dto.getNombreUsuario());
        user.setCorreo(dto.getCorreo());
        user.setContrasena(argon2.EncryptPassword(dto.getContrasena())); // encriptar password
        user.setRol("EMPLEADO");
        user.setEstado("ACTIVO");

        UserEntity userGuardado = userRepo.save(user);

        // 2. Crear y guardar Empleado vinculado al usuario
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setNombres(dto.getNombres());
        empleado.setApellidos(dto.getApellidos());
        empleado.setCargo(dto.getCargo());
        empleado.setDui(dto.getDui());
        empleado.setTelefono(dto.getTelefono());
        empleado.setDireccion(dto.getDireccion());
        empleado.setFechaContratacion(dto.getFechaContratacion());
        empleado.setFechaNacimiento(dto.getFechaNacimiento());
        empleado.setCorreoElectronico(dto.getCorreo());
        empleado.setUsuario(userGuardado);

        EmpleadoEntity guardado = repo.save(empleado);

        return convertirADTO(guardado);
    }

    // ================== Validaciones duplicados ==================
    public boolean existeCorreo(String correo) {
        return userRepo.existsByCorreo(correo);
    }

    public boolean existeUsuario(String nombreUsuario) {
        return userRepo.existsByNombreUsuario(nombreUsuario);
    }

}
