package com.alcadia.bovid.Controllers;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alcadia.bovid.Exception.InvalidPdfRequestException;
import com.alcadia.bovid.Service.UserCase.ISupportDocumentsService;

import io.micrometer.common.util.StringUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/support-document")
public class SupportDocumentController {

    private final ISupportDocumentsService supportDocumentsService;

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<?> download(@PathVariable("filename") String fileName)
            throws IOException {

        Map<String, Object> response = new HashMap<>();

        if (StringUtils.isBlank(fileName)) {
            // Construir una respuesta de error personalizada
            response.put("fileName", "el campo filename esta vacio error");

            // Devolver una respuesta con el mensaje de error al front-end

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        Resource resource = supportDocumentsService.download(fileName);

        if (resource == null) {
            response.put("objct", resource);
            response.put("clase", "error desde al descargar el arcchivo");
            response.put("mensaje", "Error al realizar la insersion en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .header("Content-type", "application/octet-stream")
                .body(resource);

    }

    @GetMapping(path = "/ver-pdf/{filename:.+}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> verPDF(@PathVariable("filename") String fileName)
            throws com.itextpdf.io.exceptions.IOException, IllegalStateException, SocketException, IOException {
        try {

            // Verifica si el nombre del archivo termina con ".pdf"
            if (!fileName.toLowerCase().endsWith(".pdf") && !fileName.toLowerCase().endsWith(".png")) {
                throw new InvalidPdfRequestException("El archivo solicitado no es válido.");
            }

            // Utiliza tu servicio FTPService para descargar el archivo PDF desde el
            // servidor FTP.
            Resource pdfContenido = supportDocumentsService.download(fileName);

            if (pdfContenido != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(fileName.endsWith(".png") ? MediaType.IMAGE_PNG : MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", fileName);
                headers.setCacheControl("no-cache, no-store, must-revalidate");
                headers.setPragma("no-cache");

                return new ResponseEntity<>(pdfContenido, headers, HttpStatus.OK);
            } else {
                // Si no se pudo descargar el archivo, devuelve un mensaje de error.
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "No se encontró el archivo PDF en el servidor.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al descargar el archivo PDF: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(response);
        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al descargar el archivo PDF: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(response);
        }
    }

}
