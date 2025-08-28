package APISASA.API_sasa.Services.Vehiculo;

import APISASA.API_sasa.Entities.Cliente.ClienteEntity;
import APISASA.API_sasa.Entities.Estado.EstadoVehiculoEntity;
import APISASA.API_sasa.Entities.Vehiculo.VehicleEntity;
import APISASA.API_sasa.Exceptions.ExceptionVehiculoNoEcontrado;
import APISASA.API_sasa.Models.DTO.Vehiculo.VehicleDTO;
import APISASA.API_sasa.Repositories.Cliente.ClientRepository;
import APISASA.API_sasa.Repositories.Estado.EstadoRepository;
import APISASA.API_sasa.Repositories.Vehiculo.VehicleRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepo;

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private EstadoRepository estadoRepo;

    //  CONSULTAR TODOS ORDENADOS POR ID
    public List<VehicleDTO> obtenerVehiculos() {
        List<VehicleEntity> lista = vehicleRepo.findAll(Sort.by(Sort.Direction.ASC, "idVehiculo"));
        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    //  INSERTAR VEHÍCULO
    public VehicleDTO insertarVehiculo(@Valid VehicleDTO data) {
        if (data == null || data.getPlaca() == null) {
            throw new IllegalArgumentException("Datos del vehículo incompletos (placa obligatoria)");
        }

        VehicleEntity entity = convertirAEntity(data);
        entity.setIdVehiculo(null); // lo maneja la secuencia/trigger en Oracle

        VehicleEntity guardado = vehicleRepo.save(entity);
        return convertirADTO(guardado);
    }

    //  ACTUALIZAR VEHÍCULO
    public VehicleDTO actualizarVehiculo(Long id, @Valid VehicleDTO data) {
        VehicleEntity existente = vehicleRepo.findById(id)
                .orElseThrow(() -> new ExceptionVehiculoNoEcontrado("Vehículo no encontrado con ID: " + id));

        existente.setMarca(data.getMarca());
        existente.setModelo(data.getModelo());
        existente.setAnio(data.getAnio());
        existente.setPlaca(data.getPlaca());
        existente.setVin(data.getVin()); // puede ser null o exactamente 17 caracteres

        //  actualizar Cliente
        if (data.getIdCliente() != null) {
            ClienteEntity cliente = clientRepo.findById(data.getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + data.getIdCliente()));
            existente.setCliente(cliente);
        }

        //  actualizar Estado
        if (data.getIdEstado() != null) {
            EstadoVehiculoEntity estado = estadoRepo.findById(data.getIdEstado())
                    .orElseThrow(() -> new RuntimeException("Estado no encontrado con ID: " + data.getIdEstado()));
            existente.setEstado(estado);
        }

        VehicleEntity actualizado = vehicleRepo.save(existente);
        return convertirADTO(actualizado);
    }

    // ELIMINAR VEHÍCULO
    public boolean eliminarVehiculo(Long id) {
        try {
            vehicleRepo.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionVehiculoNoEcontrado("No se encontró vehículo con ID: " + id + " para eliminar.");
        }
    }


    private VehicleDTO convertirADTO(VehicleEntity entity) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(entity.getIdVehiculo());
        dto.setMarca(entity.getMarca());
        dto.setModelo(entity.getModelo());
        dto.setAnio(entity.getAnio());
        dto.setPlaca(entity.getPlaca());
        dto.setVin(entity.getVin());

        if (entity.getCliente() != null) {
            dto.setIdCliente(entity.getCliente().getId());
        }
        if (entity.getEstado() != null) {
            dto.setIdEstado(entity.getEstado().getId());
        }

        return dto;
    }


    private VehicleEntity convertirAEntity(VehicleDTO dto) {
        VehicleEntity entity = new VehicleEntity();
        entity.setMarca(dto.getMarca());
        entity.setModelo(dto.getModelo());
        entity.setAnio(dto.getAnio());
        entity.setPlaca(dto.getPlaca());
        entity.setVin(dto.getVin()); // null permitido

        if (dto.getIdCliente() != null) {
            ClienteEntity cliente = clientRepo.findById(dto.getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + dto.getIdCliente()));
            entity.setCliente(cliente);
        }

        if (dto.getIdEstado() != null) {
            EstadoVehiculoEntity estado = estadoRepo.findById(dto.getIdEstado())
                    .orElseThrow(() -> new RuntimeException("Estado no encontrado con ID: " + dto.getIdEstado()));
            entity.setEstado(estado);
        }

        return entity;
    }

    public Page<VehicleDTO> obtenerVehiculosPaginado(Pageable pageable) {
        return vehicleRepo.findAll(pageable)
                .map(this::convertirADTO);
    }
 }

