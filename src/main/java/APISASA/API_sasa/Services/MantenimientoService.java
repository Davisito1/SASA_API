package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.MantenimientoEntity;
import APISASA.API_sasa.Entities.VehicleEntity;
import APISASA.API_sasa.Exceptions.ExceptionMantenimientoNoEncontrado;
import APISASA.API_sasa.Models.DTO.MantenimientoDTO;
import APISASA.API_sasa.Repositories.MantenimientoRepository;
import APISASA.API_sasa.Repositories.VehicleRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MantenimientoService {

    @Autowired
    private MantenimientoRepository repo;

    @Autowired
    private VehicleRepository vehicleRepo;

    public List<MantenimientoDTO> obtenerMantenimientos() {
        return repo.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Page<MantenimientoDTO> obtenerMantenimientosPaginado(String q, Pageable pageable) {
        if (q == null || q.isBlank()) {
            return repo.findAll(pageable).map(this::convertirADTO);
        }

        String term = q.trim();

        try {
            Long id = Long.parseLong(term);

            var opt = repo.findById(id);
            if (opt.isPresent()) {
                return new PageImpl<>(List.of(convertirADTO(opt.get())), pageable, 1);
            }

            return repo.findByVehiculo_IdVehiculo(id, pageable).map(this::convertirADTO);

        } catch (NumberFormatException ignore) {
            return repo.findByDescripcionTrabajoContainingIgnoreCaseOrCodigoMantenimientoContainingIgnoreCase(
                    term, term, pageable
            ).map(this::convertirADTO);
        }
    }

    public MantenimientoDTO insertarMantenimiento(MantenimientoDTO data) {
        if (data == null) throw new IllegalArgumentException("Los datos del mantenimiento no pueden ser nulos");

        MantenimientoEntity entity = convertirAEntity(data);
        entity.setId(null);

        String codigo = generarCodigo();
        entity.setCodigoMantenimiento(codigo);

        MantenimientoEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    public MantenimientoDTO actualizarMantenimiento(Long id, @Valid MantenimientoDTO data) {
        MantenimientoEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionMantenimientoNoEncontrado("No se encontró mantenimiento con ID: " + id));

        existente.setDescripcionTrabajo(data.getDescripcion());
        existente.setFechaRealizacion(data.getFechaRealizacion());

        if (data.getCodigoMantenimiento() != null && !data.getCodigoMantenimiento().isBlank()) {
            existente.setCodigoMantenimiento(data.getCodigoMantenimiento());
        }

        if (data.getIdVehiculo() != null) {
            VehicleEntity vehiculo = vehicleRepo.findById(data.getIdVehiculo())
                    .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con ID: " + data.getIdVehiculo()));
            existente.setVehiculo(vehiculo);
        }

        return convertirADTO(repo.save(existente));
    }

    public boolean eliminarMantenimiento(Long id) {
        try {
            repo.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionMantenimientoNoEncontrado("No se encontró mantenimiento con ID: " + id + " para eliminar.");
        }
    }

    private String generarCodigo() {
        int year = LocalDate.now().getYear();
        Integer ultimo = repo.findUltimoCorrelativo(year);
        int siguiente = (ultimo != null ? ultimo + 1 : 1);
        return String.format("MTN-%d-%03d", year, siguiente);
    }

    private MantenimientoEntity convertirAEntity(MantenimientoDTO dto) {
        MantenimientoEntity entity = new MantenimientoEntity();
        entity.setId(dto.getId());
        entity.setDescripcionTrabajo(dto.getDescripcion());
        entity.setFechaRealizacion(dto.getFechaRealizacion());
        entity.setCodigoMantenimiento(dto.getCodigoMantenimiento());

        if (dto.getIdVehiculo() != null) {
            VehicleEntity vehiculo = vehicleRepo.findById(dto.getIdVehiculo())
                    .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con ID: " + dto.getIdVehiculo()));
            entity.setVehiculo(vehiculo);
        }

        return entity;
    }

    private MantenimientoDTO convertirADTO(MantenimientoEntity entity) {
        MantenimientoDTO dto = new MantenimientoDTO();
        dto.setId(entity.getId());
        dto.setDescripcion(entity.getDescripcionTrabajo());
        dto.setFechaRealizacion(entity.getFechaRealizacion());
        dto.setCodigoMantenimiento(entity.getCodigoMantenimiento());

        if (entity.getVehiculo() != null) {
            dto.setIdVehiculo(entity.getVehiculo().getIdVehiculo());
        }

        return dto;
    }
}
