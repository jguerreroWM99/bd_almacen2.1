package prueba.com.example.almaceneronuevo.services.servicesImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prueba.com.example.almaceneronuevo.dto.ProductoRequestDTO;
import prueba.com.example.almaceneronuevo.dto.ProductoResponseDTO;
import prueba.com.example.almaceneronuevo.exception.RecursoNoEncontradoException;
import prueba.com.example.almaceneronuevo.exception.ReglaNegocioException;
import prueba.com.example.almaceneronuevo.model.Modelo;
import prueba.com.example.almaceneronuevo.model.Producto;
import prueba.com.example.almaceneronuevo.model.TipoProducto;
import prueba.com.example.almaceneronuevo.repository.ModeloRepository;
import prueba.com.example.almaceneronuevo.repository.ProductoRepository;
import prueba.com.example.almaceneronuevo.repository.TipoProductoRepository;
import prueba.com.example.almaceneronuevo.services.HistorialService;
import prueba.com.example.almaceneronuevo.services.ProductoService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final TipoProductoRepository tipoProductoRepository;
    private final ModeloRepository modeloRepository;
    private final HistorialService historialService;

    public ProductoServiceImpl(ProductoRepository productoRepository,
                               TipoProductoRepository tipoProductoRepository,
                               ModeloRepository modeloRepository,
                               HistorialService historialService) {
        this.productoRepository = productoRepository;
        this.tipoProductoRepository = tipoProductoRepository;
        this.modeloRepository = modeloRepository;
        this.historialService = historialService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarProductos() {
        return productoRepository.findAll()
                .stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));
        return mapearADTO(producto);
    }

    @Override
    @Transactional
    public ProductoResponseDTO crearProducto(ProductoRequestDTO request, Long usuarioSolicitanteId) {
        if (productoRepository.existsByCodigoSkuIgnoreCase(request.getCodigoSku().trim())) {
            throw new ReglaNegocioException("Ya existe un producto registrado con el SKU: " + request.getCodigoSku());
        }

        TipoProducto tipo = tipoProductoRepository.findById(request.getTipoProductoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de producto no encontrado con ID: " + request.getTipoProductoId()));

        Modelo modelo = modeloRepository.findById(request.getModeloId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Modelo no encontrado con ID: " + request.getModeloId()));

        Producto producto = new Producto();
        producto.setCodigoSku(request.getCodigoSku().trim().toUpperCase());
        producto.setNombre(request.getNombre().trim());
        producto.setEspecificaciones(request.getEspecificaciones());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setTipoProducto(tipo);
        producto.setModelo(modelo);

        Producto guardado = productoRepository.save(producto);

        historialService.registrarAccion(
                usuarioSolicitanteId,
                "PRODUCTOS",
                "CREAR",
                "Creó el producto '" + guardado.getNombre() + "' (SKU: " + guardado.getCodigoSku() + ", Stock: " + guardado.getStock() + ")"
        );

        return mapearADTO(guardado);
    }

    @Override
    @Transactional
    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO request, Long usuarioSolicitanteId) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        if (!producto.getCodigoSku().equalsIgnoreCase(request.getCodigoSku().trim())
                && productoRepository.existsByCodigoSkuIgnoreCase(request.getCodigoSku().trim())) {
            throw new ReglaNegocioException("Ya existe otro producto con el SKU: " + request.getCodigoSku());
        }

        TipoProducto tipo = tipoProductoRepository.findById(request.getTipoProductoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de producto no encontrado con ID: " + request.getTipoProductoId()));

        Modelo modelo = modeloRepository.findById(request.getModeloId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Modelo no encontrado con ID: " + request.getModeloId()));

        producto.setCodigoSku(request.getCodigoSku().trim().toUpperCase());
        producto.setNombre(request.getNombre().trim());
        producto.setEspecificaciones(request.getEspecificaciones());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setTipoProducto(tipo);
        producto.setModelo(modelo);

        Producto actualizado = productoRepository.save(producto);

        historialService.registrarAccion(
                usuarioSolicitanteId,
                "PRODUCTOS",
                "EDITAR",
                "Editó el producto '" + actualizado.getNombre() + "' (SKU: " + actualizado.getCodigoSku() + ", Stock actual: " + actualizado.getStock() + ")"
        );

        return mapearADTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarProducto(Long id, Long usuarioSolicitanteId) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        String infoProducto = producto.getNombre() + " (SKU: " + producto.getCodigoSku() + ")";
        productoRepository.delete(producto);

        historialService.registrarAccion(
                usuarioSolicitanteId,
                "PRODUCTOS",
                "ELIMINAR",
                "Eliminó el producto '" + infoProducto + "'"
        );
    }

    private ProductoResponseDTO mapearADTO(Producto p) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(p.getId());
        dto.setCodigoSku(p.getCodigoSku());
        dto.setNombre(p.getNombre());
        dto.setEspecificaciones(p.getEspecificaciones());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        dto.setFechaRegistro(p.getFechaRegistro());
        dto.setFechaActualizacion(p.getFechaActualizacion());

        dto.setTipoProductoId(p.getTipoProducto().getId());
        dto.setTipoProductoNombre(p.getTipoProducto().getNombre());
        dto.setCategoriaId(p.getTipoProducto().getCategoria().getId());
        dto.setCategoriaNombre(p.getTipoProducto().getCategoria().getNombre());

        dto.setModeloId(p.getModelo().getId());
        dto.setModeloNombre(p.getModelo().getNombre());
        dto.setMarcaId(p.getModelo().getMarca().getId());
        dto.setMarcaNombre(p.getModelo().getMarca().getNombre());
        return dto;
    }
}