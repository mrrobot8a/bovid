package com.alcadia.bovid.Service.Mappers;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;

import com.alcadia.bovid.Models.Dto.MarcaganderaDto;
import com.alcadia.bovid.Models.Entity.MarcaGanadera;
import com.alcadia.bovid.Models.Entity.Ubicacion;
import com.alcadia.bovid.Models.Entity.Zona;

public enum MarcaganaderaMapper implements Function<MarcaGanadera, MarcaganderaDto> {
    INSTANCE;

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

    public Set<MarcaGanadera> marcaGanaderaDtoToMarcaGanadera(List<MarcaganderaDto> marcaGanadera) {

        MarcaGanadera marcaGanaderaEntity = new MarcaGanadera();

        Set<MarcaGanadera> listMarcaGanaderas = marcaGanadera.stream().map(marca -> {

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

      

        System.out.println("el tamaño de la lista es: " + marcaGanaderaEntity.getUbicacionList());

        return listMarcaGanaderas;
    }

}
