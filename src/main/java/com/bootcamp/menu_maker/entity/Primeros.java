package com.bootcamp.menu_maker.entity;



import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PRIMEROS")
public class Primeros extends PlatoBase {

    private boolean esVegetariano;
    private int tiempoPreparacion;

    public Primeros() {}

    // Getters y setters
    public boolean isEsVegetariano() { return esVegetariano; }
    public void setEsVegetariano(boolean esVegetariano) { this.esVegetariano = esVegetariano; }
    public int getTiempoPreparacion() { return tiempoPreparacion; }
    public void setTiempoPreparacion(int tiempoPreparacion) { this.tiempoPreparacion = tiempoPreparacion; }
    @Override
    public String getTipoPlato() {
        return "PRIMEROS";
    }
}

