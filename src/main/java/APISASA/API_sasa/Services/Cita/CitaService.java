package APISASA.API_sasa.Services.Cita;

import APISASA.API_sasa.Entities.Cita.CitaEntity;
import APISASA.API_sasa.Entities.Cliente.ClienteEntity;
import APISASA.API_sasa.Entities.Vehiculo.VehicleEntity;
import APISASA.API_sasa.Exceptions.ExceptionCitaNoEncontrada;
import APISASA.API_sasa.Models.DTO.Cita.CitaDTO;
import APISASA.API_sasa.Repositories.Cita.CitaRepository;
import APISASA.API_sasa.Repositories.Cliente.ClientRepository;
import APISASA.API_sasa.Repositories.Vehiculo.VehicleRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CitaService {

    @Autowired
    private CitaRepository repo;

    @Autowired
    private ClientRepository repoClient;

    @Autowired
    private VehicleRepository repoVehiculo;

    // ============================
    // LISTAR TODAS
    // ============================
    public List<CitaDTO> obtenerCitas() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ============================
    // LISTAR PAGINADO
    // ============================
    public Page<CitaDTO> obtenerCitas(Pageable pageable) {
        return repo.findAll(pageable).map(this::convertirADTO);
    }

    // ============================
    // OBTENER POR ID
    // ============================
    public CitaDTO obtenerCitaPorId(Long id) {
        return repo.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new ExceptionCitaNoEncontrada("No se encontró cita con ID: " + id));
    }

    // ============================
    // CREAR
    // ============================
    public CitaDTO insertarCita(@Valid CitaDTO dto) {
        CitaEntity entity = convertirAEntity(dto);
        entity.setId(null); // secuencia genera el ID
        CitaEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    // ============================
    // ACTUALIZAR
    // ============================
    public CitaDTO actualizarCita(Long id, @Valid CitaDTO dto) {
        CitaEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionCitaNoEncontrada("No se encontró cita con ID: " + id));

        existente.setFecha(dto.getFecha());
        existente.setHora(dto.getHora());
        existente.setEstado(dto.getEstado());
        existente.setDescripcion(dto.getDescripcion());
        existente.setTipoServicio(dto.getTipoServicio());

        // cliente obligatorio según DTO
        ClienteEntity cliente = repoClient.getReferenceById(dto.getIdCliente());
        existente.setCliente(cliente);

        // vehículo obligatorio según DTO
        VehicleEntity vehiculo = repoVehiculo.getReferenceById(dto.getIdVehiculo());
        existente.setVehiculo(vehiculo);

        CitaEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    // ============================
    // ELIMINAR
    // ============================
    public boolean eliminarCita(Long id) {
        try {
            repo.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionCitaNoEncontrada("No se encontró cita con ID: " + id + " para eliminar.");
        }
    }

    // ============================
    // MAPPERS
    // ============================
    private CitaEntity convertirAEntity(CitaDTO dto) {
        CitaEntity entity = new CitaEntity();
        entity.setId(dto.getId());
        entity.setFecha(dto.getFecha());
        entity.setHora(dto.getHora());
        entity.setEstado(dto.getEstado());
        entity.setDescripcion(dto.getDescripcion());
        entity.setTipoServicio(dto.getTipoServicio());

        // cliente obligatorio
        ClienteEntity cliente = repoClient.getReferenceById(dto.getIdCliente());
        entity.setCliente(cliente);

        // vehículo obligatorio
        VehicleEntity vehiculo = repoVehiculo.getReferenceById(dto.getIdVehiculo());
        entity.setVehiculo(vehiculo);

        return entity;
    }

    private CitaDTO convertirADTO(CitaEntity entity) {
        CitaDTO dto = new CitaDTO();
        dto.setId(entity.getId());
        dto.setFecha(entity.getFecha());
        dto.setHora(entity.getHora());
        dto.setEstado(entity.getEstado());
        dto.setDescripcion(entity.getDescripcion());
        dto.setTipoServicio(entity.getTipoServicio());

        if (entity.getCliente() != null) {
            dto.setIdCliente(entity.getCliente().getId());
            dto.setClienteNombre(entity.getCliente().getNombre() + " " + entity.getCliente().getApellido());
        }

        if (entity.getVehiculo() != null) {
            dto.setIdVehiculo(entity.getVehiculo().getIdVehiculo());
            dto.setVehiculoNombre(entity.getVehiculo().getMarca() + " " + entity.getVehiculo().getModelo());
        }

        return dto;
    }

}

