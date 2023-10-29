package com.alcadia.bovid.Models.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nameCorregimiento;
    private String nameMunicipio;
    private String nameDepartamento;
    private String Direction;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "zona_id", referencedColumnName = "id")
    private  Zona zona;
    
}
