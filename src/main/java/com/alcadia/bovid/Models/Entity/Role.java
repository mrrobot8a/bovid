package com.alcadia.bovid.Models.Entity;


import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
    private Integer roleId;

    @Column(name = "role", unique = true)
    private String authority;

    

    @Override
    public String getAuthority() {
        return this.authority;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JsonBackReference
    @JoinTable(name = "user_role_junction", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
            @JoinColumn(name = " user_id") })
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
