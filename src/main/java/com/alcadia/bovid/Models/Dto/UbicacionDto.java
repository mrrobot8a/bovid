package com.alcadia.bovid.Models.Dto;

import lombok.Data;

@Data
public class UbicacionDto {

    private String nameCorregimiento;
    private String nameMunicipio;
    private String nameDepartamento;
    private String direction;
    private ZonaDto zonaDto;
}
