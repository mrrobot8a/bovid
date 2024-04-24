package com.alcadia.bovid.Models.Entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    private static final long serialVersionUID = 142L;

    // GrantedAuthority en el proceso de crear una representación de roles con
    // la posibilidad de asignar autoridades a los usuarios. Una vez que
    // completes la implementación de la clase Role y del método getAuthority(),
    // podrás definir y administrar roles y autoridades para los usuarios de tu
    // sistema utilizando las capacidades de seguridad de Spring.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
     

    @Column(name = "cod_role", unique = true, length = 80)
    private String codRole;

    @Column(name = "role", unique = true, length = 35)
    private String authority;

    @Column(name = "description", length = 150)
    private String description;

    @Column(name = "status")
    private Boolean status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Override
    public String getAuthority() {
        return this.authority;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonBackReference
    @JoinTable(name = "user_role_junction", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
            @JoinColumn(name = " user_id") })
    @Transient
    private Set<User> users = new HashSet<>();

    // public void addUser(User user) {
    // users.add(user);
    // user.getAuthorities().add(this);
    // }
    public void deleteRoleFromUsers() {
        // Paso 1: Recorre la colección de usuarios asociados a este rol.
        for (User user : users) {
            // Paso 2: Elimina el rol actual (this) de la lista de autoridades del usuario.
            user.getAuthorities().remove(this);
        }

        // Paso 3: Una vez que se ha eliminado el rol de todos los usuarios,
        // vacía la colección de usuarios para reflejar los cambios en la relación.
        users.clear();
    }

    public void addUser(User user) {

        Set<Role> authorities = new HashSet<>();
        authorities.add(this);
        user.setAuthorities(authorities);
        users.add(user);

    }

}
