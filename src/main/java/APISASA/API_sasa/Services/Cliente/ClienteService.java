package APISASA.API_sasa.Services.Cliente;

import APISASA.API_sasa.Entities.Cliente.ClienteEntity;
import APISASA.API_sasa.Exceptions.ExceptionClienteNoEncontrado;
import APISASA.API_sasa.Models.DTO.Cliente.ClientDTO;
import APISASA.API_sasa.Repositories.Cliente.ClientRepository;
import APISASA.API_sasa.Config.Argon2.Argon2Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClientRepository repo;

    @Autowired
    private Argon2Password argon2Password; // ✅ usamos tu clase personalizada


    // Consultar clientes con paginación
    public Page<ClientDTO> obtenerClientes(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<ClienteEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    // Consultar cliente por ID
    public ClientDTO obtenerClientePorId(Long id) {
        ClienteEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionClienteNoEncontrado("No existe un cliente con ID: " + id));
        return convertirADTO(entity);
    }

    // Insertar nuevo cliente
    public ClientDTO insertarCliente(ClientDTO dto) {
        ClienteEntity entity = convertirAEntity(dto);
        entity.setId(null); // ⚠ que lo maneje la secuencia/trigger
        // ✅ cifrar contraseña al registrar
        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            entity.setContrasena(argon2Password.EncryptPassword(dto.getContrasena()));
        }
        ClienteEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    // Actualizar cliente (completo)
    public ClientDTO actualizarCliente(Long id, ClientDTO dto) {
        ClienteEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionClienteNoEncontrado("No existe un cliente con ID: " + id));

        existente.setNombre(dto.getNombre());
        existente.setApellido(dto.getApellido());
        existente.setDui(dto.getDui());
        existente.setFechaNacimiento(dto.getFechaNacimiento());
        existente.setGenero(dto.getGenero());
        existente.setCorreo(dto.getCorreo());

        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            existente.setContrasena(argon2Password.EncryptPassword(dto.getContrasena()));
        }

        ClienteEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    // Eliminar cliente
    public boolean eliminarCliente(Long id) {
        try {
            repo.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionClienteNoEncontrado("No se encontró el cliente con ID: " + id + " para eliminar.");
        }
    }

    // ==========================
    // Conversores Entity ⇄ DTO
    // ==========================
    private ClientDTO convertirADTO(ClienteEntity entity) {
        ClientDTO dto = new ClientDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setDui(entity.getDui());
        dto.setFechaNacimiento(entity.getFechaNacimiento());
        dto.setGenero(entity.getGenero());
        dto.setCorreo(entity.getCorreo());
        dto.setContrasena(entity.getContrasena());
        return dto;
    }

    private ClienteEntity convertirAEntity(ClientDTO dto) {
        ClienteEntity entity = new ClienteEntity();
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setDui(dto.getDui());
        entity.setFechaNacimiento(dto.getFechaNacimiento());
        entity.setGenero(dto.getGenero());
        entity.setCorreo(dto.getCorreo());
        entity.setContrasena(dto.getContrasena());
        return entity;
    }

    // ✅ Actualizar cliente parcialmente (ej. ajustes)
    public ClientDTO actualizarClienteParcial(Long id, ClientDTO dto) {
        ClienteEntity cliente = repo.findById(id)
                .orElseThrow(() -> new ExceptionClienteNoEncontrado("Cliente no encontrado"));

        if (dto.getNombre() != null) cliente.setNombre(dto.getNombre());
        if (dto.getApellido() != null) cliente.setApellido(dto.getApellido());
        if (dto.getCorreo() != null) cliente.setCorreo(dto.getCorreo());

        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            cliente.setContrasena(argon2Password.EncryptPassword(dto.getContrasena()));
        }

        ClienteEntity actualizado = repo.save(cliente);

        ClientDTO response = new ClientDTO();
        response.setId(actualizado.getId());
        response.setNombre(actualizado.getNombre());
        response.setApellido(actualizado.getApellido());
        response.setCorreo(actualizado.getCorreo());
        response.setDui(actualizado.getDui());
        response.setFechaNacimiento(actualizado.getFechaNacimiento());
        response.setGenero(actualizado.getGenero());

        return response;
    }
}
