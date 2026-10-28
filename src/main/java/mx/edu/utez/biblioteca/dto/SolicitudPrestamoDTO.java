package mx.edu.utez.biblioteca.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudPrestamoDTO {

    @NotNull(message = "El ID del lector es obligatorio")
    private Long lectorId;

    @NotNull(message = "El ID del libro es obligatorio")
    private Long libroId;
}