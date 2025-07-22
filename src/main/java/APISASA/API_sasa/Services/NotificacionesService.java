package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.NotificacionesEntity;
import APISASA.API_sasa.Entities.NotificacionesEntity;
import APISASA.API_sasa.Exceptions.ExceptionNotificacionNoEncontrada;
import APISASA.API_sasa.Models.DTO.NotificacionDTO;
import APISASA.API_sasa.Repositories.NotificacionesRepository;
import APISASA.API_sasa.Repositories.NotificacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionesService {

    @Autowired
    private NotificacionesRepository repo;

    // Obtener todas las notificaciones
    public List<NotificacionDTO> obtenerNotificaciones() {
        List<NotificacionesEntity> lista = repo.findAll();
        return lista.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    // Insertar nueva notificación
    public NotificacionDTO insertarNotificacion(NotificacionDTO dto) {
        NotificacionesEntity entity = convertirAEntity(dto);
        NotificacionesEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    // Actualizar notificación
    public NotificacionDTO actualizarNotificacion(Long id, NotificacionDTO dto) {
        NotificacionesEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotificacionNoEncontrada("No existe una notificación con ID: " + id));

        existente.setMensaje(dto.getMensaje());
        existente.setFecha(dto.getFecha());
        existente.setTipoNotificacion(dto.getTipoNotificacion());
        existente.setLectura(dto.getLectura());
        existente.setPrioridad(dto.getPrioridad());
        existente.setIdUsuario(dto.getIdUsuario());

        NotificacionesEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    // Eliminar notificación
    public boolean eliminarNotificacion(Long id) {
        try {
            NotificacionesEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNotificacionNoEncontrada("No se encontró la notificación con ID: " + id + " para eliminar.");
        }
    }

    // Convertir entidad a DTO
    private NotificacionDTO convertirADTO(NotificacionesEntity entity) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(entity.getId());
        dto.setMensaje(entity.getMensaje());
        dto.setFecha(entity.getFecha());
        dto.setTipoNotificacion(entity.getTipoNotificacion());
        dto.setLectura(entity.getLectura());
        dto.setPrioridad(entity.getPrioridad());
        dto.setIdUsuario(entity.getIdUsuario());
        return dto;
    }

    // Convertir DTO a entidad
    private NotificacionesEntity convertirAEntity(NotificacionDTO dto) {
        NotificacionesEntity entity = new NotificacionesEntity();
        entity.setMensaje(dto.getMensaje());
        entity.setFecha(dto.getFecha());
        entity.setTipoNotificacion(dto.getTipoNotificacion());
        entity.setLectura(dto.getLectura());
        entity.setPrioridad(dto.getPrioridad());
        entity.setIdUsuario(dto.getIdUsuario());
        return entity;
    }
}
