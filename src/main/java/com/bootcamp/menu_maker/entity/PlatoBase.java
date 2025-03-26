package com.bootcamp.menu_maker.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plato")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "tipo_plato"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Postre.class, name = "POSTRE"),
    @JsonSubTypes.Type(value = Primeros.class, name = "PRIMEROS"),
    @JsonSubTypes.Type(value = Segundos.class, name = "SEGUNDOS")
})
public class PlatoBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Double precio;
    private String descripcion;

    // Relación bidireccional con Menu
    @ManyToMany(mappedBy = "platos")
    @JsonIgnore//evita la serialicacion???
    //@JsonBackReference // Esto previene la serialización circular
    private List<Menu> menus = new ArrayList<>();

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public List<Menu> getMenus() { return menus; }
    public void setMenus(List<Menu> menus) { this.menus = menus; }

    // Puedes definir un comportamiento diferente según el tipo de plato si es necesario.
    public String getTipoPlato() { return "No especificado"; }
}
