package mx.edu.utez.biblioteca.service;

import mx.edu.utez.biblioteca.exception.customException.CustomBadRequestException;
import mx.edu.utez.biblioteca.exception.customException.CustomNotFoundException;
import mx.edu.utez.biblioteca.model.lector.Lector;
import mx.edu.utez.biblioteca.model.libro.Libro;
import mx.edu.utez.biblioteca.model.prestamo.EstadoPrestamo;
import mx.edu.utez.biblioteca.model.prestamo.Prestamo;
import mx.edu.utez.biblioteca.model.prestamo.dto.RequestSolicitarPrestamoDTO;
import mx.edu.utez.biblioteca.model.prestamo.dto.ResponsePrestamoDTO;
import mx.edu.utez.biblioteca.repository.LectorRepository;
import mx.edu.utez.biblioteca.repository.LibroRepository;
import mx.edu.utez.biblioteca.repository.PrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {
    private static final int LIMITE_NORMAL = 3;
    private static final int LIMITE_PREMIUM = 5;
    private static final int DIAS_PRESTAMO = 14;
    private static final int DIAS_RENOVACION = 7;
    private static final List<EstadoPrestamo> ESTADOS_ACTIVOS =
            List.of(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO);
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
    @Transactional
    public ResponsePrestamoDTO solicitarPrestamo(RequestSolicitarPrestamoDTO payload) {
        // Lector existe y esta activo
        Lector lector = buscarLector(payload.getLectorId());
        if (!lector.isActivo()) {
            throw new CustomBadRequestException("El lector esta bloqueado (inactivo)");
        }
        Libro libro = libroRepository.findById(payload.getLibroId())
                .orElseThrow(() -> new CustomNotFoundException(
                        "El libro con id " + payload.getLibroId() + " no existe"));
        if (libro.getEjemplaresDisponibles() < 1) {
            throw new CustomBadRequestException("No hay ejemplares disponibles de este libro");
        }

        // Limite de prestamos activos (3 normal, 5 premium)
        int limite = lector.isMembresiaPremium() ? LIMITE_PREMIUM : LIMITE_NORMAL;
        long activos = prestamoRepository.countByLectorIdAndEstadoIn(lector.getId(), ESTADOS_ACTIVOS);
        if (activos >= limite) {
            throw new CustomBadRequestException(
                    "El lector alcanzo su limite de " + limite + " prestamos activos");
        }

        // Restar ejemplar
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro);

        // Crear prestamo
        Prestamo prestamo = new Prestamo();
        prestamo.setLector(lector);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucionMaxima(LocalDate.now().plusDays(DIAS_PRESTAMO));
        prestamo.setEstado(EstadoPrestamo.PRESTADO);
        return convertir(prestamoRepository.save(prestamo));
    }

    // 2. devolverLibro (30 pts)
    @Transactional
    public ResponsePrestamoDTO devolverLibro(Long prestamoId) {
        Prestamo prestamo = buscarPrestamo(prestamoId);
        if (!ESTADOS_ACTIVOS.contains(prestamo.getEstado())) {
            throw new CustomBadRequestException("El prestamo ya fue devuelto");
        }

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);

        Libro libro = prestamo.getLibro();
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        libroRepository.save(libro);

        // Entrega tardia -> se bloquea al lector
        if (LocalDate.now().isAfter(prestamo.getFechaDevolucionMaxima())) {
            Lector lector = prestamo.getLector();
            lector.setActivo(false);
            lectorRepository.save(lector);
        }
        return convertir(prestamoRepository.save(prestamo));
    }

    // 3. renovarPrestamo (15 pts)
    @Transactional
    public ResponsePrestamoDTO renovarPrestamo(Long prestamoId) {
        Prestamo prestamo = buscarPrestamo(prestamoId);
        if (prestamo.getEstado() == EstadoPrestamo.VENCIDO
                || prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new CustomBadRequestException(
                    "No se puede renovar un prestamo en estado " + prestamo.getEstado());
        }
        prestamo.setFechaDevolucionMaxima(
                prestamo.getFechaDevolucionMaxima().plusDays(DIAS_RENOVACION));
        return convertir(prestamoRepository.save(prestamo));
    }
    @Transactional(readOnly = true)
    public List<ResponsePrestamoDTO> listarPrestamosActivosLector(Long lectorId) {
        buscarLector(lectorId);
        return prestamoRepository.findByLectorIdAndEstadoIn(lectorId, ESTADOS_ACTIVOS)
                .stream()
                .map(this::convertir)
                .toList();
    }
    private Lector buscarLector(Long id) {
        return lectorRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("El lector con id " + id + " no existe"));
    }
    private Prestamo buscarPrestamo(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("El prestamo con id " + id + " no existe"));
    }
    private ResponsePrestamoDTO convertir(Prestamo p) {
        return new ResponsePrestamoDTO(
                p.getId(),
                p.getLector().getId(),
                p.getLector().getNombre(),
                p.getLibro().getId(),
                p.getLibro().getTitulo(),
                p.getFechaPrestamo(),
                p.getFechaDevolucionMaxima(),
                p.getEstado());
    }
}
