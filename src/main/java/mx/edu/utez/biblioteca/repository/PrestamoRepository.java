package mx.edu.utez.biblioteca.repository;

import mx.edu.utez.biblioteca.model.prestamo.EstadoPrestamo;
import mx.edu.utez.biblioteca.model.prestamo.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    // SELECT COUNT(*) ... WHERE lector_id = ? AND estado IN (...)
    long countByLectorIdAndEstadoIn(Long lectorId, Collection<EstadoPrestamo> estados);

    List<Prestamo> findByLectorIdAndEstadoIn(Long lectorId, Collection<EstadoPrestamo> estados);
}
