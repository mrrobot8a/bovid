package com.alcadia.bovid.Service.UserCase;

import org.springframework.data.domain.Page;

import com.alcadia.bovid.Models.Dto.RoleDto;
 

public interface IRoleService {

    RoleDto createRole(RoleDto role);

    Page<RoleDto> getAllRoles(int page, int size);

    String deleteRole(String role);

    String enableRole(RoleDto role);

    RoleDto updateRole(RoleDto newRole);

}
