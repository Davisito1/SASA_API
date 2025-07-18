package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.ClienteEntity;
import APISASA.API_sasa.Models.DTO.ClientDTO;
import APISASA.API_sasa.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    private ClientRepository repo;

    public List<ClientDTO> getAllClients(){
        List<ClienteEntity> clients = repo.findAll();
        return clients.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ClientDTO convertToDTO(ClienteEntity clienteEntity){
        ClientDTO dto = new ClientDTO();
        dto.setId(clienteEntity.getId());
        dto.setNombre(clienteEntity.getNombre());
        dto.setApellido(clienteEntity.getApellido());
        dto.setDui(clienteEntity.getDui());
        dto.setFechaNacimiento(clienteEntity.getFechaNacimiento());
        dto.setGenero(clienteEntity.getGenero());
        return dto;
    }
}
