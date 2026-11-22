package prueba.com.example.almaceneronuevo.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.almaceneronuevo.dto.CambioPasswordDTO;
import prueba.com.example.almaceneronuevo.dto.UsuarioRequestDTO;
import prueba.com.example.almaceneronuevo.services.UsuarioService;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody UsuarioRequestDTO request,
                                          @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        return new ResponseEntity<>(usuarioService.crearUsuario(request, usuarioSolicitanteId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id,
                                               @Valid @RequestBody UsuarioRequestDTO request,
                                               @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, request, usuarioSolicitanteId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id,
                                             @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        usuarioService.eliminarUsuario(id, usuarioSolicitanteId);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado correctamente"));
    }

    @PutMapping("/{id}/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@PathVariable Long id,
                                             @Valid @RequestBody CambioPasswordDTO request) {
        usuarioService.cambiarPassword(id, request);
        return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada correctamente"));
    }

    @GetMapping("/roles-disponibles")
    public ResponseEntity<?> listarRolesDisponibles(@RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        return ResponseEntity.ok(usuarioService.listarRolesDisponibles(usuarioSolicitanteId));
    }
}