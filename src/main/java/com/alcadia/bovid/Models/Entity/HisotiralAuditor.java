package com.alcadia.bovid.Models.Entity;


import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "historial")
public class HisotiralAuditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorial;

    private String ipComputer;

    private String httpMethod;

    private String actionUser;
   
    @Column(name = "url" , length = 150 , columnDefinition = "varchar(150)")
    private String url;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SingIn_Date")
    private Date SingInDate;

  
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "logout_date")
    private Date LogoutDate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "usuario_id") // Nombre de la columna de clave foránea en la tabla de Funcionario
    private User users ;

    // public void setAnexosPdf(<AnexoPdf> anexoPdf) {
    // this.anexosPdf = anexoPdf;
    // for (AnexoPdf anPdf : anexosPdf) {
    // anPdf.setPropuesta(this);
    // }

    // }

    

    // @PrePersist
    // public void prePersist() {

    // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    // fechaCreacion = sdf.format(new Date());
    // }

    // public void deleteRoleFromUsers() {
    // // Paso 1: Recorre la colección de usuarios asociados a este rol.
    // for (User user : users) {
    // // Paso 2: Elimina el rol actual (this) de la lista de autoridades del
    // usuario.
    // user.getAuthorities().remove(this);
    // }

    // // Paso 3: Una vez que se ha eliminado el rol de todos los usuarios,
    // // vacía la colección de usuarios para reflejar los cambios en la relación.
    // users.clear();
    // }

}
