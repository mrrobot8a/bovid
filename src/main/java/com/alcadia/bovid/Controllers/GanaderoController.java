package com.alcadia.bovid.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alcadia.bovid.Models.Dto.GanderoDto;
import com.alcadia.bovid.Service.UserCase.IGanaderoService;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
            @RequestParam("file") MultipartFile file) {

        try {

            
            GanderoDto ganderoDto = objectMapper.readValue(ganadero, GanderoDto.class);

            return ResponseEntity.ok(ganaderoService.SaveGanadero(file, ganderoDto));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }

}
