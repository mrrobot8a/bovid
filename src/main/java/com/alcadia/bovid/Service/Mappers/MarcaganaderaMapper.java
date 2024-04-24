package com.alcadia.bovid.Service.Mappers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.util.StringUtils;

import com.alcadia.bovid.Models.Dto.MarcaganderaDto;
import com.alcadia.bovid.Models.Dto.UbicacionDto;
import com.alcadia.bovid.Models.Dto.ZonaDto;
import com.alcadia.bovid.Models.Entity.Ganadero;
import com.alcadia.bovid.Models.Entity.MarcaGanadera;
import com.alcadia.bovid.Models.Entity.Ubicacion;
import com.alcadia.bovid.Models.Entity.Zona;
import com.alcadia.bovid.Service.Util.Utils;

public enum MarcaganaderaMapper implements Function<MarcaGanadera, MarcaganderaDto> {
    INSTANCE;

    private final ModelMapper modelMapper;

    MarcaganaderaMapper() {
        this.modelMapper = new ModelMapper();
        // Aquí puedes agregar configuraciones personalizadas de ModelMapper si las
        // necesitas
    }

    @Override
    public MarcaganderaDto apply(MarcaGanadera marcaGanaderaEntity) {

        if (marcaGanaderaEntity != null) {
            MarcaganderaDto marcaganderaDto = new MarcaganderaDto();

            marcaganderaDto.setDescription(marcaGanaderaEntity.getDescription());
            marcaganderaDto.setEtiqueta(marcaGanaderaEntity.getEtiqueta());
            marcaganderaDto.setUrlImage(marcaGanaderaEntity.getUrlImage());

            return marcaganderaDto;

        }

        return null;
    }

    public MarcaGanadera marcaGanaderaDtoToMarcaGanadera(MarcaganderaDto marcaGanadera) {

        MarcaGanadera marcaganderaEntity = modelMapper.map(marcaGanadera, MarcaGanadera.class);
        marcaganderaEntity.setUbicacionList(mapUbicacionList(marcaGanadera.getUbicacionDtoList()));
        return marcaganderaEntity;
    }

    private Set<Ubicacion> mapUbicacionList(List<UbicacionDto> set) {
        return set.stream()
                .map(ubicacionDto -> {
                    Ubicacion ubicacionEntity = modelMapper.map(ubicacionDto, Ubicacion.class);
                    // Mapear la relación anidada de UbicacionDto si es necesario
                    ubicacionEntity.setZona(mapZona(ubicacionDto.getZonaDto()));
                    return ubicacionEntity;
                })
                .collect(Collectors.toSet());
    }

    private Zona mapZona(ZonaDto zona) {
        return modelMapper.map(zona, Zona.class);
    }

    public MarcaganderaDto copyObjectMarcaGanaderaDto(MarcaganderaDto marcaGanadera) {

        MarcaganderaDto marcaganderaDto = new MarcaganderaDto();

        marcaganderaDto.setDescription(marcaGanadera.getDescription());
        marcaganderaDto.setEtiqueta(marcaGanadera.getEtiqueta());
        marcaganderaDto.setUrlImage(marcaGanadera.getUrlImage());
        marcaganderaDto.setUbicacionDtoList(marcaGanadera.getUbicacionDtoList().stream().map(ubicacion -> {

            UbicacionDto ubicacionDto = new UbicacionDto();

            ubicacionDto.setNameCorregimiento(ubicacion.getNameCorregimiento());
            ubicacionDto.setNameMunicipio(ubicacion.getNameMunicipio());
            ubicacionDto.setNameDepartamento(ubicacion.getNameDepartamento());
            ubicacionDto.setDirection(ubicacion.getDirection());
            ubicacionDto.setZonaDto(ubicacion.getZonaDto());

            return ubicacionDto;

        }).collect(java.util.stream.Collectors.toList()));

        return marcaganderaDto;

    }

