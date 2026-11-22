package prueba.com.example.almaceneronuevo.services;

import prueba.com.example.almaceneronuevo.dto.HistorialResponseDTO;
import java.util.List;

public interface HistorialService {
    void registrarAccion(Long usuarioId, String modulo, String accion, String detalle);
    List<HistorialResponseDTO> obtenerHistorialPorUsuario(Long usuarioId);
    List<HistorialResponseDTO> obtenerHistorialSupervisado(Long usuarioSolicitanteId);
}