package com.bootcamp.menu_maker.entity;



import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@Entity
@Table(name = "plato")
/* 
@JsonIdentityInfo utiliza un identificador único (id) para serializar y deserializar objetos. Esto evita la recursividad infinita al referenciar objetos relacionados en ambas direcciones.

Al serializar un Menu, los platos (PlatoBase) incluirán únicamente un identificador del menú sin repetir toda la estructura del objeto.

*/
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "tipo_plato"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Postre.class, name = "POSTRE"),
    @JsonSubTypes.Type(value = Primeros.class, name = "PRIMEROS"),
    @JsonSubTypes.Type(value = Segundos.class, name = "SEGUNDOS"),
    
})
public class PlatoBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Double precio;
    private String descripcion;

    @ManyToMany(mappedBy = "platos")
    @JsonBackReference
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
}
