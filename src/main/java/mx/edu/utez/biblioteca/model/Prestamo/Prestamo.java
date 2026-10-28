package mx.edu.utez.biblioteca.model.Prestamo;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table (name = "Prestamos")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor


public class Prestamo {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionMaxima;

}
