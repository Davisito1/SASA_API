package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.UserEntity;
import APISASA.API_sasa.Models.DTO.UserDTO;
import APISASA.API_sasa.Repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository repo;

    // ✅ CONSULTAR TODOS
    public Page<UserDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertToDTO);
    }

    // ✅ INSERTAR NUEVO USUARIO
    public UserDTO insertUser(UserDTO data) {
        if (data == null || data.getContrasena() == null || data.getContrasena().isEmpty()) {
            throw new IllegalArgumentException("El usuario o contraseña no pueden ser nulos");
        }

        try {
            UserEntity entity = convertToEntity(data);
            // ⚠️ No seteamos ID, Oracle lo maneja con secuencia/trigger
            entity.setIdUsuario(null);
            UserEntity guardado = repo.save(entity);
            return convertToDTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar el usuario: {}", e.getMessage());
            throw new RuntimeException("No se pudo registrar el usuario.");
        }
    }

    // ✅ ACTUALIZAR USUARIO
    public UserDTO updateUser(Long id, @Valid UserDTO data) {
        UserEntity existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        existente.setNombreUsuario(data.getNombreUsuario());
        existente.setContrasena(data.getContrasena());
        existente.setRol(data.getRol());
        existente.setEstado(data.getEstado());

        UserEntity actualizado = repo.save(existente);
        return convertToDTO(actualizado);
    }

    // ✅ ELIMINAR USUARIO
    public boolean deleteUser(Long id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("No se encontró usuario con ID: " + id + " para eliminar.");
        }
    }

    // ==========================
    // 🔹 CONVERTIR A DTO
    // ==========================
    private UserDTO convertToDTO(UserEntity userEntity) {
        UserDTO dto = new UserDTO();
        dto.setId(userEntity.getIdUsuario());  // ⚠️ si tu entity es UsuarioEntity cámbialo a getIdUsuario()
        dto.setNombreUsuario(userEntity.getNombreUsuario());
        dto.setContrasena(userEntity.getContrasena());
        dto.setRol(userEntity.getRol());
        dto.setEstado(userEntity.getEstado());
        return dto;
    }

    // ==========================
    // 🔹 CONVERTIR A ENTITY
    // ==========================
    private UserEntity convertToEntity(UserDTO data) {
        UserEntity entity = new UserEntity();
        entity.setNombreUsuario(data.getNombreUsuario());
        entity.setContrasena(data.getContrasena());
        entity.setRol(data.getRol());
        entity.setEstado(data.getEstado());
        return entity;
    }
}
