package mx.edu.utez.biblioteca.service;

import mx.edu.utez.biblioteca.exception.customException.CustomNotFoundException;
import mx.edu.utez.biblioteca.model.lector.Lector;
import mx.edu.utez.biblioteca.model.lector.dto.RequestCreateLectorDTO;
import mx.edu.utez.biblioteca.repository.LectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LectorService {

    private final LectorRepository lectorRepository;

    public LectorService(LectorRepository lectorRepository) {
        this.lectorRepository = lectorRepository;
    }

    @Transactional(readOnly = true)
    public List<Lector> listar() {
        return lectorRepository.findAll();
    }

    @Transactional
    public Lector crear(RequestCreateLectorDTO payload) {
        Lector lector = new Lector();
        lector.setNombre(payload.getNombre());
        lector.setMembresiaPremium(payload.isMembresiaPremium());
        lector.setActivo(true);
        return lectorRepository.save(lector);
    }

    // 4. rehabilitarLector
    @Transactional
    public Lector rehabilitarLector(Long id) {
        Lector lector = lectorRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("El lector con id " + id + " no existe"));
        lector.setActivo(true);
        return lectorRepository.save(lector);
    }
}
