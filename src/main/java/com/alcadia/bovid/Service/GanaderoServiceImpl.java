package com.alcadia.bovid.Service;

import java.util.UUID;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alcadia.bovid.Models.Dto.GanderoDto;
import com.alcadia.bovid.Models.Entity.Ganadero;
import com.alcadia.bovid.Repository.Dao.IGanaderoRepository;
import com.alcadia.bovid.Service.UserCase.IGanaderoService;
import com.alcadia.bovid.Service.UserCase.ISupportDocumentsService;
import com.alcadia.bovid.Service.Util.Utils;


import jakarta.transaction.Transactional;
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
    public Boolean SaveGanadero(MultipartFile file, GanderoDto ganderoDto) {

        try {

            // Normalize file name
            String uniqueFilename = UUID.randomUUID().toString() + "_"
                    + StringUtils.cleanPath(file.getOriginalFilename());
                    

            Ganadero ganaderoEntity = new Ganadero();
         

            ganaderoEntity.setFirstName(ganderoDto.getFirstName());
            ganaderoEntity.setLastName(ganderoDto.getLastName());
            ganaderoEntity.setPhone(ganderoDto.getPhone());
            ganaderoEntity.setIdentificacion(ganderoDto.getIdentificacion());

            System.out.println("el nombre del archivo es: " + Utils.URL_BASE.concat(uniqueFilename));


            ganaderoEntity.setSupportDocument(
                            supportDocumentsImpl.saveFileDocument(file, uniqueFilename,
                                    Utils.URL_BASE.concat(uniqueFilename)));

            ganaderoRepository.save(ganaderoEntity);

           

            return true;

        } catch (Exception e) {
            log.error("Error al guardar el ganadero", e);
        }

        return null;

    }

}
