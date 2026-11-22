package prueba.com.example.almaceneronuevo.services;

import prueba.com.example.almaceneronuevo.dto.LoginRequestDTO;
import prueba.com.example.almaceneronuevo.dto.RecuperarPasswordDTO;
import prueba.com.example.almaceneronuevo.dto.UsuarioResponseDTO;

public interface AuthService {
    UsuarioResponseDTO login(LoginRequestDTO request);
    void recuperarPassword(RecuperarPasswordDTO request);
}