    public Set<MarcaGanadera> UpdateMarcaGanadera(MarcaganderaDto marcaGanaderaDto, Ganadero GanaderoEntity,
            String[] filesNameImageMarcaGanadero) {
        Set<MarcaGanadera> marcaGanaderaSet = GanaderoEntity.getMarcaGanadera();

        // Si hay más de un elemento en el arreglo de imágenes, crear copias de la marca
        // ganadera y agregarlas al conjunto
        Arrays.stream(filesNameImageMarcaGanadero, 0, filesNameImageMarcaGanadero.length)
                .forEach(name -> {
                    MarcaGanadera nuevaMarcaGanadera = new MarcaGanadera();
                   
                    String urlFile = Utils.URL_BASE.concat(name);
                   
                    nuevaMarcaGanadera.setDescription("nombre de la marca ganadera"+name.trim()+"url de la imagen"+urlFile);
                    nuevaMarcaGanadera.setEtiqueta(name);
                    nuevaMarcaGanadera.setUrlImage(urlFile);

                    // Copiar y configurar la ubicación
                    marcaGanaderaDto.getUbicacionDtoList().forEach(ubicacionDto -> {
                        Ubicacion ubicacion = new Ubicacion();
                        ubicacion.setDirection(ubicacionDto.getDirection());
                        ubicacion.setNameCorregimiento(ubicacionDto.getNameCorregimiento());
                        ubicacion.setNameDepartamento(ubicacionDto.getNameDepartamento());
                        ubicacion.setNameMunicipio(ubicacionDto.getNameMunicipio());

                        // Configurar la zona
                        Zona zona = new Zona();
                        zona.setCodigoPostalCode(ubicacionDto.getZonaDto().getCodigoPostalCode());
                        ubicacion.setZona(zona);

                        nuevaMarcaGanadera.getUbicacionList().add(ubicacion);
                    });

                    marcaGanaderaSet.add(nuevaMarcaGanadera); // Agregar la nueva marca ganadera al conjunto
                });

        return marcaGanaderaSet;
    }

  
    // private List<UbicacionDto> mapUbicacionListDto(List<UbicacionDto> set) {
    // return set.stream()
    // .map(ubicacion -> {
    // UbicacionDto ubicacionDto = modelMapper.map(ubicacion, UbicacionDto.class);
    // // Mapear la relación anidada de UbicacionDto si es necesario
    // ubicacionDto.setZonaDto(mapZonaDto(ubicacion.getZonaDto()));
    // return ubicacionDto;
    // })
    // .collect(Collectors.toList());
    // }

    // private ZonaDto mapZonaDto(ZonaDto zona) {
    // return modelMapper.map(zona, ZonaDto.class);
    // }

    public Set<MarcaGanadera> marcaGanaderaDtoToMarcaGanadera(List<MarcaganderaDto> marcaGanadera) {

        Set<MarcaGanadera> listMarcaGanaderas = marcaGanadera.stream().map(marca -> {
            MarcaGanadera marcaGanaderaEntity = new MarcaGanadera();
            marcaGanaderaEntity.setDescription(marca.getDescription());
            marcaGanaderaEntity.setEtiqueta(marca.getEtiqueta());
            marcaGanaderaEntity.setUrlImage(marca.getUrlImage());
            marcaGanaderaEntity.setUbicacionList(marca.getUbicacionDtoList().stream().map(ubicacion -> {

                Ubicacion ubicacionEntity = new Ubicacion();
                Zona zonaEntity = new Zona();

                zonaEntity.setCodigoPostalCode(ubicacion.getZonaDto().getCodigoPostalCode());

                ubicacionEntity.setNameCorregimiento(ubicacion.getNameCorregimiento());
                ubicacionEntity.setNameMunicipio(ubicacion.getNameMunicipio());
                ubicacionEntity.setNameDepartamento(ubicacion.getNameDepartamento());
                ubicacionEntity.setDirection(ubicacion.getDirection());
                ubicacionEntity.setZona(zonaEntity);

                return ubicacionEntity;

            }).collect(java.util.stream.Collectors.toSet()));

            return marcaGanaderaEntity;
        }

        ).collect(java.util.stream.Collectors.toSet());

        return listMarcaGanaderas;
    }

}
