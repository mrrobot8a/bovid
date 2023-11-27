package com.alcadia.bovid.Models.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * @author Sampson Alfred
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullname;

    @NaturalId(mutable = true)
    private String email;

    private String password;

    private boolean isEnabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JsonManagedReference
    @JoinTable(name = "user_role_junction", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Set<Role> authorities = new HashSet<>();


    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Funcionario funcionario;

    @Override
    public Set<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override

    public String getUsername() {
        return this.fullname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void addRole(Role role) {
        this.authorities.add(role);
        // role.getUsers().add(this);
    }

    public void deleteUsersFromRoles() {

         // Iterar sobre los roles del usuario
         for (GrantedAuthority authority : this.getAuthorities()) {
            if (authority instanceof Role) {
                Role role = (Role) authority;

                // Eliminar la relación entre el usuario y el rol
                role.getUsers().remove(this);
            }
        }

        // Limpiar la colección de roles del usuario
        this.getAuthorities().clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User{");
        sb.append("id=").append(id);
        sb.append(", fullname='").append(fullname).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", isEnabled=").append(isEnabled);

        // Agregar información sobre las authorities sin causar recursión infinita
        if (authorities != null) {
            sb.append(", authorities=[");
            boolean first = true;
            for (Role authority : authorities) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append("Role{id=").append(authority.getRoleId()).append(", name='").append(authority.getAuthority())
                        .append('\'');
                first = false;
            }
            sb.append("]");
        }

        sb.append('}');
        return sb.toString();
    }

}
