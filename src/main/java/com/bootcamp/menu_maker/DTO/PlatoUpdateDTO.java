package com.bootcamp.menu_maker.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlatoUpdateDTO(
    String nombre,
    Double precio,
    String descripcion,
    @JsonProperty("tipo_plato") 
    String tipoPlato
) {}
