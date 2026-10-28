package mx.edu.utez.biblioteca.model.Lector;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lector")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Lector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank (message = "El nombre es oblgatorio")
    private String nombre;

    private boolean activo;

    private boolean membresiaPremium ;

}
