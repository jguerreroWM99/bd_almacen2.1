package prueba.com.example.almaceneronuevo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import prueba.com.example.almaceneronuevo.model.*;
import prueba.com.example.almaceneronuevo.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final TipoProductoRepository tipoProductoRepository;
    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;
    private final ProductoRepository productoRepository;
    private final HistorialRepository historialRepository;

    public DataInitializer(RolRepository rolRepository,
                           UsuarioRepository usuarioRepository,
                           CategoriaRepository categoriaRepository,
                           TipoProductoRepository tipoProductoRepository,
                           MarcaRepository marcaRepository,
                           ModeloRepository modeloRepository,
                           ProductoRepository productoRepository,
                           HistorialRepository historialRepository) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.tipoProductoRepository = tipoProductoRepository;
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
        this.productoRepository = productoRepository;
        this.historialRepository = historialRepository;
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
        Usuario admin = null;
        if (usuarioRepository.count() == 0) {
            // Usuario 1: Administrador
            admin = new Usuario();
            admin.setUsername("admin");
            admin.setNombres("Carlos");
            admin.setApellidos("Mendoza");
            admin.setDocumentoIdentidad("11111111");
            admin.setFechaNacimiento(LocalDate.of(1999, 2, 27));
            admin.setCorreo("admin@jose.com");
            admin.setPassword("admin");
            admin.setActivo(true);
            admin.setRol(rolAdmin);
            admin = usuarioRepository.save(admin);

            // Usuario 2: Supervisor
            Usuario supervisor = new Usuario();
            supervisor.setUsername("super");
            supervisor.setNombres("Lucía");
            supervisor.setApellidos("Fernández");
            supervisor.setDocumentoIdentidad("22222222");
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
            coordinador.setDocumentoIdentidad("33333333");
            coordinador.setFechaNacimiento(LocalDate.of(1999, 2, 27));
            coordinador.setCorreo("jose@jose.com");
            coordinador.setPassword("jose123");
            coordinador.setActivo(true);
            coordinador.setRol(rolCoordinador);
            usuarioRepository.save(coordinador);
        } else {
            admin = usuarioRepository.findByUsername("admin").orElse(null);
        }

        // 3. Cargar catálogos y 10 productos tecnológicos iniciales si el inventario está vacío
        if (productoRepository.count() == 0) {
            // Categorías
            Categoria catComputo = categoriaRepository.save(new Categoria("Cómputo", "Equipos portátiles y estaciones de trabajo"));
            Categoria catPerifericos = categoriaRepository.save(new Categoria("Periféricos", "Monitores, teclados, ratones y accesorios externos"));
            Categoria catRedes = categoriaRepository.save(new Categoria("Redes", "Equipos de conectividad, routers y switches"));
            Categoria catComponentes = categoriaRepository.save(new Categoria("Componentes", "Memorias, discos sólidos y hardware interno"));

            // Tipos de Producto
            TipoProducto tipoLaptop = crearTipo("Laptop", "Computadoras portátiles empresariales", catComputo);
            TipoProducto tipoMonitor = crearTipo("Monitor", "Pantallas LED / IPS de escritorio", catPerifericos);
            TipoProducto tipoTeclado = crearTipo("Teclado", "Teclados mecánicos y de membrana", catPerifericos);
            TipoProducto tipoMouse = crearTipo("Mouse", "Ratones inalámbricos y ergonómicos", catPerifericos);
            TipoProducto tipoRouter = crearTipo("Router", "Enrutadores Wi-Fi empresariales", catRedes);
            TipoProducto tipoSsd = crearTipo("Almacenamiento SSD", "Unidades de estado sólido NVMe y SATA", catComponentes);

            // Marcas
            Marca marcaDell = marcaRepository.save(new Marca("Dell"));
            Marca marcaLenovo = marcaRepository.save(new Marca("Lenovo"));
            Marca marcaLogitech = marcaRepository.save(new Marca("Logitech"));
            Marca marcaTpLink = marcaRepository.save(new Marca("TP-Link"));
            Marca marcaKingston = marcaRepository.save(new Marca("Kingston"));

            // Modelos
            Modelo modLatitude = modeloRepository.save(new Modelo("Latitude 5440", marcaDell));
            Modelo modUltraSharp = modeloRepository.save(new Modelo("UltraSharp U2723QE", marcaDell));
            Modelo modThinkPad = modeloRepository.save(new Modelo("ThinkPad T14 Gen 4", marcaLenovo));
            Modelo modMxMaster = modeloRepository.save(new Modelo("MX Master 3S", marcaLogitech));
            Modelo modMxKeys = modeloRepository.save(new Modelo("MX Keys S", marcaLogitech));
            Modelo modArcher = modeloRepository.save(new Modelo("Archer AX73", marcaTpLink));
            Modelo modNv2 = modeloRepository.save(new Modelo("NV2 PCIe 4.0", marcaKingston));

            // 10 Productos Tecnológicos
            guardarProductoInicial("TEC-001", "Laptop Dell Latitude 14\"", "Intel Core i7-1355U, 16GB RAM DDR4, 512GB SSD NVMe, Windows 11 Pro", new BigDecimal("4250.00"), 12, tipoLaptop, modLatitude, admin);
            guardarProductoInicial("TEC-002", "Laptop Lenovo ThinkPad T14", "AMD Ryzen 7 PRO, 16GB RAM, 1TB SSD, Pantalla 14\" FHD Antirreflejo", new BigDecimal("4590.00"), 8, tipoLaptop, modThinkPad, admin);
            guardarProductoInicial("TEC-003", "Monitor Dell UltraSharp 27\" 4K", "Panel IPS Black 3840x2160, USB-C Hub 90W, Ajuste de altura", new BigDecimal("2150.00"), 15, tipoMonitor, modUltraSharp, admin);
            guardarProductoInicial("TEC-004", "Mouse Logitech MX Master 3S", "Sensor 8000 DPI, Clics silenciosos, Bluetooth / Logi Bolt, Grafito", new BigDecimal("389.00"), 25, tipoMouse, modMxMaster, admin);
            guardarProductoInicial("TEC-005", "Teclado Logitech MX Keys S", "Inalámbrico retroiluminado, perfil bajo, multidispositivo", new BigDecimal("449.00"), 20, tipoTeclado, modMxKeys, admin);
            guardarProductoInicial("TEC-006", "Router TP-Link Archer AX73", "Wi-Fi 6 AX5400 Doble Banda, 6 antenas de alta ganancia, Gigabit", new BigDecimal("520.00"), 10, tipoRouter, modArcher, admin);
            guardarProductoInicial("TEC-007", "SSD Kingston NV2 1TB M.2", "Formato M.2 2280 NVMe PCIe 4.0 x4, lectura hasta 3500 MB/s", new BigDecimal("245.00"), 35, tipoSsd, modNv2, admin);
            guardarProductoInicial("TEC-008", "SSD Kingston NV2 500GB M.2", "Formato M.2 2280 NVMe PCIe 4.0 x4, lectura hasta 3500 MB/s", new BigDecimal("155.00"), 40, tipoSsd, modNv2, admin);
            guardarProductoInicial("TEC-009", "Laptop Dell Latitude 14\" Core i5", "Intel Core i5-1335U, 8GB RAM, 512GB SSD, Teclado retroiluminado", new BigDecimal("3490.00"), 14, tipoLaptop, modLatitude, admin);
            guardarProductoInicial("TEC-010", "Combo Productividad Logitech MX", "Pack teclado MX Keys S + Mouse MX Master 3S color gris pálido", new BigDecimal("790.00"), 9, tipoTeclado, modMxKeys, admin);

            System.out.println(">>> Inventario inicial cargado: 4 categorías, 6 tipos, 5 marcas, 7 modelos y 10 productos.");
        }
    }

    private TipoProducto crearTipo(String nombre, String descripcion, Categoria categoria) {
        TipoProducto tipo = new TipoProducto();
        tipo.setNombre(nombre);
        tipo.setDescripcion(descripcion);
        tipo.setCategoria(categoria);
        return tipoProductoRepository.save(tipo);
    }

    private void guardarProductoInicial(String sku, String nombre, String specs, BigDecimal precio,
                                        int stock, TipoProducto tipo, Modelo modelo, Usuario creador) {
        Producto p = new Producto();
        p.setCodigoSku(sku);
        p.setNombre(nombre);
        p.setEspecificaciones(specs);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setTipoProducto(tipo);
        p.setModelo(modelo);
        Producto guardado = productoRepository.save(p);

        if (creador != null) {
            historialRepository.save(new HistorialActividad(
                    creador,
                    "PRODUCTOS",
                    "CREAR",
                    "Carga inicial del producto '" + guardado.getNombre() + "' (SKU: " + guardado.getCodigoSku() + ", Stock: " + guardado.getStock() + ")"
            ));
        }
    }
}