package com.alcadia.bovid.Service.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

import com.alcadia.bovid.Models.Dto.RegistrationResponse;
import com.alcadia.bovid.Models.Dto.RoleDto;

import com.alcadia.bovid.Models.Entity.User;
import com.alcadia.bovid.Models.Entity.Role;

public enum UserToRegistrationResponse implements Function<User, RegistrationResponse> {

    INSTANCE;

    @Override
    public RegistrationResponse apply(User userEntity) {
     List<RoleDto> roles = new ArrayList<>();

        roles = userEntity.getAuthorities().stream()
                    .filter(role -> role != null)
                    .map((Function<GrantedAuthority, RoleDto>) role -> RoleDto.builder()
                            .authority(role.getAuthority())
                            .status(((Role) role).getStatus()) // Asegúrate de que getStatus() está definido en Role
                            .build())
                    .collect(Collectors.toList());

        RegistrationResponse registrationResponse = new RegistrationResponse( userEntity.getId(),userEntity.getFirstName(),userEntity.getLastName(), userEntity.getPassword(),
                userEntity.getEmail(),roles,userEntity.isAccountNonLocked(),userEntity.getFuncionario().getNumberPhone(),userEntity.getFuncionario().getPosition());
        return registrationResponse;

    }

}
