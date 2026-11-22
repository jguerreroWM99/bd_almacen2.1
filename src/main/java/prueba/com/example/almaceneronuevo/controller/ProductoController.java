package prueba.com.example.almaceneronuevo.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.almaceneronuevo.dto.ProductoRequestDTO;
import prueba.com.example.almaceneronuevo.services.ProductoService;

import java.util.Map;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<?> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestBody ProductoRequestDTO request,
                                           @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        return new ResponseEntity<>(productoService.crearProducto(request, usuarioSolicitanteId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id,
                                                @Valid @RequestBody ProductoRequestDTO request,
                                                @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, request, usuarioSolicitanteId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id,
                                              @RequestHeader("X-User-Id") Long usuarioSolicitanteId) {
        productoService.eliminarProducto(id, usuarioSolicitanteId);
        return ResponseEntity.ok(Map.of("mensaje", "Producto eliminado correctamente"));
    }
}