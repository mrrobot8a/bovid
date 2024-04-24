package com.alcadia.bovid.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alcadia.bovid.Models.Dto.ImageSimilarResponse;
import com.alcadia.bovid.Models.Dto.ImageSimilarResponse.SimilarImage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@Slf4j
public class FastApiRequestImageFilterService {

    private final RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public FastApiRequestImageFilterService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<SimilarImage> getImagesSimilar(MultipartFile imageData) throws IOException {

        try {

            // Leer el contenido del MultipartFile en un arreglo de bytes
            byte[] imageBytes = imageData.getBytes();

            // Crear el cuerpo de la solicitud con la imagen
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new ByteArrayResource(imageBytes) {
                @Override
                public String getFilename() {
                    return imageData.getOriginalFilename();
                }
            });

            // Configurar las cabeceras de la solicitud
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Crear la entidad de la solicitud con el cuerpo y las cabeceras
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Enviar la solicitud POST a la API de FastAPI
            String apiUrl = "https://6bf5-190-144-160-138.ngrok-free.app/upload/";
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity,
                    String.class);

            // Obtener la respuesta del cuerpo de la entidad de respuesta
            String jsonResponse = responseEntity.getBody();

            ObjectMapper objectMapper = new ObjectMapper();

            // Obtener el valor de la clave "similar_images" como una cadena JSON
            String similarImagesJson = objectMapper.readTree(jsonResponse).get("similar_images").asText();

            // Deserializar la cadena JSON en una lista de objetos ImageSimilarResponse
            List<SimilarImage> listImagesSimilar = objectMapper.readValue(similarImagesJson,
                    new TypeReference<List<SimilarImage>>() {
                    });

            // Procesar y devolver la respuesta JSON
            return listImagesSimilar;

        } catch (IOException e) {
            log.error("Error al buscar la marca ganadera", e.getMessage());

            throw new RuntimeException("Error al buscar la marca ganadera" + e.getMessage() + e.getStackTrace());
        }        catch (Exception e) {
            log.error(e.getMessage());

            throw new RuntimeException("Error al buscar la marca ganadera" + e.getMessage() + e.getStackTrace());
        }

    }

    public String getRequestToFastAPI() {
        // URL del método de FastAPI
        String apiUrl = "https://9612-190-144-160-138.ngrok-free.app/formularioFile/";

        // Realizar la solicitud GET y obtener la respuesta como una cadena JSON
        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

        // Devolver la respuesta JSON
        return jsonResponse;
    }
}
