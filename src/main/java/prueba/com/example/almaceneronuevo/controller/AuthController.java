package prueba.com.example.almaceneronuevo.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.almaceneronuevo.dto.LoginRequestDTO;
import prueba.com.example.almaceneronuevo.dto.RecuperarPasswordDTO;
import prueba.com.example.almaceneronuevo.services.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<?> recuperarPassword(@Valid @RequestBody RecuperarPasswordDTO request) {
        authService.recuperarPassword(request);
        return ResponseEntity.ok(Map.of("mensaje", "Contraseña restablecida correctamente. Ya puedes iniciar sesión."));
    }
}