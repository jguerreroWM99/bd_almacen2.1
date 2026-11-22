package prueba.com.example.almaceneronuevo.servicesImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prueba.com.example.almaceneronuevo.dto.LoginRequestDTO;
import prueba.com.example.almaceneronuevo.dto.RecuperarPasswordDTO;
import prueba.com.example.almaceneronuevo.dto.UsuarioResponseDTO;
import prueba.com.example.almaceneronuevo.exception.ReglaNegocioException;
import prueba.com.example.almaceneronuevo.model.Usuario;
import prueba.com.example.almaceneronuevo.repository.UsuarioRepository;
import prueba.com.example.almaceneronuevo.services.AuthService;
import prueba.com.example.almaceneronuevo.services.HistorialService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final HistorialService historialService;

    public AuthServiceImpl(UsuarioRepository usuarioRepository, HistorialService historialService) {
        this.usuarioRepository = usuarioRepository;
        this.historialService = historialService;
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername().trim())
                .orElseThrow(() -> new ReglaNegocioException("Usuario o contraseña incorrectos"));

        if (!usuario.getActivo()) {
            throw new ReglaNegocioException("El usuario se encuentra inactivo. Contacte al administrador.");
        }

        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new ReglaNegocioException("Usuario o contraseña incorrectos");
        }

        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(usuario.getId());
        response.setUsername(usuario.getUsername());
        response.setNombres(usuario.getNombres());
        response.setApellidos(usuario.getApellidos());
        response.setDocumentoIdentidad(usuario.getDocumentoIdentidad());
        response.setFechaNacimiento(usuario.getFechaNacimiento());
        response.setCorreo(usuario.getCorreo());
        response.setActivo(usuario.getActivo());
        response.setFechaCreacion(usuario.getFechaCreacion());
        response.setRolId(usuario.getRol().getId());
        response.setRolNombre(usuario.getRol().getNombre());
        response.setNivelJerarquia(usuario.getRol().getNivelJerarquia());

        return response;
    }

    @Override
    @Transactional
    public void recuperarPassword(RecuperarPasswordDTO request) {
        Usuario usuario = usuarioRepository.findByUsernameAndDocumentoIdentidadAndFechaNacimientoAndCorreo(
                request.getUsername().trim(),
                request.getDocumentoIdentidad().trim(),
                request.getFechaNacimiento(),
                request.getCorreo().trim()
        ).orElseThrow(() -> new ReglaNegocioException(
                "Los datos ingresados no coinciden con ningún usuario registrado."
        ));

        usuario.setPassword(request.getNuevaPassword());
        usuarioRepository.save(usuario);

        historialService.registrarAccion(
                usuario.getId(),
                "PERFIL",
                "RECUPERAR_PASSWORD",
                "Restableció su contraseña mediante verificación de identidad"
        );
    }
}