package mx.edu.utez.biblioteca.model.Libro;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Libros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Libro {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long Id;
    private String titulo;
    private int ejemplaresDisponibles;


}
