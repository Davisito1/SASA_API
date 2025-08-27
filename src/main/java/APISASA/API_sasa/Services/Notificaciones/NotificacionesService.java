package APISASA.API_sasa.Services.Notificaciones;

import APISASA.API_sasa.Entities.Notificaciones.NotificacionesEntity;
import APISASA.API_sasa.Entities.Usuario.UserEntity;
import APISASA.API_sasa.Exceptions.ExceptionNotificacionNoEncontrada;
import APISASA.API_sasa.Models.DTO.Notificaciones.NotificacionDTO;
import APISASA.API_sasa.Repositories.Notificaciones.NotificacionesRepository;
import APISASA.API_sasa.Repositories.Usuario.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionesService {

    @Autowired
    private NotificacionesRepository repo;

    @Autowired
    private UserRepository userRepo;

    // ✅ Obtener todas las notificaciones de un usuario
    public List<NotificacionDTO> obtenerPorUsuario(Long idUsuario) {
        return repo.findByUsuario_IdUsuario(idUsuario) // relación con UserEntity
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ✅ Crear nueva notificación
    public NotificacionDTO crearNotificacion(NotificacionDTO dto) {
        NotificacionesEntity entity = new NotificacionesEntity();
        entity.setMensaje(dto.getMensaje());
        entity.setFecha(dto.getFecha());
        entity.setTipoNotificacion(dto.getTipoNotificacion());
        entity.setLectura(dto.getLectura() != null ? dto.getLectura() : 0);
        entity.setPrioridad(dto.getPrioridad());

        // 🔹 Vincular con usuario existente
        UserEntity usuario = userRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getIdUsuario()));
        entity.setUsuario(usuario);

        NotificacionesEntity guardada = repo.save(entity);
        return convertirADTO(guardada);
    }

    // ✅ Marcar notificación como leída
    public NotificacionDTO marcarLeida(Long id) {
        NotificacionesEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotificacionNoEncontrada("No existe notificación con ID: " + id));

        entity.setLectura(1); // marcar como leída
        return convertirADTO(repo.save(entity));
    }

    // ✅ Eliminar notificación
    public boolean eliminarNotificacion(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        throw new ExceptionNotificacionNoEncontrada("No se encontró notificación con ID: " + id);
    }

    // ==========================
    // 🔹 Conversor Entity → DTO
    // ==========================
    private NotificacionDTO convertirADTO(NotificacionesEntity entity) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(entity.getIdNotificacion()); // usa el nombre real de la columna
        dto.setMensaje(entity.getMensaje());
        dto.setFecha(entity.getFecha());
        dto.setTipoNotificacion(entity.getTipoNotificacion());
        dto.setLectura(entity.getLectura());
        dto.setPrioridad(entity.getPrioridad());

        if (entity.getUsuario() != null) {
            dto.setIdUsuario(entity.getUsuario().getIdUsuario()); // usa getIdUsuario de UserEntity
        }

        return dto;
    }
}
