package com.alcadia.bovid.Models.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Data
@Table(name = "supportDocuments")
@Entity
public class SupportDocument {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nombres_archivos", columnDefinition = "TEXT")
    // @JsonProperty("nombresArchivosJson")
    private String fileName;

    @Column(name = "Url_file", columnDefinition = "TEXT")
    // @JsonProperty("archivoPDFJson")
    private String urlFile;


    // @ManyToOne(fetch = FetchType.LAZY)
    // // @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    // @JsonBackReference(value = "propuesta-anexos") // se coloca en la clase que contiene la llave foranea
    // @JoinColumn(name = "propuesta_id" , unique = true)
    // private Propuesta propuesta;



    
}
