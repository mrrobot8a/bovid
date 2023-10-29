package com.alcadia.bovid.Models.Entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "marca_ganadera")
public class MarcaGanadera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;
    private String etiqueta;
    private String urlImage;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    // @OneToOne
    // @JoinColumn(name = "ubicacion_id", referencedColumnName = "id")
    // private Ubicacion ubicacion;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JoinColumn(name = "marcaganadera_id", nullable = false) // Columna que hace referencia al ID dela marcaganadera
    private Set<Ubicacion> UbicacionList = new HashSet<>();

    public void addUbicacion(Ubicacion ubicacion) {

        this.UbicacionList.add(ubicacion);

    }

    public void deleteUbicacionFromMarcaGanadera() {

        this.UbicacionList = null;

    }

    // @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    // @JoinColumn(name = "ubicacion_id")
    // private Ubicacion ubicacion;

}
