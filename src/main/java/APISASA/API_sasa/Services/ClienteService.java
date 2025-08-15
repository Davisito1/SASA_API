package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.ClienteEntity;
import APISASA.API_sasa.Exceptions.ExceptionClienteNoEncontrado;
import APISASA.API_sasa.Models.DTO.ClientDTO;
import APISASA.API_sasa.Models.DTO.EmpleadoDTO;
import APISASA.API_sasa.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClientRepository repo;

    public Page<ClientDTO> obtenerClientes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClienteEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    public ClientDTO insertarCliente(ClientDTO dto) {
        ClienteEntity entity = convertirAEntity(dto);
        ClienteEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    public ClientDTO actualizarCliente(Long id, ClientDTO dto) {
        ClienteEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionClienteNoEncontrado("No existe un cliente con ID: " + id));

        existente.setNombre(dto.getNombre());
        existente.setApellido(dto.getApellido());
        existente.setDui(dto.getDui());
        existente.setFechaNacimiento(dto.getFechaNacimiento());
        existente.setGenero(dto.getGenero());
        // Agregar campos nuevos
        existente.setCorreoElectronico(dto.getCorreoElectronico());
        existente.setContrasena(dto.getContrasena());

        ClienteEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    public boolean eliminarCliente(Long id) {
        try {
            ClienteEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionClienteNoEncontrado("No se encontró el cliente con ID: " + id + " para eliminar.");
        }
    }

    private ClientDTO convertirADTO(ClienteEntity entity) {
        ClientDTO dto = new ClientDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setDui(entity.getDui());
        dto.setFechaNacimiento(entity.getFechaNacimiento());
        dto.setGenero(entity.getGenero());
        dto.setCorreoElectronico(entity.getCorreoElectronico());
        dto.setContrasena(entity.getContrasena());
        return dto;
    }

    private ClienteEntity convertirAEntity(ClientDTO dto) {
        ClienteEntity entity = new ClienteEntity();
        entity.setId(dto.getId()); // si es null, JPA lo ignora y usa la secuencia
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setDui(dto.getDui());
        entity.setFechaNacimiento(dto.getFechaNacimiento());
        entity.setGenero(dto.getGenero());
        entity.setCorreoElectronico(dto.getCorreoElectronico());
        entity.setContrasena(dto.getContrasena());
        return entity;
    }
}
