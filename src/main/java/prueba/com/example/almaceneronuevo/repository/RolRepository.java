package prueba.com.example.almaceneronuevo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.com.example.almaceneronuevo.model.Rol;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}