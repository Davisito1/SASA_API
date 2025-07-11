package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.UserEntity;
import APISASA.API_sasa.Models.UserDTO;
import APISASA.API_sasa.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    public List<UserDTO> getAllUsers(){
        List<UserEntity> users = repo.findAll();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private UserDTO convertToDTO(UserEntity userEntity) {
        UserDTO dto = new UserDTO();
        dto.setId(userEntity.getId());
        dto.setNombreUsuario(userEntity.getNombreUsuario());
        dto.setContrasena(userEntity.getContrasena());
        dto.setRol(userEntity.getRol());
        dto.setEstado(userEntity.getEstado());
        return dto;
    }
}
