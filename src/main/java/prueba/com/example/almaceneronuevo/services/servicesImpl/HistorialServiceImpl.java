package prueba.com.example.almaceneronuevo.services.servicesImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prueba.com.example.almaceneronuevo.dto.HistorialResponseDTO;
import prueba.com.example.almaceneronuevo.exception.RecursoNoEncontradoException;
import prueba.com.example.almaceneronuevo.model.HistorialActividad;
import prueba.com.example.almaceneronuevo.model.Usuario;
import prueba.com.example.almaceneronuevo.repository.HistorialRepository;
import prueba.com.example.almaceneronuevo.repository.UsuarioRepository;
import prueba.com.example.almaceneronuevo.services.HistorialService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialServiceImpl implements HistorialService {

    private final HistorialRepository historialRepository;
    private final UsuarioRepository usuarioRepository;

    public HistorialServiceImpl(HistorialRepository historialRepository, UsuarioRepository usuarioRepository) {
        this.historialRepository = historialRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public void registrarAccion(Long usuarioId, String modulo, String accion, String detalle) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario ejecutor no encontrado con ID: " + usuarioId));

        HistorialActividad actividad = new HistorialActividad(usuario, modulo, accion, detalle);
        historialRepository.save(actividad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialResponseDTO> obtenerHistorialPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con ID: " + usuarioId);
        }
        return historialRepository.findByUsuarioIdOrderByFechaHoraDesc(usuarioId)
                .stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialResponseDTO> obtenerHistorialSupervisado(Long usuarioSolicitanteId) {
        Usuario solicitante = usuarioRepository.findById(usuarioSolicitanteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario solicitante no encontrado"));

        int nivel = solicitante.getRol().getNivelJerarquia();

        List<HistorialActividad> registros;
        if (nivel == 1) {
            // ADMIN: ve absolutamente todo el historial del sistema
            registros = historialRepository.findAllByOrderByFechaHoraDesc();
        } else if (nivel == 2) {
            // SUPERVISOR: ve historial de Supervisor (2) y Coordinador (3), excluye Admin (1)
            registros = historialRepository.findByUsuarioRolNivelJerarquiaGreaterThanEqualOrderByFechaHoraDesc(2);
        } else {
            // COORDINADOR: solo ve su propio historial
            registros = historialRepository.findByUsuarioIdOrderByFechaHoraDesc(usuarioSolicitanteId);
        }

        return registros.stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    private HistorialResponseDTO mapearADTO(HistorialActividad h) {
        HistorialResponseDTO dto = new HistorialResponseDTO();
        dto.setId(h.getId());
        dto.setUsuarioId(h.getUsuario().getId());
        dto.setUsuarioNombreCompleto(h.getUsuario().getNombres() + " " + h.getUsuario().getApellidos());
        dto.setUsuarioRol(h.getUsuario().getRol().getNombre());
        dto.setModulo(h.getModulo());
        dto.setAccion(h.getAccion());
        dto.setDetalle(h.getDetalle());
        dto.setFechaHora(h.getFechaHora());
        return dto;
    }
}