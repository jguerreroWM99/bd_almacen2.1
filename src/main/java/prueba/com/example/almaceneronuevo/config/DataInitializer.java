package prueba.com.example.almaceneronuevo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import prueba.com.example.almaceneronuevo.model.Rol;
import prueba.com.example.almaceneronuevo.model.Usuario;
import prueba.com.example.almaceneronuevo.repository.RolRepository;
import prueba.com.example.almaceneronuevo.repository.UsuarioRepository;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    public DataInitializer(RolRepository rolRepository, UsuarioRepository usuarioRepository) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        // 1. Crear roles base si no existen
        Rol rolAdmin = rolRepository.findByNombre("ADMIN")
                .orElseGet(() -> rolRepository.save(
                        new Rol("ADMIN", 1, "Jefe del programa con acceso total a usuarios, productos e historial")
                ));

        Rol rolSupervisor = rolRepository.findByNombre("SUPERVISOR")
                .orElseGet(() -> rolRepository.save(
                        new Rol("SUPERVISOR", 2, "Gestión de inventario, creación de coordinadores y supervisión de historial")
                ));

        Rol rolCoordinador = rolRepository.findByNombre("COORDINADOR")
                .orElseGet(() -> rolRepository.save(
                        new Rol("COORDINADOR", 3, "Gestión de inventario y visualización de usuarios e historial propio")
                ));

        // 2. Crear los 3 usuarios iniciales si la tabla está vacía
        if (usuarioRepository.count() == 0) {
            // Usuario 1: Administrador
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setNombres("Carlos");
            admin.setApellidos("Mendoza");
            admin.setDocumentoIdentidad("11111111");
            admin.setFechaNacimiento(LocalDate.of(1999, 2, 27));
            admin.setCorreo("admin@jose.com");
            admin.setPassword("admin");
            admin.setActivo(true);
            admin.setRol(rolAdmin);
            usuarioRepository.save(admin);

            // Usuario 2: Supervisor
            Usuario supervisor = new Usuario();
            supervisor.setUsername("super");
            supervisor.setNombres("Lucía");
            supervisor.setApellidos("Fernández");
            supervisor.setDocumentoIdentidad("11111111");
            supervisor.setFechaNacimiento(LocalDate.of(1999, 2, 27));
            supervisor.setCorreo("super@jose.com");
            supervisor.setPassword("super");
            supervisor.setActivo(true);
            supervisor.setRol(rolSupervisor);
            usuarioRepository.save(supervisor);

            // Usuario 3: Coordinador
            Usuario coordinador = new Usuario();
            coordinador.setUsername("jose123");
            coordinador.setNombres("José");
            coordinador.setApellidos("Ramírez");
            coordinador.setDocumentoIdentidad("11111111");
            coordinador.setFechaNacimiento(LocalDate.of(1999, 2, 27));
            coordinador.setCorreo("jose@jose.com");
            coordinador.setPassword("jose123");
            coordinador.setActivo(true);
            coordinador.setRol(rolCoordinador);
            usuarioRepository.save(coordinador);

        }
    }
}