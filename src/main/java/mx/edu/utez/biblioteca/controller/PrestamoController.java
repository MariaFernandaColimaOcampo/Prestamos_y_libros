package mx.edu.utez.biblioteca.controller;

import jakarta.validation.Valid;
import mx.edu.utez.biblioteca.model.prestamo.dto.RequestSolicitarPrestamoDTO;
import mx.edu.utez.biblioteca.model.prestamo.dto.ResponsePrestamoDTO;
import mx.edu.utez.biblioteca.service.PrestamoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
@CrossOrigin("*")
public class PrestamoController {

    private final PrestamoService prestamoService;
    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }
    @PostMapping
    public ResponseEntity<ResponsePrestamoDTO> solicitar(@RequestBody @Valid RequestSolicitarPrestamoDTO payload) {
        return ResponseEntity.status(201).body(prestamoService.solicitarPrestamo(payload));
    }
    @PatchMapping("/{id}/devolver")
    public ResponseEntity<ResponsePrestamoDTO> devolver(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.devolverLibro(id));
    }

    @PatchMapping("/{id}/renovar")
    public ResponseEntity<ResponsePrestamoDTO> renovar(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.renovarPrestamo(id));
    }
    @GetMapping("/lector/{lectorId}/activos")
    public ResponseEntity<List<ResponsePrestamoDTO>> activosDeLector(@PathVariable Long lectorId) {
        return ResponseEntity.ok(prestamoService.listarPrestamosActivosLector(lectorId));
    }
}
