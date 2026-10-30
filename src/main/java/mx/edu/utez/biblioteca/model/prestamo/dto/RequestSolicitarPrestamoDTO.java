package mx.edu.utez.biblioteca.model.prestamo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestSolicitarPrestamoDTO {

    @NotNull(message = "El id del lector es obligatorio")
    @Positive(message = "El id del lector debe ser positivo")
    private Long lectorId;

    @NotNull(message = "El id del libro es obligatorio")
    @Positive(message = "El id del libro debe ser positivo")
    private Long libroId;
}
