package com.alcadia.bovid.Models.Dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GanaderoDto {

    private Long id;

    private String identificacion;

    private String firstName;

    private String lastName;

    private String phone ;
    
    @JsonProperty("marcasGanadera")
    private List<MarcaganderaDto> marcasGanaderaDtos = new ArrayList<>();

    @JsonProperty("supportDocument")
    private SupportDocumentDto supportDocumentDto;
    
    
}
