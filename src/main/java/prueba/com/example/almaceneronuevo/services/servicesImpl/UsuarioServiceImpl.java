package prueba.com.example.almaceneronuevo.servicesImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prueba.com.example.almaceneronuevo.dto.CambioPasswordDTO;
import prueba.com.example.almaceneronuevo.dto.UsuarioRequestDTO;
import prueba.com.example.almaceneronuevo.dto.UsuarioResponseDTO;
import prueba.com.example.almaceneronuevo.exception.AccesoDenegadoRolException;
import prueba.com.example.almaceneronuevo.exception.RecursoNoEncontradoException;
import prueba.com.example.almaceneronuevo.exception.ReglaNegocioException;
import prueba.com.example.almaceneronuevo.model.Rol;
import prueba.com.example.almaceneronuevo.model.Usuario;
import prueba.com.example.almaceneronuevo.repository.RolRepository;
import prueba.com.example.almaceneronuevo.repository.UsuarioRepository;
import prueba.com.example.almaceneronuevo.services.HistorialService;
import prueba.com.example.almaceneronuevo.services.UsuarioService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final HistorialService historialService;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              HistorialService historialService) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.historialService = historialService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));
        return mapearADTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request, Long usuarioSolicitanteId) {
        Usuario solicitante = obtenerUsuarioSolicitante(usuarioSolicitanteId);
        int nivelSolicitante = solicitante.getRol().getNivelJerarquia();

        if (nivelSolicitante == 3) {
            throw new AccesoDenegadoRolException("El rol Coordinador no tiene permisos para crear usuarios.");
        }

        Rol rolAsignado = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con ID: " + request.getRolId()));

        if (nivelSolicitante == 2 && rolAsignado.getNivelJerarquia() != 3) {
            throw new AccesoDenegadoRolException("Un Supervisor únicamente puede crear usuarios con rol Coordinador.");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new ReglaNegocioException("La contraseña es obligatoria para crear un nuevo usuario.");
        }

        if (usuarioRepository.existsByUsername(request.getUsername().trim())) {
            throw new ReglaNegocioException("El nombre de usuario '" + request.getUsername() + "' ya está en uso.");
        }

        if (usuarioRepository.existsByCorreo(request.getCorreo().trim())) {
            throw new ReglaNegocioException("Ya existe un usuario registrado con el correo: " + request.getCorreo());
        }

        if (usuarioRepository.existsByDocumentoIdentidad(request.getDocumentoIdentidad().trim())) {
            throw new ReglaNegocioException("Ya existe un usuario con el documento: " + request.getDocumentoIdentidad());
        }

        Usuario nuevo = new Usuario();
        nuevo.setUsername(request.getUsername().trim());
        nuevo.setNombres(request.getNombres().trim());
        nuevo.setApellidos(request.getApellidos().trim());
        nuevo.setDocumentoIdentidad(request.getDocumentoIdentidad().trim());
        nuevo.setFechaNacimiento(request.getFechaNacimiento());
        nuevo.setCorreo(request.getCorreo().trim());
        nuevo.setPassword(request.getPassword());
        nuevo.setActivo(request.getActivo() != null ? request.getActivo() : true);
        nuevo.setRol(rolAsignado);

        Usuario guardado = usuarioRepository.save(nuevo);

        historialService.registrarAccion(
                usuarioSolicitanteId,
                "USUARIOS",
                "CREAR",
                "Creó al usuario '" + guardado.getUsername() + "' (" + guardado.getNombres() + " " + guardado.getApellidos() + ") con rol " + rolAsignado.getNombre()
        );

        return mapearADTO(guardado);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO request, Long usuarioSolicitanteId) {
        Usuario solicitante = obtenerUsuarioSolicitante(usuarioSolicitanteId);
        int nivelSolicitante = solicitante.getRol().getNivelJerarquia();

        if (nivelSolicitante != 1) {
            throw new AccesoDenegadoRolException("Solo el Administrador puede editar usuarios.");
        }

        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));

        Rol rolAsignado = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con ID: " + request.getRolId()));

        if (!existente.getUsername().equalsIgnoreCase(request.getUsername().trim())
                && usuarioRepository.existsByUsername(request.getUsername().trim())) {
            throw new ReglaNegocioException("El nombre de usuario ya está en uso por otro usuario.");
        }

        if (!existente.getCorreo().equalsIgnoreCase(request.getCorreo().trim())
                && usuarioRepository.existsByCorreo(request.getCorreo().trim())) {
            throw new ReglaNegocioException("El correo ya está en uso por otro usuario.");
        }

        if (!existente.getDocumentoIdentidad().equalsIgnoreCase(request.getDocumentoIdentidad().trim())
                && usuarioRepository.existsByDocumentoIdentidad(request.getDocumentoIdentidad().trim())) {
            throw new ReglaNegocioException("El documento de identidad ya está en uso por otro usuario.");
        }

        existente.setUsername(request.getUsername().trim());
        existente.setNombres(request.getNombres().trim());
        existente.setApellidos(request.getApellidos().trim());
        existente.setDocumentoIdentidad(request.getDocumentoIdentidad().trim());
        existente.setFechaNacimiento(request.getFechaNacimiento());
        existente.setCorreo(request.getCorreo().trim());
        existente.setRol(rolAsignado);

        if (request.getActivo() != null) {
            existente.setActivo(request.getActivo());
        }

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            existente.setPassword(request.getPassword());
        }

        Usuario actualizado = usuarioRepository.save(existente);

        historialService.registrarAccion(
                usuarioSolicitanteId,
                "USUARIOS",
                "EDITAR",
                "Editó los datos del usuario '" + actualizado.getUsername() + "' (ID: " + actualizado.getId() + ")"
        );

        return mapearADTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id, Long usuarioSolicitanteId) {
        Usuario solicitante = obtenerUsuarioSolicitante(usuarioSolicitanteId);

        if (solicitante.getRol().getNivelJerarquia() != 1) {
            throw new AccesoDenegadoRolException("Solo el Administrador puede eliminar usuarios.");
        }

        if (id.equals(usuarioSolicitanteId)) {
            throw new ReglaNegocioException("No puedes eliminar tu propio usuario mientras estás en sesión.");
        }

        Usuario objetivo = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));

        String infoEliminado = objetivo.getUsername() + " (" + objetivo.getNombres() + " " + objetivo.getApellidos() + ")";
        usuarioRepository.delete(objetivo);

        historialService.registrarAccion(
                usuarioSolicitanteId,
                "USUARIOS",
                "ELIMINAR",
                "Eliminó al usuario " + infoEliminado
        );
    }

    @Override
    @Transactional
    public void cambiarPassword(Long usuarioId, CambioPasswordDTO request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        if (!usuario.getPassword().equals(request.getPasswordActual())) {
            throw new ReglaNegocioException("La contraseña actual ingresada es incorrecta.");
        }

        usuario.setPassword(request.getPasswordNueva());
        usuarioRepository.save(usuario);

        historialService.registrarAccion(
                usuarioId,
                "PERFIL",
                "CAMBIO_PASSWORD",
                "Actualizó su contraseña desde su perfil"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> listarRolesDisponibles(Long usuarioSolicitanteId) {
        Usuario solicitante = obtenerUsuarioSolicitante(usuarioSolicitanteId);
        int nivel = solicitante.getRol().getNivelJerarquia();

        if (nivel == 1) {
            return rolRepository.findAll();
        } else if (nivel == 2) {
            return rolRepository.findAll().stream()
                    .filter(r -> r.getNivelJerarquia() == 3)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    private Usuario obtenerUsuarioSolicitante(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario solicitante no válido"));
    }

    private UsuarioResponseDTO mapearADTO(Usuario u) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setNombres(u.getNombres());
        dto.setApellidos(u.getApellidos());
        dto.setDocumentoIdentidad(u.getDocumentoIdentidad());
        dto.setFechaNacimiento(u.getFechaNacimiento());
        dto.setCorreo(u.getCorreo());
        dto.setActivo(u.getActivo());
        dto.setFechaCreacion(u.getFechaCreacion());
        dto.setRolId(u.getRol().getId());
        dto.setRolNombre(u.getRol().getNombre());
        dto.setNivelJerarquia(u.getRol().getNivelJerarquia());
        return dto;
    }
}