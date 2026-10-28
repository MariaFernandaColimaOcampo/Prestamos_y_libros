package mx.edu.utez.biblioteca.controller;

import mx.edu.utez.biblioteca.dto.PrestamoResponseDTO;
import mx.edu.utez.biblioteca.dto.SolicitudPrestamoDTO;
import mx.edu.utez.biblioteca.model.Lector.Lector;
import mx.edu.utez.biblioteca.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@CrossOrigin("*")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<PrestamoResponseDTO> solicitarPrestamo(@Valid @RequestBody SolicitudPrestamoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoService.solicitarPrestamo(dto));
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<PrestamoResponseDTO> devolverLibro(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.devolverLibro(id));
    }

    @PutMapping("/{id}/renovar")
    public ResponseEntity<PrestamoResponseDTO> renovarPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.renovarPrestamo(id));
    }

    @PutMapping("/lectores/{id}/rehabilitar")
    public ResponseEntity<Lector> rehabilitarLector(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.rehabilitarLector(id));
    }

    @GetMapping("/lectores/{id}/activos")
    public ResponseEntity<List<PrestamoResponseDTO>> listarPrestamosActivosLector(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.listarPrestamosActivosLector(id));
    }
}