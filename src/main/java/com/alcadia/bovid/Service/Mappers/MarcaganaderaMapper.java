package com.alcadia.bovid.Service.Mappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.modelmapper.ModelMapper;

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

    public Set<MarcaGanadera> copyObjectMarcaGanaderaDto(
            List<MarcaganderaDto> marcaGanaderaDto,
            String[] filesNameImageMarcaGanadero) {

        Set<MarcaGanadera> marcaGanaderaSet = new HashSet<>();

        IntStream.range(0, filesNameImageMarcaGanadero.length)
                .mapToObj(index -> {

                    String name = filesNameImageMarcaGanadero[index];
                    MarcaGanadera nuevaMarcaGanadera = new MarcaGanadera();

                    String urlFile = Utils.URL_BASE.concat(name);

                    nuevaMarcaGanadera
                            .setDescription("nombre de la marca ganadera" + name.trim() + "url de la imagen" + urlFile);
                    nuevaMarcaGanadera.setEtiqueta(name);
                    nuevaMarcaGanadera.setUrlImage(urlFile);

                    List<UbicacionDto> ubicacionDtoList = marcaGanaderaDto.get(index).getUbicacionDtoList();
                    // Asegúrate de que `ubicacionDtoList` tiene suficientes elementos
                    if (!ubicacionDtoList.isEmpty()) {
                        UbicacionDto ubicacionDto = ubicacionDtoList.get(0); // Obteniendo el primer elemento como
                        System.out.print(ubicacionDto); // ejemplo

                        Ubicacion ubicacion = new Ubicacion();
                        ubicacion.setDirection(ubicacionDto.getDirection());
                        ubicacion.setNameCorregimiento(ubicacionDto.getNameCorregimiento());
                        ubicacion.setNameDepartamento(ubicacionDto.getNameDepartamento());
                        ubicacion.setNameMunicipio(ubicacionDto.getNameMunicipio());

                        // Configura Zona
                        Zona zona = new Zona();
                        zona.setCodigoPostalCode(ubicacionDto.getZonaDto().getCodigoPostalCode());
                        ubicacion.setZona(zona);

                        nuevaMarcaGanadera.getUbicacionList().add(ubicacion);
                    }

                    return nuevaMarcaGanadera;

                }).forEach(marcaGanaderaSet::add);

        return marcaGanaderaSet;
    }

    public Set<MarcaGanadera> updateMarcaGanadera(List<MarcaganderaDto> marcaGanaderaDto, Ganadero ganaderoEntity,
            String[] filesNameImageMarcaGanadero) {
        Set<MarcaGanadera> marcaGanaderaSet = ganaderoEntity.getMarcaGanadera();

        // Find existing MarcaGanadera by ID and update it
        Optional<MarcaGanadera> existingMarcaGanadera = marcaGanaderaSet.stream()
                .filter(marcaGanadera -> marcaGanadera.getId().equals(marcaGanaderaDto.get(0).getId()))
                .findFirst();

        if (marcaGanaderaDto.get(0).getIsDeleted() && marcaGanaderaDto.get(0).getId() != null) {
            // Eliminar marcas ganaderas marcadas como eliminadas y con el mismo ID que las
            // de marcaGanaderaDto
            marcaGanaderaSet.removeIf(marcaGanadera -> marcaGanadera.getId().equals(marcaGanaderaDto.get(0).getId())
                    && marcaGanaderaDto.get(0).getIsDeleted());

        } else if (existingMarcaGanadera.isPresent() && !marcaGanaderaDto.get(0).getIsDeleted()) {

            MarcaGanadera marcaGanaderaToUpdate = existingMarcaGanadera.get();
            marcaGanaderaToUpdate.setDescription(marcaGanaderaDto.get(0).getDescription());
            marcaGanaderaToUpdate.setEtiqueta(marcaGanaderaDto.get(0).getEtiqueta());
            marcaGanaderaToUpdate.setUrlImage(marcaGanaderaDto.get(0).getUrlImage());

            // Update Ubicacion
            marcaGanaderaDto.get(0).getUbicacionDtoList().forEach(ubicacionDto -> {
                Ubicacion ubicacionEntity = new ArrayList<>(marcaGanaderaToUpdate.getUbicacionList()).get(0);
                ubicacionEntity.setDirection(ubicacionDto.getDirection());
                ubicacionEntity.setNameCorregimiento(ubicacionDto.getNameCorregimiento());
                ubicacionEntity.setNameDepartamento(ubicacionDto.getNameDepartamento());
                ubicacionEntity.setNameMunicipio(ubicacionDto.getNameMunicipio());

                // update Zona
                Zona zona = ubicacionEntity.getZona();
                zona.setCodigoPostalCode(ubicacionDto.getZonaDto().getCodigoPostalCode());
                ubicacionEntity.setZona(zona);

                marcaGanaderaToUpdate.getUbicacionList().add(ubicacionEntity);
            });

        }

        IntStream.range(0, filesNameImageMarcaGanadero.length)
                .mapToObj(index -> {

                    int i = filesNameImageMarcaGanadero.length != 1 ? index : index + 1;
                    String name = filesNameImageMarcaGanadero[index];
                    MarcaGanadera nuevaMarcaGanadera = new MarcaGanadera();

                    String urlFile = Utils.URL_BASE.concat(name);

                    nuevaMarcaGanadera
                            .setDescription("nombre de la marca ganadera" + name.trim() + "url de la imagen" + urlFile);
                    nuevaMarcaGanadera.setEtiqueta(name);
                    nuevaMarcaGanadera.setUrlImage(urlFile);

                    List<UbicacionDto> ubicacionDtoList = marcaGanaderaDto.get(index + 1).getUbicacionDtoList();
                    // Asegúrate de que `ubicacionDtoList` tiene suficientes elementos
                    if (!ubicacionDtoList.isEmpty()) {
                        UbicacionDto ubicacionDto = ubicacionDtoList.get(0); // Obteniendo el primer elemento como
                        System.out.print(ubicacionDto); // ejemplo

                        Ubicacion ubicacion = new Ubicacion();
                        ubicacion.setDirection(ubicacionDto.getDirection());
                        ubicacion.setNameCorregimiento(ubicacionDto.getNameCorregimiento());
                        ubicacion.setNameDepartamento(ubicacionDto.getNameDepartamento());
                        ubicacion.setNameMunicipio(ubicacionDto.getNameMunicipio());

                        // Configura Zona
                        Zona zona = new Zona();
                        zona.setCodigoPostalCode(ubicacionDto.getZonaDto().getCodigoPostalCode());
                        ubicacion.setZona(zona);

                        nuevaMarcaGanadera.getUbicacionList().add(ubicacion);
                    }

                    return nuevaMarcaGanadera;

                }).forEach(marcaGanaderaSet::add);

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
