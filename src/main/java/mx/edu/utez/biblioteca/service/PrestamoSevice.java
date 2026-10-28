package mx.edu.utez.biblioteca.service;

import mx.edu.utez.biblioteca.dto.PrestamoResponseDTO;
import mx.edu.utez.biblioteca.dto.SolicitudPrestamoDTO;
import mx.edu.utez.biblioteca.exception.CustomException;
import mx.edu.utez.biblioteca.model.Lector.Lector;
import mx.edu.utez.biblioteca.model.Libro.Libro;
import mx.edu.utez.biblioteca.model.Prestamo.Prestamo;
import mx.edu.utez.biblioteca.model.Prestamo.estado;
import mx.edu.utez.biblioteca.repository.LectorRepository;
import mx.edu.utez.biblioteca.repository.LibroRepository;
import mx.edu.utez.biblioteca.repository.PrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LectorRepository lectorRepository;
    private final LibroRepository libroRepository;

    public PrestamoService(PrestamoRepository prestamoRepository,
                           LectorRepository lectorRepository,
                           LibroRepository libroRepository) {
        this.prestamoRepository = prestamoRepository;
        this.lectorRepository = lectorRepository;
        this.libroRepository = libroRepository;
    }

    // 1. solicitarPrestamo  50 pts
    @Transactional
    public PrestamoResponseDTO solicitarPrestamo(SolicitudPrestamoDTO dto) {
        Lector lector = lectorRepository.findById(dto.getLectorId())
                .orElseThrow(() -> new CustomException("El lector no existe."));

        if (!lector.isActivo()) {
            throw new CustomException("El lector no está activo.");
        }

        Libro libro = libroRepository.findById(dto.getLibroId())
                .orElseThrow(() -> new CustomException("El libro no existe."));

        if (libro.getEjemplaresDisponibles() < 1) {
            throw new CustomException("No hay ejemplares disponibles para este libro.");
        }

        long prestamosActivos = prestamoRepository.countByLectorIdAndEstadoIn(
                lector.getId(),
                Arrays.asList(estado.PRESTADO, estado.VENCIDO)
        );

        int limitePrestamos = lector.isMembresiaPremium() ? 5 : 3;

        if (prestamosActivos >= limitePrestamos) {
            throw new CustomException("El lector ha alcanzado el límite máximo de préstamos activos (" + limitePrestamos + ").");
        }

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro);

        Prestamo prestamo = new Prestamo();
        prestamo.setLector(lector);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucionMaxima(LocalDate.now().plusDays(14));
        prestamo.setEstado(estado.PRESTADO);

        Prestamo guardado = prestamoRepository.save(prestamo);
        return mapToDTO(guardado);
    }

    // 2. devolverLibro  30 pts
    @Transactional
    public PrestamoResponseDTO devolverLibro(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new CustomException("El préstamo no existe."));

        if (prestamo.getEstado() == estado.DEVUELTO) {
            throw new CustomException("El préstamo ya ha sido devuelto.");
        }

        prestamo.setEstado(estado.DEVUELTO);

        Libro libro = prestamo.getLibro();
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        libroRepository.save(libro);

        if (LocalDate.now().isAfter(prestamo.getFechaDevolucionMaxima())) {
            Lector lector = prestamo.getLector();
            lector.setActivo(false);
            lectorRepository.save(lector);
        }

        Prestamo guardado = prestamoRepository.save(prestamo);
        return mapToDTO(guardado);
    }

    // 3. renovarPrestamo  15 pts
    @Transactional
    public PrestamoResponseDTO renovarPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new CustomException("El préstamo no existe."));

        if (prestamo.getEstado() == estado.VENCIDO || prestamo.getEstado() == estado.DEVUELTO) {
            throw new CustomException("No se puede renovar un préstamo en estado " + prestamo.getEstado() + ".");
        }

        prestamo.setFechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima().plusDays(7));
        Prestamo guardado = prestamoRepository.save(prestamo);
        return mapToDTO(guardado);
    }

    // 4. rehabilitarLector  10 pts
    @Transactional
    public Lector rehabilitarLector(Long lectorId) {
        Lector lector = lectorRepository.findById(lectorId)
                .orElseThrow(() -> new CustomException("El lector no existe."));

        lector.setActivo(true);
        return lectorRepository.save(lector);
    }

    // 5. listarPrestamosActivosLector 10 pts
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> listarPrestamosActivosLector(Long lectorId) {
        if (!lectorRepository.existsById(lectorId)) {
            throw new CustomException("El lector no existe.");
        }

        List<Prestamo> prestamos = prestamoRepository.findByLectorIdAndEstadoIn(
                lectorId,
                Arrays.asList(estado.PRESTADO, estado.VENCIDO)
        );

        return prestamos.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PrestamoResponseDTO mapToDTO(Prestamo prestamo) {
        PrestamoResponseDTO dto = new PrestamoResponseDTO();
        dto.setId(prestamo.getId());
        dto.setLectorId(prestamo.getLector().getId());
        dto.setNombreLector(prestamo.getLector().getNombre());
        dto.setLibroId(prestamo.getLibro().getId());
        dto.setTituloLibro(prestamo.getLibro().getTitulo());
        dto.setFechaPrestamo(prestamo.getFechaPrestamo());
        dto.setFechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima());
        dto.setEstado(prestamo.getEstado());
        return dto;
    }
}