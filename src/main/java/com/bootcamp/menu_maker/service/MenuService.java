package com.bootcamp.menu_maker.service;

import com.bootcamp.menu_maker.entity.Menu;
import com.bootcamp.menu_maker.entity.PlatoBase;
import com.bootcamp.menu_maker.repository.MenuRepository;
import com.bootcamp.menu_maker.repository.PlatoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final PlatoRepository platoRepository;

    public MenuService(MenuRepository menuRepository, PlatoRepository platoRepository) {
        this.menuRepository = menuRepository;
        this.platoRepository = platoRepository;
    }

    public List<Menu> obtenerTodosMenus() {
        return menuRepository.findAll();
    }

    public Optional<Menu> obtenerMenuPorId(Long id) {
        return menuRepository.findById(id);
    }

    @Transactional
    public Menu crearMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Transactional
    public Menu actualizarMenu(Long id, Menu menuActualizado) {
        return menuRepository.findById(id)
            .map(menuExistente -> {
                menuExistente.setNombre(menuActualizado.getNombre());
                menuExistente.setPlatos(menuActualizado.getPlatos()); // Actualiza la lista de platos
                return menuRepository.save(menuExistente);
            })
            .orElseThrow(() -> new RuntimeException("Menu no encontrado con id: " + id));
    }

    @Transactional
    public void eliminarMenu(Long id) {
        menuRepository.deleteById(id);
    }

    // Nuevo método: Asociar platos a un menú
    @Transactional
    public Menu agregarPlatosAMenu(Long menuId, List<Long> platosIds) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu no encontrado con id: " + menuId));
        
        List<PlatoBase> platos = platoRepository.findAllById(platosIds); // Obtiene los platos por sus IDs
        menu.getPlatos().addAll(platos); // Agrega los platos al menú
        
        return menuRepository.save(menu); // Guarda el menú actualizado
    }
    public void removePlateFromMenu(Long menuId, Long plateId) {
        // Buscar el menú por ID
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menú no encontrado con ID: " + menuId));

        // Buscar el plato por ID
        PlatoBase plato = platoRepository.findById(plateId)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado con ID: " + plateId));

        // Eliminar el plato del menú
        menu.getPlatos().remove(plato);

        // Guardar el menú actualizado
        menuRepository.save(menu);
    }
    


}
