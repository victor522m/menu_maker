package com.bootcamp.menu_maker.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bootcamp.menu_maker.entity.Menu;
import com.bootcamp.menu_maker.entity.PlatoBase;
import com.bootcamp.menu_maker.service.MenuService;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public List<Menu> obtenerTodosMenus() {
        return menuService.obtenerTodosMenus();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> obtenerMenuPorId(@PathVariable Long id) {
        return menuService.obtenerMenuPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Menu crearMenu(@RequestBody Menu menu) {
        return menuService.crearMenu(menu);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Menu> actualizarMenu(@PathVariable Long id, @RequestBody Menu menu) {
        try {
            Menu menuActualizado = menuService.actualizarMenu(id, menu);
            return ResponseEntity.ok(menuActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMenu(@PathVariable Long id) {
        menuService.eliminarMenu(id);
        return ResponseEntity.noContent().build();
    }

    // Nuevo método: Obtener platos asociados a un menú
    @GetMapping("/{id}/platos")
    public ResponseEntity<List<PlatoBase>> obtenerPlatosDeMenu(@PathVariable Long id) {
        return menuService.obtenerMenuPorId(id)
                .map(menu -> ResponseEntity.ok(menu.getPlatos()))
                .orElse(ResponseEntity.notFound().build());
    }

    // Nuevo método: Asociar platos a un menú
    @PostMapping("/{id}/platos")
    public ResponseEntity<Menu> agregarPlatosAMenu(@PathVariable Long id, @RequestBody List<Long> platosIds) {
        try {
            Menu menuActualizado = menuService.agregarPlatosAMenu(id, platosIds);
            return ResponseEntity.ok(menuActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
