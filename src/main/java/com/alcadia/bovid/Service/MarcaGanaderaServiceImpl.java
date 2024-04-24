package com.alcadia.bovid.Service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alcadia.bovid.Models.Dto.GanaderoDto;
import com.alcadia.bovid.Models.Dto.MarcaganderaDto;
import com.alcadia.bovid.Models.Dto.SupportDocumentDto;
import com.alcadia.bovid.Models.Dto.UbicacionDto;
import com.alcadia.bovid.Models.Dto.ZonaDto;
import com.alcadia.bovid.Models.Dto.ImageSimilarResponse.SimilarImage;
import com.alcadia.bovid.Models.Entity.Ganadero;
import com.alcadia.bovid.Models.Entity.MarcaGanadera;
import com.alcadia.bovid.Models.Entity.Ubicacion;
import com.alcadia.bovid.Models.Entity.Zona;
import com.alcadia.bovid.Repository.Dao.IMarcaGanaderaRepository;

import com.alcadia.bovid.Service.UserCase.IMarcaGanaderaService;

@RequiredArgsConstructor
@Service
public class MarcaGanaderaServiceImpl implements IMarcaGanaderaService {

    private final IMarcaGanaderaRepository marcaGanaderaRepository;
    private final FastApiRequestImageFilterService fastApiRequestImageFilterService;
    private ModelMapper modelMapper;

    @Override
    public List<GanaderoDto> buscarPorEtiqueta(MultipartFile imageData) {

        try {

            List<SimilarImage> listImagesSimilar = fastApiRequestImageFilterService.getImagesSimilar(imageData);

            if (listImagesSimilar == null) {
                throw new RuntimeException("No se encontraron imagenes similares");
            }

             List<MarcaGanadera> listMarcaGanadera = new ArrayList<MarcaGanadera>();

            for (SimilarImage item : listImagesSimilar) {

                MarcaGanadera marcaGanaderas = marcaGanaderaRepository.findByEtiqueta(item.getNombreImagen());
                if (marcaGanaderas != null) {
                    listMarcaGanadera.add(marcaGanaderas);
                }

            }

            Map<Ganadero, List<MarcaGanadera>> map = listMarcaGanadera.stream()
                    .collect(Collectors.groupingBy(marcaGanadera -> marcaGanadera.getGanadero()));

            List<GanaderoDto> ganaderoDtoList = mapToGanaderoDtoList(map, listImagesSimilar);

            return ganaderoDtoList;

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la marca ganadera" + e.getMessage());
        }

    }

    public List<GanaderoDto> mapToGanaderoDtoList(Map<Ganadero, List<MarcaGanadera>> ganaderoMarcaGanaderaMap,
            List<SimilarImage> listImagesSimilar) {
        List<GanaderoDto> ganaderoDtoList = new ArrayList<>();

        for (Map.Entry<Ganadero, List<MarcaGanadera>> entry : ganaderoMarcaGanaderaMap.entrySet()) {
            Ganadero ganadero = entry.getKey();
            List<MarcaGanadera> marcasGanadera = entry.getValue();

            // Crear un GanaderoDto y asignar los campos
            GanaderoDto ganaderoDto = new GanaderoDto();
            ganaderoDto.setId(ganadero.getId());
            ganaderoDto.setIdentificacion(ganadero.getIdentificacion());
            ganaderoDto.setFirstName(ganadero.getFirstName());
            ganaderoDto.setLastName(ganadero.getLastName());
            ganaderoDto.setPhone(ganadero.getPhone());

            SupportDocumentDto supportDocumentDto = new SupportDocumentDto();
            supportDocumentDto.setFileName(ganadero.getSupportDocument().getFileName());
            supportDocumentDto.setUrlFile(ganadero.getSupportDocument().getUrlFile());
            supportDocumentDto.setId(ganadero.getSupportDocument().getId());
            ganaderoDto.setSupportDocumentDto(supportDocumentDto);

            // Convertir la lista de MarcaGanadera a una lista de MarcaganderaDto
            List<MarcaganderaDto> marcasGanaderaDtos = marcasGanadera.stream()
                    .map(marcaGanadera -> {
                        MarcaganderaDto marcaGanaderaDto = new MarcaganderaDto();
                        marcaGanaderaDto.setId(marcaGanadera.getId());
                        marcaGanaderaDto.setDescription(marcaGanadera.getDescription());
                        marcaGanaderaDto.setEtiqueta(marcaGanadera.getEtiqueta());
                        marcaGanaderaDto.setUrlImage(marcaGanadera.getUrlImage());
                        marcaGanaderaDto.setSimilitud(
                                obtenerSimilitudPorEtiqueta(marcaGanadera.getEtiqueta(), listImagesSimilar));
                        marcaGanaderaDto.setUbicacionDtoList(mapUbicacionList(marcaGanadera.getUbicacionList()));
                        // Convertir la lista de Ubicacion a una lista de UbicacionDto
                        // marcaGanaderaDto.setUbicacionDtoList(...);
                        return marcaGanaderaDto;
                    })
                    .collect(Collectors.toList());

            // Asignar la lista de MarcaganderaDto al GanaderoDto
            ganaderoDto.setMarcasGanaderaDtos(marcasGanaderaDtos);

            // Agregar el GanaderoDto a la lista resultante
            ganaderoDtoList.add(ganaderoDto);
        }

        return ganaderoDtoList;
    }

    private static String obtenerSimilitudPorEtiqueta(String etiqueta, List<SimilarImage> listImagesSimilar) {
        for (SimilarImage item : listImagesSimilar) {
            if (item.getNombreImagen().equals(etiqueta)) {
                return String.valueOf(item.getSimilitud());
            }
        }
        return "N/A"; // O algún valor predeterminado si no se encuentra la similitud
    }

    public List<UbicacionDto> mapUbicacionList(Set<Ubicacion> set) {

        List<UbicacionDto> listUbicaionDtos = new ArrayList<>();

        set.stream().forEach(ubicacion -> {
            UbicacionDto ubicacionDto = new UbicacionDto();
            ZonaDto zonaDto = new ZonaDto();

            ubicacionDto.setNameCorregimiento(ubicacion.getNameCorregimiento());
            ubicacionDto.setNameMunicipio(ubicacion.getNameMunicipio());
            ubicacionDto.setNameDepartamento(ubicacion.getNameDepartamento());

            ubicacionDto.setDirection(ubicacion.getDirection());

            zonaDto.setCodigoPostalCode(ubicacion.getZona().getCodigoPostalCode());

            ubicacionDto.setZonaDto(zonaDto);

            listUbicaionDtos.add(ubicacionDto);

        });

        return listUbicaionDtos;
    }

}
