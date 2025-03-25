package com.bootcamp.menu_maker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bootcamp.menu_maker.entity.Menu;
import com.bootcamp.menu_maker.entity.PlatoBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

class MenuTest {

    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = new Menu();
        PlatoBase plato1 = new PlatoBase();
        plato1.setPrecio(10.00); // Precio del plato 1
        PlatoBase plato2 = new PlatoBase();
        plato2.setPrecio(15.50); // Precio del plato 2
        PlatoBase plato3 = new PlatoBase();
        plato3.setPrecio(9.30); // Precio del plato 3

        menu.setPlatos(List.of(plato1, plato2, plato3));
    }

    @Test
    void testCalcularTotalPrecios() {
        double total = menu.calcularTotalPrecios();
        assertEquals(34.80, total, 0.01); // Verifica que el total sea correcto
    }

    @Test
    void testCalcularTotalConIva() {
        double totalConIva = menu.calcularTotalConIva(21); // Aplicar un 21% de IVA
        assertEquals(42.11, totalConIva, 0.01); // Verifica el total con IVA
    }
}

