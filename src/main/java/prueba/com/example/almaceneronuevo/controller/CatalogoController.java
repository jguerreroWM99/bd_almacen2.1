package prueba.com.example.almaceneronuevo.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.almaceneronuevo.dto.CategoriaDTO;
import prueba.com.example.almaceneronuevo.dto.MarcaDTO;
import prueba.com.example.almaceneronuevo.dto.ModeloDTO;
import prueba.com.example.almaceneronuevo.dto.TipoProductoDTO;
import prueba.com.example.almaceneronuevo.services.CatalogoService;

import java.util.Map;

@RestController
@RequestMapping("/api/catalogos")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    // ==================== CATEGORÍAS ====================

    @GetMapping("/categorias")
    public ResponseEntity<?> listarCategorias() {
        return ResponseEntity.ok(catalogoService.listarCategorias());
    }

    @PostMapping("/categorias")
    public ResponseEntity<?> crearCategoria(@Valid @RequestBody CategoriaDTO dto,
                                            @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        return new ResponseEntity<>(catalogoService.crearCategoria(dto, usuarioSolicitanteId), HttpStatus.CREATED);
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<?> actualizarCategoria(@PathVariable Long id,
                                                 @Valid @RequestBody CategoriaDTO dto,
                                                 @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        return ResponseEntity.ok(catalogoService.actualizarCategoria(id, dto, usuarioSolicitanteId));
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id,
                                               @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        catalogoService.eliminarCategoria(id, usuarioSolicitanteId);
        return ResponseEntity.ok(Map.of("mensaje", "Categoría eliminada correctamente"));
    }

    // ==================== TIPOS DE PRODUCTO ====================

    @GetMapping("/tipos")
    public ResponseEntity<?> listarTiposProducto() {
        return ResponseEntity.ok(catalogoService.listarTiposProducto());
    }

    @GetMapping("/tipos/categoria/{categoriaId}")
    public ResponseEntity<?> listarTiposPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(catalogoService.listarTiposPorCategoria(categoriaId));
    }

    @PostMapping("/tipos")
    public ResponseEntity<?> crearTipoProducto(@Valid @RequestBody TipoProductoDTO dto,
                                               @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        return new ResponseEntity<>(catalogoService.crearTipoProducto(dto, usuarioSolicitanteId), HttpStatus.CREATED);
    }

    @PutMapping("/tipos/{id}")
    public ResponseEntity<?> actualizarTipoProducto(@PathVariable Long id,
                                                    @Valid @RequestBody TipoProductoDTO dto,
                                                    @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        return ResponseEntity.ok(catalogoService.actualizarTipoProducto(id, dto, usuarioSolicitanteId));
    }

    @DeleteMapping("/tipos/{id}")
    public ResponseEntity<?> eliminarTipoProducto(@PathVariable Long id,
                                                  @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        catalogoService.eliminarTipoProducto(id, usuarioSolicitanteId);
        return ResponseEntity.ok(Map.of("mensaje", "Tipo de producto eliminado correctamente"));
    }

    // ==================== MARCAS ====================

    @GetMapping("/marcas")
    public ResponseEntity<?> listarMarcas() {
        return ResponseEntity.ok(catalogoService.listarMarcas());
    }

    @PostMapping("/marcas")
    public ResponseEntity<?> crearMarca(@Valid @RequestBody MarcaDTO dto,
                                        @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        return new ResponseEntity<>(catalogoService.crearMarca(dto, usuarioSolicitanteId), HttpStatus.CREATED);
    }

    @PutMapping("/marcas/{id}")
    public ResponseEntity<?> actualizarMarca(@PathVariable Long id,
                                             @Valid @RequestBody MarcaDTO dto,
                                             @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        return ResponseEntity.ok(catalogoService.actualizarMarca(id, dto, usuarioSolicitanteId));
    }

    @DeleteMapping("/marcas/{id}")
    public ResponseEntity<?> eliminarMarca(@PathVariable Long id,
                                           @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        catalogoService.eliminarMarca(id, usuarioSolicitanteId);
        return ResponseEntity.ok(Map.of("mensaje", "Marca eliminada correctamente"));
    }

    // ==================== MODELOS ====================

    @GetMapping("/modelos")
    public ResponseEntity<?> listarModelos() {
        return ResponseEntity.ok(catalogoService.listarModelos());
    }

    @GetMapping("/modelos/marca/{marcaId}")
    public ResponseEntity<?> listarModelosPorMarca(@PathVariable Long marcaId) {
        return ResponseEntity.ok(catalogoService.listarModelosPorMarca(marcaId));
    }

    @PostMapping("/modelos")
    public ResponseEntity<?> crearModelo(@Valid @RequestBody ModeloDTO dto,
                                         @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        return new ResponseEntity<>(catalogoService.crearModelo(dto, usuarioSolicitanteId), HttpStatus.CREATED);
    }

    @PutMapping("/modelos/{id}")
    public ResponseEntity<?> actualizarModelo(@PathVariable Long id,
                                              @Valid @RequestBody ModeloDTO dto,
                                              @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        return ResponseEntity.ok(catalogoService.actualizarModelo(id, dto, usuarioSolicitanteId));
    }

    @DeleteMapping("/modelos/{id}")
    public ResponseEntity<?> eliminarModelo(@PathVariable Long id,
                                            @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        catalogoService.eliminarModelo(id, usuarioSolicitanteId);
        return ResponseEntity.ok(Map.of("mensaje", "Modelo eliminado correctamente"));
    }
}