package com.bootcamp.menu_maker.entity;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("POSTRE")
public class Postre extends PlatoBase {

    private String tipoPostre;
    private boolean aptoCeliaco;

    public Postre() {}

    // Getters y setters
    public String getTipoPostre() { return tipoPostre; }
    public void setTipoPostre(String tipoPostre) { this.tipoPostre = tipoPostre; }
    public boolean isAptoCeliaco() { return aptoCeliaco; }
    public void setAptoCeliaco(boolean aptoCeliaco) { this.aptoCeliaco = aptoCeliaco; }
    @Override
    public String getTipoPlato() {
        return "POSTRE";
    }
}

