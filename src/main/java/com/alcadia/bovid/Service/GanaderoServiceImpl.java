package com.alcadia.bovid.Service;

import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alcadia.bovid.Exception.ErrorMessage;
import com.alcadia.bovid.Exception.FtpErrors;
import com.alcadia.bovid.Models.Dto.GanderoDto;
import com.alcadia.bovid.Models.Entity.Ganadero;
import com.alcadia.bovid.Models.Entity.MarcaGanadera;
import com.alcadia.bovid.Repository.Dao.IGanaderoRepository;
import com.alcadia.bovid.Service.Mappers.MarcaganaderaMapper;
import com.alcadia.bovid.Service.UserCase.IGanaderoService;
import com.alcadia.bovid.Service.UserCase.ISupportDocumentsService;
import com.alcadia.bovid.Service.Util.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class GanaderoServiceImpl implements IGanaderoService {

    private final IGanaderoRepository ganaderoRepository;

    private final ISupportDocumentsService supportDocumentsImpl;

    @Override
    @Transactional
    public Boolean SaveGanadero(MultipartFile fileSupportDocuments, GanderoDto ganderoDto,
            MultipartFile imageMarcaGanadero) {

        try {

            // Normalize file name
            String uniqueFileNameDocuments = createNameUniqueFile(fileSupportDocuments.getOriginalFilename());
            String uniqueFileImageMarcaGandera = createNameUniqueFile(imageMarcaGanadero.getOriginalFilename());

            Ganadero ganaderoEntity = new Ganadero();

            ganaderoEntity.setFirstName(ganderoDto.getFirstName());
            ganaderoEntity.setLastName(ganderoDto.getLastName());
            ganaderoEntity.setPhone(ganderoDto.getPhone());
            ganaderoEntity.setIdentificacion(ganderoDto.getIdentificacion());

            // set url image to marca ganadera dto and add to ganadero entity
            String ulrMarcaGanadera = supportDocumentsImpl.saveFileImage(imageMarcaGanadero,
                    uniqueFileImageMarcaGandera, Utils.NAME_FOLDER_IMAGES_MARCA_GANADERA).getUrlFile();

            // ganderoDto.getMarcaGanaderaDto().setUrlImage(ulrMarcaGanadera);

            ganderoDto.getMarcasGanaderaDtos().stream().forEach(marca -> {
                marca.setUrlImage(ulrMarcaGanadera);
            });
            // maping marca ganadera
            Set<MarcaGanadera> marcaganaderaEntity = MarcaganaderaMapper.INSTANCE
                    .marcaGanaderaDtoToMarcaGanadera(ganderoDto.getMarcasGanaderaDtos());

            // add marca ganadera to ganadero
            ganaderoEntity.setMarcaGanadera(marcaganaderaEntity);

            // save file in ftp server and return url file
            ganaderoEntity.setSupportDocument(
                    supportDocumentsImpl.saveFileDocument(fileSupportDocuments, uniqueFileNameDocuments,
                            Utils.NAME_FOLDER_SUPPORTDOCUMENTS));

            ganaderoRepository.save(ganaderoEntity);

            return true;

        } catch (Exception e) {
            log.error("Error al guardar el ganadero", e);

            ErrorMessage errorMessage = new ErrorMessage(-1,
                    "errorMessage=" + e.getMessage());
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);
        }

    }

    private String createNameUniqueFile(String nameFile) {
        return UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(nameFile);
    }

}
