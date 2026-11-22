package prueba.com.example.almaceneronuevo.services;

import prueba.com.example.almaceneronuevo.dto.CambioPasswordDTO;
import prueba.com.example.almaceneronuevo.dto.UsuarioRequestDTO;
import prueba.com.example.almaceneronuevo.dto.UsuarioResponseDTO;
import prueba.com.example.almaceneronuevo.model.Rol;

import java.util.List;

public interface UsuarioService {
    List<UsuarioResponseDTO> listarUsuarios();
    UsuarioResponseDTO obtenerPorId(Long id);
    UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request, Long usuarioSolicitanteId);
    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO request, Long usuarioSolicitanteId);
    void eliminarUsuario(Long id, Long usuarioSolicitanteId);
    void cambiarPassword(Long usuarioId, CambioPasswordDTO request);
    List<Rol> listarRolesDisponibles(Long usuarioSolicitanteId);
}