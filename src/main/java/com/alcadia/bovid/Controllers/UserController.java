package com.alcadia.bovid.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alcadia.bovid.Models.Dto.UserDto;
import com.alcadia.bovid.Service.UserCase.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UserController {

    private final IUserService userService;

    @GetMapping("/")
    public String helloUserController() {
        return "User access level";
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Map<String, Object> response = new HashMap<>();

        try {
            Page<UserDto> userPage = userService.getAllUsers(page, size);

            System.out.println(userPage.getContent().toString());

            response.put("user", userPage.getContent());
            response.put("currentPage", userPage.getNumber());
            response.put("totalItems", userPage.getTotalElements());
            response.put("totalPages", userPage.getTotalPages());

        } catch (Exception e) {
            // Maneja la excepción aquí si ocurre algún error, como un error en la base de
            // datos
            response.put("error", "Error al obtener User: " + e.getMessage());
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {

        Map<String, Object> response = new HashMap<>();

        try {

            System.out.println(userDto);
            UserDto user = userService.updateUser(userDto);

            

            response.put("user", user);

        } catch (Exception e) {
            // Maneja la excepción aquí si ocurre algún error, como un error en la base de
            // datos
            response.put("error", "Error al actualizar User: " + e.getMessage());
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestBody UserDto userDto) {

        Map<String, Object> response = new HashMap<>();

        try {

            userService.deleteUser(userDto);

            response.put("mensaje", "SUCCESS TO DELETE USER");

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // Maneja la excepción aquí si ocurre algún error, como un error en la base de
            // datos
            response.put("error", "Error al actualizar User: " + e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
