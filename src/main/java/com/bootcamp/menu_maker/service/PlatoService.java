package com.bootcamp.menu_maker.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bootcamp.menu_maker.entity.Menu;
import com.bootcamp.menu_maker.entity.PlatoBase;
import com.bootcamp.menu_maker.entity.Postre;
import com.bootcamp.menu_maker.entity.Primeros;
import com.bootcamp.menu_maker.entity.Segundos;
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
            // 1. Actualizar campos comunes
            platoExistente.setNombre(platoActualizado.getNombre());
            platoExistente.setPrecio(platoActualizado.getPrecio());
            platoExistente.setDescripcion(platoActualizado.getDescripcion());
            
            // 2. Si cambia el tipo, recrear la entidad
            if (!platoExistente.getTipoPlato().equals(platoActualizado.getTipoPlato())) {
                // Copiar relaciones existentes
                List<Menu> menusRelacionados = platoExistente.getMenus(); 
                
                // Crear nueva instancia del tipo correcto
                PlatoBase nuevoPlato = crearInstanciaPorTipo(platoActualizado.getTipoPlato());
                nuevoPlato.setId(platoExistente.getId());
                nuevoPlato.setNombre(platoExistente.getNombre());
                nuevoPlato.setPrecio(platoExistente.getPrecio());
                nuevoPlato.setDescripcion(platoExistente.getDescripcion());
                nuevoPlato.setMenus(menusRelacionados); // Mantener relaciones
                
                // Eliminar instancia anterior y guardar nueva
                platoRepository.delete(platoExistente);
                return platoRepository.save(nuevoPlato);
            }
            
            // 3. Si no cambia el tipo, guardar normalmente
            return platoRepository.save(platoExistente);
        })
        .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
}

private PlatoBase crearInstanciaPorTipo(String tipo) {
    return switch(tipo) {
        case "PRIMEROS" -> new Primeros();
        case "SEGUNDOS" -> new Segundos();
        case "POSTRE" -> new Postre();
        default -> throw new IllegalArgumentException("Tipo inválido: " + tipo);
    };
}


    @Transactional
    public void eliminarPlato(Long id) {
        platoRepository.deleteById(id);
    }
}
