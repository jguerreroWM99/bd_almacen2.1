package prueba.com.example.almaceneronuevo.services.servicesImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prueba.com.example.almaceneronuevo.dto.CategoriaDTO;
import prueba.com.example.almaceneronuevo.dto.MarcaDTO;
import prueba.com.example.almaceneronuevo.dto.ModeloDTO;
import prueba.com.example.almaceneronuevo.dto.TipoProductoDTO;
import prueba.com.example.almaceneronuevo.exception.RecursoNoEncontradoException;
import prueba.com.example.almaceneronuevo.exception.ReglaNegocioException;
import prueba.com.example.almaceneronuevo.model.Categoria;
import prueba.com.example.almaceneronuevo.model.Marca;
import prueba.com.example.almaceneronuevo.model.Modelo;
import prueba.com.example.almaceneronuevo.model.TipoProducto;
import prueba.com.example.almaceneronuevo.repository.CategoriaRepository;
import prueba.com.example.almaceneronuevo.repository.MarcaRepository;
import prueba.com.example.almaceneronuevo.repository.ModeloRepository;
import prueba.com.example.almaceneronuevo.repository.TipoProductoRepository;
import prueba.com.example.almaceneronuevo.services.CatalogoService;
import prueba.com.example.almaceneronuevo.services.HistorialService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogoServiceImpl implements CatalogoService {

    private final CategoriaRepository categoriaRepository;
    private final TipoProductoRepository tipoProductoRepository;
    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;
    private final HistorialService historialService;

    public CatalogoServiceImpl(CategoriaRepository categoriaRepository,
                               TipoProductoRepository tipoProductoRepository,
                               MarcaRepository marcaRepository,
                               ModeloRepository modeloRepository,
                               HistorialService historialService) {
        this.categoriaRepository = categoriaRepository;
        this.tipoProductoRepository = tipoProductoRepository;
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
        this.historialService = historialService;
    }

    // ==================== CATEGORÍAS ====================

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarCategorias() {
        return categoriaRepository.findAll().stream().map(c -> {
            CategoriaDTO dto = new CategoriaDTO();
            dto.setId(c.getId());
            dto.setNombre(c.getNombre());
            dto.setDescripcion(c.getDescripcion());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoriaDTO crearCategoria(CategoriaDTO dto, Long usuarioSolicitanteId) {
        if (categoriaRepository.existsByNombreIgnoreCase(dto.getNombre().trim())) {
            throw new ReglaNegocioException("Ya existe una categoría con el nombre: " + dto.getNombre());
        }
        Categoria categoria = new Categoria(dto.getNombre().trim(), dto.getDescripcion());
        Categoria guardada = categoriaRepository.save(categoria);

        historialService.registrarAccion(usuarioSolicitanteId, "CATALOGOS", "CREAR",
                "Creó la categoría: " + guardada.getNombre());

        dto.setId(guardada.getId());
        return dto;
    }

    @Override
    @Transactional
    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO dto, Long usuarioSolicitanteId) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con ID: " + id));

        categoria.setNombre(dto.getNombre().trim());
        categoria.setDescripcion(dto.getDescripcion());
        Categoria actualizada = categoriaRepository.save(categoria);

        historialService.registrarAccion(usuarioSolicitanteId, "CATALOGOS", "EDITAR",
                "Editó la categoría: " + actualizada.getNombre());

        dto.setId(actualizada.getId());
        return dto;
    }

    @Override
    @Transactional
    public void eliminarCategoria(Long id, Long usuarioSolicitanteId) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con ID: " + id));

        String nombre = categoria.getNombre();
        categoriaRepository.delete(categoria);

        historialService.registrarAccion(usuarioSolicitanteId, "CATALOGOS", "ELIMINAR",
                "Eliminó la categoría: " + nombre);
    }

    // ==================== TIPOS DE PRODUCTO ====================

    @Override
    @Transactional(readOnly = true)
    public List<TipoProductoDTO> listarTiposProducto() {
        return tipoProductoRepository.findAll().stream().map(this::mapearTipoDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoProductoDTO> listarTiposPorCategoria(Long categoriaId) {
        return tipoProductoRepository.findByCategoriaId(categoriaId).stream().map(this::mapearTipoDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TipoProductoDTO crearTipoProducto(TipoProductoDTO dto, Long usuarioSolicitanteId) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con ID: " + dto.getCategoriaId()));

        if (tipoProductoRepository.existsByNombreIgnoreCaseAndCategoriaId(dto.getNombre().trim(), categoria.getId())) {
            throw new ReglaNegocioException("El tipo de producto ya existe en esa categoría");
        }

        TipoProducto tipo = new TipoProducto();
        tipo.setNombre(dto.getNombre().trim());
        tipo.setDescripcion(dto.getDescripcion());
        tipo.setCategoria(categoria);

        TipoProducto guardado = tipoProductoRepository.save(tipo);

        historialService.registrarAccion(usuarioSolicitanteId, "CATALOGOS", "CREAR",
                "Creó el tipo de producto: " + guardado.getNombre() + " (Categoría: " + categoria.getNombre() + ")");

        return mapearTipoDTO(guardado);
    }

    @Override
    @Transactional
    public TipoProductoDTO actualizarTipoProducto(Long id, TipoProductoDTO dto, Long usuarioSolicitanteId) {
        TipoProducto tipo = tipoProductoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de producto no encontrado con ID: " + id));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con ID: " + dto.getCategoriaId()));

        tipo.setNombre(dto.getNombre().trim());
        tipo.setDescripcion(dto.getDescripcion());
        tipo.setCategoria(categoria);

        TipoProducto actualizado = tipoProductoRepository.save(tipo);

        historialService.registrarAccion(usuarioSolicitanteId, "CATALOGOS", "EDITAR",
                "Editó el tipo de producto: " + actualizado.getNombre());

        return mapearTipoDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarTipoProducto(Long id, Long usuarioSolicitanteId) {
        TipoProducto tipo = tipoProductoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de producto no encontrado con ID: " + id));

        String nombre = tipo.getNombre();
        tipoProductoRepository.delete(tipo);

        historialService.registrarAccion(usuarioSolicitanteId, "CATALOGOS", "ELIMINAR",
                "Eliminó el tipo de producto: " + nombre);
    }

    // ==================== MARCAS ====================

    @Override
    @Transactional(readOnly = true)
    public List<MarcaDTO> listarMarcas() {
        return marcaRepository.findAll().stream().map(m -> {
            MarcaDTO dto = new MarcaDTO();
            dto.setId(m.getId());
            dto.setNombre(m.getNombre());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MarcaDTO crearMarca(MarcaDTO dto, Long usuarioSolicitanteId) {
        if (marcaRepository.existsByNombreIgnoreCase(dto.getNombre().trim())) {
            throw new ReglaNegocioException("Ya existe una marca con el nombre: " + dto.getNombre());
        }

        Marca marca = new Marca(dto.getNombre().trim());
        Marca guardada = marcaRepository.save(marca);

        historialService.registrarAccion(usuarioSolicitanteId, "CATALOGOS", "CREAR",
                "Creó la marca: " + guardada.getNombre());

        dto.setId(guardada.getId());
        return dto;
    }

    @Override
    @Transactional
    public MarcaDTO actualizarMarca(Long id, MarcaDTO dto, Long usuarioSolicitanteId) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Marca no encontrada con ID: " + id));

        marca.setNombre(dto.getNombre().trim());
        Marca actualizada = marcaRepository.save(marca);

        historialService.registrarAccion(usuarioSolicitanteId, "CATALOGOS", "EDITAR",
                "Editó la marca: " + actualizada.getNombre());

        dto.setId(actualizada.getId());
        return dto;
    }

    @Override
    @Transactional
    public void eliminarMarca(Long id, Long usuarioSolicitanteId) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Marca no encontrada con ID: " + id));

        String nombre = marca.getNombre();
        marcaRepository.delete(marca);

        historialService.registrarAccion(usuarioSolicitanteId, "CATALOGOS", "ELIMINAR",
                "Eliminó la marca: " + nombre);
    }

    // ==================== MODELOS ====================

    @Override
    @Transactional(readOnly = true)
    public List<ModeloDTO> listarModelos() {
        return modeloRepository.findAll().stream().map(this::mapearModeloDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModeloDTO> listarModelosPorMarca(Long marcaId) {
        return modeloRepository.findByMarcaId(marcaId).stream().map(this::mapearModeloDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ModeloDTO crearModelo(ModeloDTO dto, Long usuarioSolicitanteId) {
        Marca marca = marcaRepository.findById(dto.getMarcaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Marca no encontrada con ID: " + dto.getMarcaId()));

        if (modeloRepository.existsByNombreIgnoreCaseAndMarcaId(dto.getNombre().trim(), marca.getId())) {
            throw new ReglaNegocioException("El modelo ya existe para la marca " + marca.getNombre());
        }

        Modelo modelo = new Modelo(dto.getNombre().trim(), marca);
        Modelo guardado = modeloRepository.save(modelo);

        historialService.registrarAccion(usuarioSolicitanteId, "CATALOGOS", "CREAR",
                "Creó el modelo: " + guardado.getNombre() + " (Marca: " + marca.getNombre() + ")");

        return mapearModeloDTO(guardado);
    }

    @Override
    @Transactional
    public ModeloDTO actualizarModelo(Long id, ModeloDTO dto, Long usuarioSolicitanteId) {
        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Modelo no encontrado con ID: " + id));

        Marca marca = marcaRepository.findById(dto.getMarcaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Marca no encontrada con ID: " + dto.getMarcaId()));

        modelo.setNombre(dto.getNombre().trim());
        modelo.setMarca(marca);
        Modelo actualizado = modeloRepository.save(modelo);

        historialService.registrarAccion(usuarioSolicitanteId, "CATALOGOS", "EDITAR",
                "Editó el modelo: " + actualizado.getNombre());

        return mapearModeloDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarModelo(Long id, Long usuarioSolicitanteId) {
        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Modelo no encontrado con ID: " + id));

        String nombre = modelo.getNombre();
        modeloRepository.delete(modelo);

        historialService.registrarAccion(usuarioSolicitanteId, "CATALOGOS", "ELIMINAR",
                "Eliminó el modelo: " + nombre);
    }

    // ==================== MAPPERS ====================

    private TipoProductoDTO mapearTipoDTO(TipoProducto t) {
        TipoProductoDTO dto = new TipoProductoDTO();
        dto.setId(t.getId());
        dto.setNombre(t.getNombre());
        dto.setDescripcion(t.getDescripcion());
        dto.setCategoriaId(t.getCategoria().getId());
        dto.setCategoriaNombre(t.getCategoria().getNombre());
        return dto;
    }

    private ModeloDTO mapearModeloDTO(Modelo m) {
        ModeloDTO dto = new ModeloDTO();
        dto.setId(m.getId());
        dto.setNombre(m.getNombre());
        dto.setMarcaId(m.getMarca().getId());
        dto.setMarcaNombre(m.getMarca().getNombre());
        return dto;
    }
}