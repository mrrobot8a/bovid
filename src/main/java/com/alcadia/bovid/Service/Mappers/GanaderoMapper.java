package com.alcadia.bovid.Service.Mappers;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.alcadia.bovid.Models.Dto.GanaderoDto;
import com.alcadia.bovid.Models.Dto.MarcaganderaDto;
import com.alcadia.bovid.Models.Dto.SupportDocumentDto;
import com.alcadia.bovid.Models.Dto.UbicacionDto;
import com.alcadia.bovid.Models.Dto.ZonaDto;
import com.alcadia.bovid.Models.Entity.Ganadero;
import com.alcadia.bovid.Models.Entity.SupportDocument;
import com.alcadia.bovid.Models.Entity.Ubicacion;
import com.alcadia.bovid.Models.Entity.Zona;

public enum GanaderoMapper implements Function<Ganadero, GanaderoDto> {
    INSTANCE;

    private final ModelMapper modelMapper;

    GanaderoMapper() {
        this.modelMapper = new ModelMapper();
        // Aquí puedes agregar configuraciones personalizadas de ModelMapper si las
        // necesitas
    }

    @Override
    public GanaderoDto apply(Ganadero t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'apply'");
    }

    public Ganadero ganaderoDtoToGanadero(GanaderoDto ganaderoDto) {
        Ganadero ganadero = modelMapper.map(ganaderoDto, Ganadero.class);
        ganadero.setSupportDocument(modelMapper.map(ganaderoDto.getSupportDocumentDto(), SupportDocument.class));
        return ganadero;
    }

    
    public GanaderoDto convertToDto(Ganadero ganadero) {
        
        GanaderoDto ganaderoDto = modelMapper.map(ganadero, GanaderoDto.class);
        SupportDocument supportDocument = ganadero.getSupportDocument();
        SupportDocumentDto supportDocumentDto = modelMapper.map(supportDocument, SupportDocumentDto.class);

        ganaderoDto.setSupportDocumentDto(supportDocumentDto);

        return ganaderoDto;
    } 




    public Page<GanaderoDto> convertToDtoPage(Page<Ganadero> ganaderoPage) {
        List<GanaderoDto> ganaderoDtos = ganaderoPage.getContent().stream()
                .map(this::mapGanadero)
                .collect(Collectors.toList());
        return new PageImpl<>(ganaderoDtos, ganaderoPage.getPageable(), ganaderoPage.getTotalElements());
    }

    private GanaderoDto mapGanadero(Ganadero ganadero) {
        GanaderoDto ganaderoDto = modelMapper.map(ganadero, GanaderoDto.class);
        SupportDocument supportDocument = ganadero.getSupportDocument();
        SupportDocumentDto supportDocumentDto = modelMapper.map(supportDocument, SupportDocumentDto.class);

        List<MarcaganderaDto> marcasGanaderasDto = ganadero.getMarcaGanadera().stream()
                .map(marcagandera -> {
                    MarcaganderaDto marcaganderaDto = modelMapper.map(marcagandera, MarcaganderaDto.class);
                    // Mapear las relaciones anidadas de MarcaganderaDto si es necesario
                    marcaganderaDto.setUbicacionDtoList(mapUbicacionList(marcagandera.getUbicacionList()));
                    return marcaganderaDto;
                })
                .collect(Collectors.toList());
        ganaderoDto.setMarcasGanaderaDtos(marcasGanaderasDto);
        ganaderoDto.setSupportDocumentDto(supportDocumentDto);

        return ganaderoDto;
    }

    public List<UbicacionDto> mapUbicacionList(Set<Ubicacion> set) {
        return set.stream()
                .map(ubicacion -> {
                    UbicacionDto ubicacionDto = modelMapper.map(ubicacion, UbicacionDto.class);
                    // Mapear la relación anidada de UbicacionDto si es necesario
                    ubicacionDto.setZonaDto(mapZona(ubicacion.getZona()));
                    return ubicacionDto;
                })
                .collect(Collectors.toList());
    }

    private ZonaDto mapZona(Zona zona) {
        return modelMapper.map(zona, ZonaDto.class);
    }

    List<UbicacionDto> mapUbicacionToUbicacionDtoList(Set<Ubicacion> ubicacionList) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mapUbicacionToUbicacionDtoList'");
    }

}
