package prueba.com.example.almaceneronuevo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.almaceneronuevo.services.HistorialService;

@RestController
@RequestMapping("/api/historial")
public class HistorialController {

    private final HistorialService historialService;

    public HistorialController(HistorialService historialService) {
        this.historialService = historialService;
    }

    // Vista "Mi Perfil": trae únicamente las acciones del usuario indicado
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerHistorialPersonal(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(historialService.obtenerHistorialPorUsuario(usuarioId));
    }

    // Vista "Historial General": filtra según la jerarquía del solicitante (Admin = todo, Supervisor = sin Admin, Coordinador = propio)
    @GetMapping("/supervisado")
    public ResponseEntity<?> obtenerHistorialSupervisado(@RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        return ResponseEntity.ok(historialService.obtenerHistorialSupervisado(usuarioSolicitanteId));
    }
}