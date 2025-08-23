package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.NotificacionesEntity;
import APISASA.API_sasa.Exceptions.ExceptionNotificacionNoEncontrada;
import APISASA.API_sasa.Models.DTO.NotificacionDTO;
import APISASA.API_sasa.Repositories.NotificacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionesService {

    @Autowired
    private NotificacionesRepository repo;

    // ✅ Obtener todas las notificaciones de un usuario
    public List<NotificacionDTO> obtenerPorUsuario(Long idUsuario) {
        return repo.findByIdUsuario(idUsuario)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ✅ Marcar notificación como leída
    public NotificacionDTO marcarLeida(Long id) {
        NotificacionesEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotificacionNoEncontrada("No existe notificación con ID: " + id));

        entity.setLectura(1); // marcar como leída
        NotificacionesEntity actualizada = repo.save(entity);
        return convertirADTO(actualizada);
    }

    // ✅ Eliminar notificación
    public boolean eliminarNotificacion(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        throw new ExceptionNotificacionNoEncontrada("No se encontró notificación con ID: " + id);
    }

    // 🔁 Conversores
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
}
