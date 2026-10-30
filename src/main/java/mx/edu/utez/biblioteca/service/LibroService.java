package mx.edu.utez.biblioteca.service;

import mx.edu.utez.biblioteca.model.libro.Libro;
import mx.edu.utez.biblioteca.model.libro.dto.RequestCreateLibroDTO;
import mx.edu.utez.biblioteca.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LibroService {
    private final LibroRepository libroRepository;
    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }
    @Transactional(readOnly = true)
    public List<Libro> listar() {
        return libroRepository.findAll();
    }
    @Transactional
    public Libro crear(RequestCreateLibroDTO payload) {
        Libro libro = new Libro();
        libro.setTitulo(payload.getTitulo());
        libro.setEjemplaresDisponibles(payload.getEjemplaresDisponibles());
        return libroRepository.save(libro);
    }
}
