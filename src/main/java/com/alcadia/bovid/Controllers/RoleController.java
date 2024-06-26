package com.alcadia.bovid.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alcadia.bovid.Models.Dto.RoleDto;
import com.alcadia.bovid.Service.UserCase.IRoleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class RoleController {

    private final IRoleService roleService;

    @PostMapping("/register-Role")
    public ResponseEntity<?> registerUser(@RequestBody RoleDto roleRequest) {

        Map<String, Object> response = new HashMap<>();

        try {
            roleRequest = roleService.createRole(roleRequest);
            response.put("role", roleRequest);
            response.put("mensaje", "SUCCESS TO Register ROLE");
            response.put("success", true);

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            // Maneja la excepción aquí si ocurre algún error, como un error en la base de
            // datos
            response.put("error", e.getMessage());
            response.put("message", "Error al realizar el registro");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/get-all-roles")
    public ResponseEntity<?> getAllRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Map<String, Object> response = new HashMap<>();

        try {

            Page<RoleDto> rolePage = roleService.getAllRoles(page, size);

            response.put("roles", rolePage);
            response.put("menssage", "SUCCESS TO GET ROLES");
            response.put("success", true);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // Maneja la excepción aquí si ocurre algún error, como un error en la base de
            // datos
            response.put("error", "Error al obtener roles: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }

    }

    @PutMapping("/update-role")
    public ResponseEntity<?> updateRole(@RequestBody RoleDto roleResquest) {

        Map<String, Object> response = new HashMap<>();

        try {

            response.put("role", roleService.updateRole(roleResquest));

            response.put("mensaje", "SUCCESS TO UPDATE ROLE");
            response.put("success", true);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // Maneja la excepción aquí si ocurre algún error, como un error en la base de
            // datos
            response.put("error", "Error al actualizar rol: " + e.getMessage());
            response.put("succes", false);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/enable-role")
    public ResponseEntity<?> enableRole(@RequestBody RoleDto roleResquest) {

        Map<String, Object> response = new HashMap<>();

        try {

            response.put("message", roleService.enableRole(roleResquest));
            response.put("success", true);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // Maneja la excepción aquí si ocurre algún error, como un error en la base de
            // datos
            response.put("error", e.getMessage());
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/delete-role")
    public ResponseEntity<?> deleteRole(@RequestBody RoleDto roleResquest) {

        Map<String, Object> response = new HashMap<>();

        try {

            String ok = roleService.deleteRole(roleResquest.getAuthority());

            if (ok != null) {
                response.put("mensaje", "SUCCESS TO DELETE ROLE");

                return new ResponseEntity<>(response, HttpStatus.OK);

            }

        } catch (Exception e) {
            // Maneja la excepción aquí si ocurre algún error, como un error en la base de
            // datos
            response.put("error", "Error al eliminar rol: " + e.getMessage());

            // return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
