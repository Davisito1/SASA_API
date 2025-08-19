package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.VehicleEntity;
import APISASA.API_sasa.Exceptions.ExceptionVehiculoNoEcontrado;
import APISASA.API_sasa.Models.DTO.VehicleDTO;
import APISASA.API_sasa.Repositories.VehicleRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@CrossOrigin
public class VehicleService {

    @Autowired
    private VehicleRepository repo;

    // CONSULTAR TODOS
    public List<VehicleDTO> obtenerVehiculos() {
        List<VehicleEntity> lista = repo.findAll();
        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // INSERTAR VEHÍCULO
    public VehicleDTO insertarVehiculo(VehicleDTO data) {
        if (data == null || data.getPlaca() == null || data.getVin() == null) {
            throw new IllegalArgumentException("Datos del vehículo incompletos");
        }

        try {
            VehicleEntity entity = convertirAEntity(data);
            VehicleEntity guardado = repo.save(entity);
            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar vehículo: {}", e.getMessage());
            throw new RuntimeException("No se pudo registrar el vehículo");
        }
    }

    // ACTUALIZAR VEHÍCULO
    public VehicleDTO actualizarVehiculo(Long id, @Valid VehicleDTO data) {
        VehicleEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionVehiculoNoEcontrado("Vehículo no encontrado con ID: " + id));

        existente.setMarca(data.getMarca());
        existente.setModelo(data.getModelo());
        existente.setAnio(data.getAnio());
        existente.setPlaca(data.getPlaca());
        existente.setVin(data.getVin());
        existente.setIdCliente(data.getIdCliente());
        existente.setIdEstado(data.getIdEstado());

        VehicleEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    // ELIMINAR VEHÍCULO
    public boolean eliminarVehiculo(Long id) {
        try {
            VehicleEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("No se encontró vehículo con ID: " + id + " para eliminar.");
        }
    }

    // CONVERTIR ENTITY → DTO
    private VehicleDTO convertirADTO(VehicleEntity entity) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(entity.getId());
        dto.setMarca(entity.getMarca());
        dto.setModelo(entity.getModelo());
        dto.setAnio(entity.getAnio());
        dto.setPlaca(entity.getPlaca());
        dto.setVin(entity.getVin());
        dto.setIdCliente(entity.getIdCliente());
        dto.setIdEstado(entity.getIdEstado());
        return dto;
    }

    // CONVERTIR DTO → ENTITY
    private VehicleEntity convertirAEntity(VehicleDTO dto) {
        VehicleEntity entity = new VehicleEntity();
        entity.setMarca(dto.getMarca());
        entity.setModelo(dto.getModelo());
        entity.setAnio(dto.getAnio());
        entity.setPlaca(dto.getPlaca());
        entity.setVin(dto.getVin());
        entity.setIdCliente(dto.getIdCliente());
        entity.setIdEstado(dto.getIdEstado());
        return entity;
    }
}
