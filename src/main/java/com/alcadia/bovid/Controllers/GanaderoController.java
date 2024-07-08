package com.alcadia.bovid.Controllers;

import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import com.alcadia.bovid.Models.Dto.GanaderoDto;
import com.alcadia.bovid.Service.UserCase.IGanaderoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.exceptions.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/ganadero")
public class GanaderoController {

    private final IGanaderoService ganaderoService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/save-ganadero")
    public ResponseEntity<?> uploadImages(@RequestParam("images") MultipartFile[] images,
            @RequestParam("ganadero") String ganadero,
            @RequestParam("fileDocument") MultipartFile fileSupportDocuments)
            throws IOException, SocketException, java.io.IOException {

        Map<String, Object> response = new HashMap<>();

        try {

            GanaderoDto ganderoDto = objectMapper.readValue(ganadero, GanaderoDto.class);

            System.out.println("el nombre del archivo es: " + ganderoDto);

            boolean isOK = ganaderoService.SaveGanadero(fileSupportDocuments, ganderoDto, images);

            response.put("message", "Ganadero guardado con éxito");
            response.put("gandero", ganderoDto);
            response.put("success", true);
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {

            response.put("error", e.getMessage());
            response.put("message", "Error al guardar el ganadero");
            return ResponseEntity.badRequest().body(response);

        }

    }

    @PutMapping("/update-ganadero")
    public ResponseEntity<?> updateGanadero(
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam("ganadero") String ganadero,
            @RequestParam(value = "fileDocument", required = false) MultipartFile fileSupportDocuments)
            throws IOException, SocketException, java.io.IOException {

        Map<String, Object> response = new HashMap<>();

        try {

            GanaderoDto ganderoDto = objectMapper.readValue(ganadero, GanaderoDto.class);

            System.out.println("el nombre del archivo es: " + ganderoDto);

            boolean isOK = ganaderoService.UpdateGanadero(fileSupportDocuments, ganderoDto, images);

            response.put("message", "Ganadero guardado con éxito");
            response.put("gandero", ganderoDto);
            response.put("success", true);
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {

            response.put("error", e.getMessage());
            response.put("message", "Error al guardar el ganadero");
            return ResponseEntity.badRequest().body(response);

        }

    }

    @GetMapping("/get-all-ganaderos")
    public ResponseEntity<?> getAllGanederos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Map<String, Object> response = new HashMap<>();

        try {

            Page<GanaderoDto> ganderoPage = ganaderoService.getAllGanaderoPage(page, size);

            System.out.println(ganderoPage.getContent().toString());

            if (ganderoPage != null) {
                response.put("ganadero", ganderoPage);
                response.put("mensaje", "SUCCESS TO GET Ganderos");
                response.put("success", true);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // Maneja la excepción aquí si ocurre algún error, como un error en la base de
            // datos
            response.put("error", "Error al obtener User: " + e.getMessage());
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

}
