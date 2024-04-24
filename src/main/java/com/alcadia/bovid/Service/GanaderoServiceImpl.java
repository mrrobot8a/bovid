package com.alcadia.bovid.Service;

import java.net.SocketException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alcadia.bovid.Exception.CustomerNotExistException;
import com.alcadia.bovid.Exception.FtpErrors;

import com.alcadia.bovid.Models.Dto.GanaderoDto;
import com.alcadia.bovid.Models.Dto.MarcaganderaDto;
import com.alcadia.bovid.Models.Entity.Ganadero;
import com.alcadia.bovid.Models.Entity.MarcaGanadera;
import com.alcadia.bovid.Models.Entity.SupportDocument;
import com.alcadia.bovid.Repository.Dao.IGanaderoRepository;
import com.alcadia.bovid.Service.Mappers.GanaderoMapper;
import com.alcadia.bovid.Service.Mappers.MarcaganaderaMapper;
import com.alcadia.bovid.Service.UserCase.IGanaderoService;
import com.alcadia.bovid.Service.UserCase.ISupportDocumentsService;
import com.alcadia.bovid.Service.Util.Utils;
import com.itextpdf.io.exceptions.IOException;

import ch.qos.logback.classic.pattern.Util;
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
    @Transactional(rollbackOn = Exception.class)
    public Boolean SaveGanadero(MultipartFile fileSupportDocuments, GanaderoDto ganderoDto,
            MultipartFile[] imageMarcaGanadero) throws IOException, SocketException {

        try {

            String uniqueFileNameDocuments = createNameUniqueFile(fileSupportDocuments.getOriginalFilename());
            // String uniqueFileImageMarcaGandera =
            // createNameUniqueFile(imageMarcaGanadero.getOriginalFilename());

            // Convierte los nombres de archivo de los MultipartFile en un array de String
            String[] filesNameImageMarcaGanadero = Arrays.stream(imageMarcaGanadero)
                    .map(name -> createNameUniqueFileImage(name.getOriginalFilename(), 0, 5))
                    .toArray(String[]::new);
            // ARRAY DE NOMBRES DE ARCHIVOS para la BD
            String[] fileNameForDB = Arrays.stream(imageMarcaGanadero).map(MultipartFile::getOriginalFilename)
                    .toArray(String[]::new);

            // Procesar el arreglo de imágenes con Stream
            Arrays.stream(filesNameImageMarcaGanadero)
                    // Mapear cada MultipartFile al nombre de archivo

                    // Filtrar si hay algún nombre de archivo (para asegurarse de que hay imágenes
                    // en el arreglo)
                    .filter(Objects::nonNull)
                    .findFirst() // Obtener el primer nombre de archivo (si existe)
                    .ifPresent(fileName -> {
                        String nameForDB = fileNameForDB[0];
                        // Si la lista de marcas ganaderas está vacía, agregar una nueva marca ganadera
                        // con los datos de la imagen
                        if (ganderoDto.getMarcasGanaderaDtos().isEmpty()) {
                            MarcaganderaDto nuevaMarca = new MarcaganderaDto();
                            nuevaMarca.setEtiqueta(fileName);
                            nuevaMarca.setUrlImage(Utils.URL_BASE.concat(filesNameImageMarcaGanadero[0]));
                            nuevaMarca.setDescription("nombre de la marca ganadera" + nameForDB.trim()
                                    + "url de la imagen" + Utils.URL_BASE.concat(fileName));
                            ganderoDto.getMarcasGanaderaDtos().add(nuevaMarca);
                        } else {
                            // Obtener la primera marca ganadera de la lista y actualizar la etiqueta y la
                            // URL de la imagen
                            MarcaganderaDto marcaExistente = ganderoDto.getMarcasGanaderaDtos().get(0);
                            marcaExistente.setEtiqueta(fileName);
                            marcaExistente.setUrlImage(Utils.URL_BASE.concat(filesNameImageMarcaGanadero[0]));
                            marcaExistente.setDescription("nombre de la marca ganadera" + nameForDB.trim()
                                    + "url de la imagen" + Utils.URL_BASE.concat(fileName));
                        }
                    });

            if (imageMarcaGanadero.length > 1) {

                for (int i = 1; i < filesNameImageMarcaGanadero.length; i++) {
                    String name = filesNameImageMarcaGanadero[i];
                    String nameForDB = fileNameForDB[i];
                    MarcaganderaDto marcaGanaderaCopy = MarcaganaderaMapper.INSTANCE
                            .copyObjectMarcaGanaderaDto(ganderoDto.getMarcasGanaderaDtos().get(0));

                    String urlFile = Utils.URL_BASE.concat(name);

                    marcaGanaderaCopy.setEtiqueta(name);
                    marcaGanaderaCopy.setDescription(
                            "nombre de la marca ganadera" + nameForDB.trim() + "url de la imagen" + urlFile);
                    marcaGanaderaCopy.setUrlImage(urlFile);

                    ganderoDto.getMarcasGanaderaDtos().add(marcaGanaderaCopy);
                }
            }

            Ganadero ganaderoEntity = new Ganadero();

            ganaderoEntity.setFirstName(ganderoDto.getFirstName());
            ganaderoEntity.setLastName(ganderoDto.getLastName());
            ganaderoEntity.setPhone(ganderoDto.getPhone());
            ganaderoEntity.setIdentificacion(ganderoDto.getIdentificacion());

            supportDocumentsImpl.UploadMultipleFilesToFTP(imageMarcaGanadero,
                    Utils.NAME_FOLDER_IMAGES_MARCA_GANADERA, filesNameImageMarcaGanadero);

            SupportDocument supportDocument = supportDocumentsImpl.saveFileDocument(fileSupportDocuments,
                    uniqueFileNameDocuments,
                    Utils.NAME_FOLDER_SUPPORTDOCUMENTS);

            // se mapea el Dto de marca ganadera a la entidad de marca ganadera
            Set<MarcaGanadera> marcaganaderaEntity = MarcaganaderaMapper.INSTANCE
                    .marcaGanaderaDtoToMarcaGanadera(ganderoDto.getMarcasGanaderaDtos());

            ganaderoEntity.setMarcaGanadera(marcaganaderaEntity);

            ganaderoEntity.setSupportDocument(supportDocument);

            ganaderoEntity = ganaderoRepository.save(ganaderoEntity);

            return true;

        } catch (DataAccessException e) {
            // Manejar errores de acceso a datos
            log.error("Error de acceso a datos: " + e.getMessage());
            throw e;
        } catch (IOException e) {
            // Manejar cualquier otra excepción no capturada anteriormente
            log.error("Error IOExcetion: " + e.getMessage());
            throw e;
        } catch (SocketException e) {
            // Manejar cualquier otra excepción no capturada anteriormente
            log.error("Error SocketException: " + e.getMessage());
            throw e;
        } catch (FtpErrors e) {
            // Manejar cualquier otra excepción no capturada anteriormente
            log.error("Error FtpErrors " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Manejar cualquier otra excepción no capturada anteriormente
            log.error("Error inesperado: " + e.getMessage());
            throw new RuntimeException("Error inesperado " + e.getMessage());
        }

    }

    private String createNameUniqueFileImage(String nameFile, int startIndex, int endIndex) {
        String uuid = UUID.randomUUID().toString().substring(startIndex, endIndex);
        return uuid + "_" + StringUtils.cleanPath(nameFile);
    }

    private String createNameUniqueFile(String nameFile) {
        return UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(nameFile);
    }

    @Override
    public Page<GanaderoDto> getAllGanaderoPage(int page, int size) {

        try {

            Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "id"));
            Page<Ganadero> ganaderoPage = ganaderoRepository.findAll(pageable);

            log.info(ganaderoPage.getContent().toString());

            if (ganaderoPage.isEmpty()) {
                throw new CustomerNotExistException("No hay usuarios registrados");
            }

            return GanaderoMapper.INSTANCE.convertToDtoPage(ganaderoPage);

        } catch (DataAccessException e) {
            // Manejar errores de acceso a datos
            log.error("Error de acceso a datos: " + e.getMessage());
            throw e;
        } catch (CustomerNotExistException e) {
            // Manejar errores de acceso a datos
            log.error("Error de acceso a datos: " + e.getMessage());
            throw e;

        } catch (Exception e) {
            // Manejar cualquier otra excepción no capturada anteriormente
            log.error("Error inesperado: " + e.getMessage());
            throw new RuntimeException("Error inesperado " + e.getMessage());

        }
    }

    @SuppressWarnings("null")
    @Override
    public Boolean UpdateGanadero(MultipartFile fileSupportDocuments, GanaderoDto ganderoDto,
            MultipartFile[] imageMarcaGanadero) {

        try {

            Ganadero ganaderoEntity = ganaderoRepository.findById(ganderoDto.getId())
                    .orElseThrow(() -> new CustomerNotExistException("El usuario no existe"));

            // si viene con archivos y con imagenes
            if (fileSupportDocuments != null && imageMarcaGanadero.length > 0) {

                String uniqueFileNameDocuments = createNameUniqueFile(fileSupportDocuments.getOriginalFilename());
                // String uniqueFileImageMarcaGandera =
                // createNameUniqueFile(imageMarcaGanadero.getOriginalFilename());

                // Convierte los nombres de archivo de los MultipartFile en un array de String
                String[] filesNameImageMarcaGanadero = Arrays.stream(imageMarcaGanadero)
                        .map(MultipartFile::getOriginalFilename)
                        .toArray(String[]::new);

                // sube los Files al Ftp
                supportDocumentsImpl.UploadMultipleFilesToFTP(imageMarcaGanadero,
                        Utils.NAME_FOLDER_IMAGES_MARCA_GANADERA, filesNameImageMarcaGanadero);

                SupportDocument supportDocument = supportDocumentsImpl.saveFileDocument(fileSupportDocuments,
                        uniqueFileNameDocuments,
                        Utils.NAME_FOLDER_SUPPORTDOCUMENTS);

                Set<MarcaGanadera> setMarcaGanaderas = MarcaganaderaMapper.INSTANCE
                        .UpdateMarcaGanadera(ganderoDto.getMarcasGanaderaDtos().get(0), ganaderoEntity,
                                filesNameImageMarcaGanadero);
                ganaderoEntity.setMarcaGanadera(setMarcaGanaderas);
                ganaderoEntity.setSupportDocument(supportDocument);

            }
            // si no viene con archivos y con imagenes
            if (fileSupportDocuments == null && imageMarcaGanadero == null) {
                updateganaderoSinFiles(ganderoDto, ganaderoEntity);

            }

            if (fileSupportDocuments != null && imageMarcaGanadero.length == 0) {
                String uniqueFileNameDocuments = createNameUniqueFile(fileSupportDocuments.getOriginalFilename());
                SupportDocument supportDocument = supportDocumentsImpl.saveFileDocument(fileSupportDocuments,
                        uniqueFileNameDocuments,
                        Utils.NAME_FOLDER_SUPPORTDOCUMENTS);
                ganaderoEntity.setSupportDocument(supportDocument);
            }

            if (imageMarcaGanadero != null && fileSupportDocuments == null) {
                String[] filesNameImageMarcaGanadero = Arrays.stream(imageMarcaGanadero)
                        .map(MultipartFile::getOriginalFilename)
                        .map(name -> createNameUniqueFileImage(name, 0, 5))
                        .toArray(String[]::new);

                supportDocumentsImpl.UploadMultipleFilesToFTP(imageMarcaGanadero,
                        Utils.NAME_FOLDER_IMAGES_MARCA_GANADERA, filesNameImageMarcaGanadero);

                Set<MarcaGanadera> setMarcaGanaderas = MarcaganaderaMapper.INSTANCE
                        .UpdateMarcaGanadera(ganderoDto.getMarcasGanaderaDtos().get(0), ganaderoEntity,
                                filesNameImageMarcaGanadero);
                ganaderoEntity.setMarcaGanadera(setMarcaGanaderas);
            }

            ganaderoEntity.setFirstName(ganderoDto.getFirstName());
            ganaderoEntity.setLastName(ganderoDto.getLastName());
            ganaderoEntity.setPhone(ganderoDto.getPhone());
            ganaderoEntity.setIdentificacion(ganderoDto.getIdentificacion());

            ganaderoRepository.save(ganaderoEntity);
            return true;

        } catch (DataAccessException e) {
            // Manejar errores de acceso a datos
            log.error("Error de acceso a datos: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Manejar cualquier otra excepción no capturada anteriormente
            log.error("Error inesperado: " + e.getMessage());
            throw new RuntimeException("Error inesperado " + e.getMessage());

        }

    }

    private void updateganaderoSinFiles(GanaderoDto ganderoDto, Ganadero ganaderoEntity) {
        ganaderoEntity.getMarcaGanadera().forEach(marca -> {
            if (marca.getId() == ganderoDto.getMarcasGanaderaDtos().stream().findFirst().get().getId()) {

                marca.setDescription(
                        ganderoDto.getMarcasGanaderaDtos().stream().findFirst().get().getDescription());
                marca.getUbicacionList().forEach(ubicacion -> {
                    ubicacion.setDirection(ganderoDto.getMarcasGanaderaDtos().stream().findFirst().get()
                            .getUbicacionDtoList().stream().findFirst().get().getDirection());
                    ubicacion.setNameCorregimiento(ganderoDto.getMarcasGanaderaDtos().stream().findFirst().get()
                            .getUbicacionDtoList().stream().findFirst().get().getNameCorregimiento());
                    ubicacion.setNameDepartamento(ganderoDto.getMarcasGanaderaDtos().stream().findFirst().get()
                            .getUbicacionDtoList().stream().findFirst().get().getNameDepartamento());
                    ubicacion.setNameMunicipio(ganderoDto.getMarcasGanaderaDtos().stream().findFirst().get()
                            .getUbicacionDtoList().stream().findFirst().get().getNameMunicipio());
                    ubicacion.getZona()
                            .setCodigoPostalCode(ganderoDto.getMarcasGanaderaDtos().stream().findFirst().get()
                                    .getUbicacionDtoList().stream().findFirst().get().getZonaDto()
                                    .getCodigoPostalCode());
                });

            }
        });
    }

}
