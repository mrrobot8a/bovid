package com.alcadia.bovid.Models.Entity;

import java.util.HashSet;
import java.util.Set;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "ganaderos")
@Entity
public class Ganadero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String identificacion;

    private String firstName;

    private String lastName;

    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "supportDocument_id", referencedColumnName = "id")
    private SupportDocument supportDocument;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ganadero_id", nullable = false) // Columna que hace referencia al ID del Ganadero
    private Set<MarcaGanadera> marcaGanadera = new HashSet<>();

    public void addMarcaGanadera(MarcaGanadera marcaGanadera) {

        this.marcaGanadera.add(marcaGanadera);

    }

    public void deleteSuportDocumentFromGanadero() {

        this.supportDocument = null;

    }

    public void deleteGanderoFromMarcaGanadera() {

        this.marcaGanadera.clear();

    }

}
