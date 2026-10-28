package mx.edu.utez.biblioteca.dto;

import mx.edu.utez.biblioteca.model.Prestamo.estado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoResponseDTO {

    private Long id;
    private Long lectorId;
    private String nombreLector;
    private Long libroId;
    private String tituloLibro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionMaxima;
    private estado estado;
}