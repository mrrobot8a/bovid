package com.alcadia.bovid.Models.Dto;


import java.util.List;
import lombok.Data;

@Data
public class MarcaganderaDto {

    private String description;
    private String etiqueta;
    private String urlImage;


    private List<UbicacionDto> ubicacionDtoList ;
    
}
