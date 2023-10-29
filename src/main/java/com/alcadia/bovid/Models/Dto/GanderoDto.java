package com.alcadia.bovid.Models.Dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GanderoDto {


    private String identificacion;

    private String firstName;

    private String lastName;

    private String phone ;

    private List<MarcaganderaDto> marcasGanaderaDtos = new ArrayList<>();

  
    
    
}
