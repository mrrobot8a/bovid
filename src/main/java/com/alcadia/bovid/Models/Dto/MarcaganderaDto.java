package com.alcadia.bovid.Models.Dto;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarcaganderaDto {
   
    private Long id;
    private String description;
    private String etiqueta;
    private String urlImage;
    private String similitud;
    private Boolean isDeleted;

    @JsonProperty("ubicacionList")
    private List<UbicacionDto> ubicacionDtoList ;

   


    
}
