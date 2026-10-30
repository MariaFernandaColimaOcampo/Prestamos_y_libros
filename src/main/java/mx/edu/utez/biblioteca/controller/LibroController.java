package mx.edu.utez.biblioteca.controller;

import jakarta.validation.Valid;
import mx.edu.utez.biblioteca.model.libro.Libro;
import mx.edu.utez.biblioteca.model.libro.dto.RequestCreateLibroDTO;
import mx.edu.utez.biblioteca.service.LibroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/libros")
@CrossOrigin("*")
public class LibroController {

    private final LibroService libroService;
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }
    @GetMapping
    public ResponseEntity<List<Libro>> listar() {
        return ResponseEntity.ok(libroService.listar());
    }
    @PostMapping
    public ResponseEntity<Libro> crear(@RequestBody @Valid RequestCreateLibroDTO payload) {
        return ResponseEntity.status(201).body(libroService.crear(payload));
    }
}
