package com.bootcamp.menu_maker.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bootcamp.menu_maker.entity.Menu;
import com.bootcamp.menu_maker.entity.PlatoBase;
import com.bootcamp.menu_maker.pdf.PdfBoxGeneratorService;
import com.bootcamp.menu_maker.service.MenuService;

import java.io.File;
import java.io.FileInputStream;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;
    private final PdfBoxGeneratorService pdfBoxGeneratorService;

    // Constructor con inyección de dependencias
    public MenuController(MenuService menuService, PdfBoxGeneratorService pdfBoxGeneratorService) {
        this.menuService = menuService;
        this.pdfBoxGeneratorService = pdfBoxGeneratorService;
    }

    @GetMapping("pdf/{id}/{porcentajeIva}")
    public ResponseEntity<Resource> generatePdf(
            @PathVariable Long id,
            @PathVariable double porcentajeIva) {
        try {
            // Obtener el menú por ID desde el servicio
            Menu menu = menuService.obtenerMenuPorId(id)
                    .orElseThrow(() -> new RuntimeException("Menú no encontrado"));

            // Definir la ruta del archivo PDF generado
            String destino = System.getProperty("java.io.tmpdir") + File.separator + "menu_" + id + ".pdf";

            // Generar el PDF
            pdfBoxGeneratorService.generateMenuPdf(menu, destino, porcentajeIva);

            // Verificar que el archivo se haya generado correctamente
            File file = new File(destino);
            if (!file.exists()) {
                throw new RuntimeException("El archivo PDF no fue generado correctamente.");
            }

            // Cargar el archivo como recurso
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            // Configurar encabezados para la descarga
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            // Manejo genérico de errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    @GetMapping("/{id}/platos")
    public ResponseEntity<List<PlatoBase>> obtenerPlatosDeMenu(@PathVariable Long id) {
        return menuService.obtenerMenuPorId(id)
                .map(menu -> ResponseEntity.ok(menu.getPlatos()))
                .orElse(ResponseEntity.notFound().build());
    }

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
