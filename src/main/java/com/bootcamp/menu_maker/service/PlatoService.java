package com.bootcamp.menu_maker.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bootcamp.menu_maker.entity.PlatoBase;
import com.bootcamp.menu_maker.repository.PlatoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PlatoService {

    private final PlatoRepository platoRepository;

    public PlatoService(PlatoRepository platoRepository) {
        this.platoRepository = platoRepository;
    }

    public List<PlatoBase> obtenerTodosPlatos() {
        return platoRepository.findAll();
    }

    public Optional<PlatoBase> obtenerPlatoPorId(Long id) {
        return platoRepository.findById(id);
    }

    @Transactional
    public PlatoBase crearPlato(PlatoBase plato) {
        return platoRepository.save(plato);
    }

    @Transactional
    public PlatoBase actualizarPlato(Long id, PlatoBase platoActualizado) {
        return platoRepository.findById(id)
            .map(platoExistente -> {
                platoExistente.setNombre(platoActualizado.getNombre());
                platoExistente.setPrecio(platoActualizado.getPrecio());
                platoExistente.setDescripcion(platoActualizado.getDescripcion());
                return platoRepository.save(platoExistente);
            })
            .orElseThrow(() -> new RuntimeException("Plato no encontrado con id: " + id));
    }

    @Transactional
    public void eliminarPlato(Long id) {
        platoRepository.deleteById(id);
    }
}
