package mx.edu.utez.biblioteca.controller;

import jakarta.validation.Valid;
import mx.edu.utez.biblioteca.model.lector.Lector;
import mx.edu.utez.biblioteca.model.lector.dto.RequestCreateLectorDTO;
import mx.edu.utez.biblioteca.service.LectorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lectores")
@CrossOrigin("*")
public class LectorController {

    private final LectorService lectorService;
    public LectorController(LectorService lectorService) {
        this.lectorService = lectorService;
    }
    @GetMapping
    public ResponseEntity<List<Lector>> listar() {
        return ResponseEntity.ok(lectorService.listar());
    }
    @PostMapping
    public ResponseEntity<Lector> crear(@RequestBody @Valid RequestCreateLectorDTO payload) {
        return ResponseEntity.status(201).body(lectorService.crear(payload));
    }
    @PatchMapping("/{id}/rehabilitar")
    public ResponseEntity<Lector> rehabilitar(@PathVariable Long id) {
        return ResponseEntity.ok(lectorService.rehabilitarLector(id));
    }
}
