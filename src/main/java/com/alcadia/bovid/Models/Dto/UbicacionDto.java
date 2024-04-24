package com.alcadia.bovid.Models.Dto;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UbicacionDto {

    private String nameCorregimiento;
    private String nameMunicipio;
    private String nameDepartamento;
    private String direction;
    
    @JsonProperty("zona")
    private ZonaDto zonaDto;
}
