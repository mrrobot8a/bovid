package com.alcadia.bovid.Service.UserCase;

import org.springframework.data.domain.Page;

import com.alcadia.bovid.Models.Dto.RoleDto;
 

public interface IRoleService {

    String creatRele(String role);

    Page<RoleDto> getAllRoles(int page, int size);

    String deleteRole(String role);

    String updateRole(RoleDto newRole);

}
