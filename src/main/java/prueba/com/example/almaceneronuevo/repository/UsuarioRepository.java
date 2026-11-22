package prueba.com.example.almaceneronuevo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.com.example.almaceneronuevo.model.Usuario;

import java.time.LocalDate;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByCorreo(String correo);
    boolean existsByDocumentoIdentidad(String documentoIdentidad);

    // Validación de 4 datos para recuperación de contraseña
    Optional<Usuario> findByUsernameAndDocumentoIdentidadAndFechaNacimientoAndCorreo(
            String username,
            String documentoIdentidad,
            LocalDate fechaNacimiento,
            String correo
    );
}