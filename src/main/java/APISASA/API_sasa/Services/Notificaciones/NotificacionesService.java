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

    // 🔹 Obtener todas las notificaciones de un usuario
    public List<NotificacionDTO> obtenerPorUsuario(Long idUsuario) {
        return repo.findByUsuario_IdUsuario(idUsuario)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // 🔹 Crear nueva notificación
    public NotificacionDTO crearNotificacion(NotificacionDTO dto) {
        NotificacionesEntity entity = new NotificacionesEntity();
        entity.setMensaje(dto.getMensaje());
        entity.setTipoNotificacion(dto.getTipoNotificacion());
        entity.setPrioridad(dto.getPrioridad());

        // Vincular con usuario existente
        UserEntity usuario = userRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getIdUsuario()));
        entity.setUsuario(usuario);

        // ⚡ fecha y lectura se setean solos en @PrePersist
        NotificacionesEntity guardada = repo.save(entity);
        return convertirADTO(guardada);
    }

    // 🔹 Marcar notificación como leída
    public NotificacionDTO marcarLeida(Long id) {
        NotificacionesEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotificacionNoEncontrada("No existe notificación con ID: " + id));

        entity.setLectura(1); // 1 = leída
        return convertirADTO(repo.save(entity));
    }

    // 🔹 Eliminar notificación
    public boolean eliminarNotificacion(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false; // el Controller devuelve 404 si es false
    }

    // 🔹 Convertir Entity → DTO
    private NotificacionDTO convertirADTO(NotificacionesEntity entity) {
        return NotificacionDTO.builder()
                .id(entity.getIdNotificacion())
                .mensaje(entity.getMensaje())
                .tipoNotificacion(entity.getTipoNotificacion())
                .prioridad(entity.getPrioridad())
                .idUsuario(entity.getUsuario() != null ? entity.getUsuario().getIdUsuario() : null)
                .build();
    }
}
