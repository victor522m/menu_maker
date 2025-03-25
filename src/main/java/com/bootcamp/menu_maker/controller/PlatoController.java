package com.bootcamp.menu_maker.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bootcamp.menu_maker.entity.PlatoBase;
import com.bootcamp.menu_maker.service.PlatoService;

import java.util.List;

@RestController
@RequestMapping("/api/platos")
public class PlatoController {

    private final PlatoService platoService;

    public PlatoController(PlatoService platoService) {
        this.platoService = platoService;
    }

    @GetMapping
    public List<PlatoBase> obtenerTodosPlatos() {
        return platoService.obtenerTodosPlatos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatoBase> obtenerPlatoPorId(@PathVariable Long id) {
        return platoService.obtenerPlatoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlatoBase crearPlato(@RequestBody PlatoBase plato) {
        return platoService.crearPlato(plato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatoBase> actualizarPlato(@PathVariable Long id, @RequestBody PlatoBase plato) {
        try {
            PlatoBase platoActualizado = platoService.actualizarPlato(id, plato);
            return ResponseEntity.ok(platoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPlato(@PathVariable Long id) {
        platoService.eliminarPlato(id);
        return ResponseEntity.noContent().build();
    }
}
