package com.bootcamp.menu_maker.entity;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SEGUNDO")
public class Segundos extends PlatoBase {

    private String tipoCarne;
    private String guarnicion;

    public Segundos() {}

    // Getters y setters
    public String getTipoCarne() { return tipoCarne; }
    public void setTipoCarne(String tipoCarne) { this.tipoCarne = tipoCarne; }
    public String getGuarnicion() { return guarnicion; }
    public void setGuarnicion(String guarnicion) { this.guarnicion = guarnicion; }
    @Override
    public String getTipoPlato() {
        return "PRIMERO";
    }
}
