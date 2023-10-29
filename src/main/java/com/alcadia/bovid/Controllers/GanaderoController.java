package com.alcadia.bovid.Controllers;

import java.util.HashMap;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alcadia.bovid.Models.Dto.GanderoDto;
import com.alcadia.bovid.Service.UserCase.IGanaderoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/ganadero")
public class GanaderoController {

    private final IGanaderoService ganaderoService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/save-ganadero")
    public ResponseEntity<?> saveGanadero(@RequestParam("ganadero") String ganadero,
            @RequestParam("fileDocument") MultipartFile fileSupportDocuments,
            @RequestParam("imageMarca") MultipartFile imageMarcaGanadero) {

        Map<String, Object> response = new HashMap<>();

        try {

            GanderoDto ganderoDto = objectMapper.readValue(ganadero, GanderoDto.class);

            System.out.println("el nombre del archivo es: " + ganderoDto);

            boolean isOK = ganaderoService.SaveGanadero(fileSupportDocuments, ganderoDto, imageMarcaGanadero);

            if (isOK) {
                response.put("message", "Ganadero guardado con éxito");
                response.put("gandero", ganderoDto);
                return ResponseEntity.ok().body(response);
            }

            response.put("ganadero", ganderoDto);
            response.put("message", "Error al guardar el ganadero");
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }

}
