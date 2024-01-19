package com.alcadia.bovid.Service.Mappers;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.alcadia.bovid.Models.Dto.RoleDto;
import com.alcadia.bovid.Models.Dto.UserDto;
import com.alcadia.bovid.Models.Entity.Role;

public enum RoleMapper implements Function<Role, RoleDto> {
    INSTANCE;

    @Override
    public RoleDto apply(Role roleEntity) {

        if (roleEntity != null) {
            RoleDto roleDto = new RoleDto(
                    null,
                    null,
                    roleEntity.getCodRole(),
                    roleEntity.getStatus(),
                    roleEntity.getDescription(),
                    roleEntity.getAuthority(),
                    null

            );

            return roleDto;

        }
        return null;
    }

    public Page<RoleDto> pageRoleToPageRoleDto(Page<Role> roles) {

        if (roles != null) {
            List<RoleDto> roleDtos = roles.getContent()
                    .stream()
                    .map(roleEntity -> new RoleDto(null,
                            null,
                            roleEntity.getCodRole(),
                            roleEntity.getStatus(),
                            roleEntity.getDescription(),
                            roleEntity.getAuthority(),
                            roleEntity.getUsers().stream()
                                    .map(user -> new UserDto(user.getFullname(), user.getEmail(), null))
                                    .collect(Collectors.toList())))
                    .collect(Collectors.toList());

            return new PageImpl<>(roleDtos, roles.getPageable(), roles.getTotalElements());
        }
        return Page.empty(); // Otra opción es return null, dependiendo de cómo quieras manejarlo.
    }

    public List<RoleDto> LiistRoleToListRoleDto(List<Role> roles) {

        if (roles != null) {
            List<RoleDto> roleDtos = roles.stream()
                    .map(roleEntity -> new RoleDto(null,
                            null,
                            roleEntity.getCodRole(),
                            roleEntity.getStatus(),
                            roleEntity.getDescription(),
                            roleEntity.getAuthority(),
                            roleEntity.getUsers().stream()
                                    .map(user -> new UserDto(user.getFullname(), user.getEmail(), null))
                                    .collect(Collectors.toList())))
                    .collect(Collectors.toList());

            return roleDtos;
        }
        return null; // Otra opción es return null, dependiendo de cómo quieras manejarlo.
    }

    public Set<Role> ListDtoToListRole(List<RoleDto> roles) {
        if (roles != null) {
            Set<Role> roleDtos = roles.stream()
                    .map(role -> {
                        Role roleEntity = new Role();
                        roleEntity.setAuthority(role.getAuthority());
                        return roleEntity;
                    })

                    .collect(Collectors.toSet());

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + roleDtos);

            return roleDtos;
        }
        return null; // Otra opción es return null, dependiendo de cómo quieras manejarlo.
    }

}
