package mx.edu.utez.biblioteca.model.prestamo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.biblioteca.model.prestamo.EstadoPrestamo;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePrestamoDTO {
    private Long id;
    private Long lectorId;
    private String lectorNombre;
    private Long libroId;
    private String libroTitulo;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionMaxima;
    private EstadoPrestamo estado;
}
