package com.alcadia.bovid.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alcadia.bovid.Models.Dto.HistoryAuditordDto;
import com.alcadia.bovid.Service.UserCase.IHistorialAuditoriaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class HistoryAuditorController {

    final IHistorialAuditoriaService historyService;

    @GetMapping("/get-all-historys")
    public ResponseEntity<?> getAllHistorys(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Map<String, Object> response = new HashMap<>();

        try {
            

            Page<HistoryAuditordDto> historial = historyService.getAllHisotiralesPage(page, size);

            if (historial != null) {

                response.put("historial", historial);

                return new ResponseEntity<>(response, HttpStatus.OK);

            } else {

                response.put("mensaje", "NO SE ENCONTRARON REGISTROS");

                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

            }

        } catch (Exception e) {

            response.put("mensaje", "ERROR AL REALIZAR LA CONSULTA");
            response.put("erro", e.getMessage() );
            response.put("cause", e.getCause() );   
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

}
