package com.alcadia.bovid.Service;


import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alcadia.bovid.Exception.RoleNoExistsException;
import com.alcadia.bovid.Models.Dto.RoleDto;
import com.alcadia.bovid.Models.Entity.Role;
import com.alcadia.bovid.Repository.Dao.IRoleRepository;
import com.alcadia.bovid.Service.Mappers.RoleToRoleDto;
import com.alcadia.bovid.Service.UserCase.IRoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final IRoleRepository roleRepository;

    @Override
    public String creatRele(String role) {

        try {

            Optional<Role> roleEntity = roleRepository.findByAuthority(role);

            if (roleEntity.isPresent()) {
                throw new RoleNoExistsException(
                        "El role  ya existe en la base de datos");
            }

            Role roleSave = new Role();
            

            roleSave.setAuthority(role);

            roleRepository.save(roleSave);

            return "success";

        } catch (DataAccessException e) {

            // Imprimir detalles de la excepción en una sola línea
            System.err.println("Mensaje de la excepción: " + e.getMessage() +
                    ", Causa raíz (si existe): " + e.getCause() +
                    ", Nombre de la clase de la excepción: " + e.getClass().getName());
        }

        return null;

    }

    @Override
    public Page<RoleDto> getAllRoles(int page, int size) {

        try {

            Pageable pageable = PageRequest.of(page, size);

            Page<Role> roles = roleRepository.findAll(pageable);

            if (roles.isEmpty()) {
                throw new RoleNoExistsException("No hay roles en la base de datos");
            }

            return RoleToRoleDto.INSTANCE.pageRoleToPageRoleDto(roles);

        } catch (DataAccessException e) {

            // Imprimir detalles de la excepción en una sola línea
            System.err.println("Mensaje de la excepción: " + e.getMessage() +
                    ", Causa raíz (si existe): " + e.getCause() +
                    ", Nombre de la clase de la excepción: " + e.getClass().getName());

        }

        return null;

    }

    @Override
    public String deleteRole(String role) {

        try {

            Optional<Role> roleEntity = roleRepository.findByAuthority(role);

            if (roleEntity.isEmpty()) {
                throw new RoleNoExistsException("El rol asignado al usuario no existe en la base de datos");
            }

            // elimina la relacion con el usuario de la tabla intermedia user_role_junction  en ambas direcciones
            roleEntity.get().deleteRoleFromUsers();

            // Finalmente, eliminar el rol
            roleRepository.deleteById(roleEntity.get().getRoleId());

            return "success";
        } catch (DataAccessException e) {
            // Manejar excepciones
            System.err.println("Mensaje de la excepción: " + e.getMessage() +
                    ", Causa raíz (si existe): " + e.getCause() +
                    ", Nombre de la clase de la excepción: " + e.getClass().getName());
        }
        return null;
    }

    @Override
    public String updateRole(RoleDto newRole) {

        try {

            Optional<Role> roleEntity = roleRepository.findByAuthority(newRole.getAuthority());

            if (roleEntity.isEmpty()) {
                throw new RoleNoExistsException(
                        "El role asiganddo al usuario no existe en la base de datos");
            }

            roleEntity.get().setAuthority(newRole.getNewNameRole());

            roleRepository.save(roleEntity.get());

            return "success";

        } catch (DataAccessException e) {

            // Imprimir detalles de la excepción en una sola línea
            System.err.println("Mensaje de la excepción: " + e.getMessage() +
                    ", Causa raíz (si existe): " + e.getCause() +
                    ", Nombre de la clase de la excepción: " + e.getClass().getName());
        }

        return null;
    }

}
