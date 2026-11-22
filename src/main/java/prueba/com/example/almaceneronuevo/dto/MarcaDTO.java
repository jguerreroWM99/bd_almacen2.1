package prueba.com.example.almaceneronuevo.dto;

import jakarta.validation.constraints.NotBlank;

public class MarcaDTO {

    private Long id;

    @NotBlank(message = "El nombre de la marca es obligatorio")
    private String nombre;

    public MarcaDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}