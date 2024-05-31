package com.alcadia.bovid.Service;

import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alcadia.bovid.Exception.CustomerNotExistException;
import com.alcadia.bovid.Exception.ErrorMessage;
import com.alcadia.bovid.Exception.FtpErrors;
import com.alcadia.bovid.Models.Entity.SupportDocument;
import com.alcadia.bovid.Repository.Dao.ISupportDocumentsRepository;
import com.alcadia.bovid.Service.UserCase.IFtpService;
import com.alcadia.bovid.Service.UserCase.ISupportDocumentsService;
import com.alcadia.bovid.Service.Util.Utils;
import com.itextpdf.io.exceptions.IOException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupportDocumentsImpl implements ISupportDocumentsService {

    private final ISupportDocumentsRepository supportDocumentsRepository;

    private final IFtpService ftpServiceimpl;

    @SuppressWarnings("null")
    @Override
    @Transactional(rollbackOn = Exception.class)
    public SupportDocument saveFileDocument(MultipartFile file, String nameFile, String folder)
            throws IOException, SocketException, java.io.IOException {

        try {

            ftpServiceimpl.connectToFTP();

            String urlFile = Utils.URL_BASE.concat(nameFile);

            InputStream inputStream = file.getInputStream();

            SupportDocument supportDocuments = new SupportDocument();

            supportDocuments.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
            supportDocuments.setUrlFile(urlFile);

            ftpServiceimpl.uploadFileToFTP(inputStream, folder, nameFile);

            inputStream.close(); // cerrar el inputstream
            ftpServiceimpl.disconnectFTP(); // desconectar el ftp
            return supportDocumentsRepository.save(supportDocuments);

        } catch (IOException e) {

            log.error("Error al guardar el documento de soporte", e.getStackTrace().toString());
            ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo subir el archivo al servidor." + e.getMessage());

            throw new CustomerNotExistException(e.getMessage());
        }

    }

    @Override
    @Transactional
    public SupportDocument saveFileImage(MultipartFile file, String nameFile, String folder)
            throws IOException, SocketException, java.io.IOException {
        // Proporciona valores predeterminados para "urlFile" y "folder"

        ftpServiceimpl.connectToFTP();

        InputStream inputStream = file.getInputStream();

        String urlFile = Utils.URL_BASE.concat(nameFile);
        System.out.println("el nombre del archivo es: " + urlFile);

        ftpServiceimpl.uploadFileToFTP(inputStream, folder, nameFile);
        inputStream.close(); // cerrar el inputstream
        ftpServiceimpl.disconnectFTP(); // desconectar el ftp

        return SupportDocument.builder().urlFile(urlFile).build();

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> download(String fileName)
            throws IOException, SocketException, java.io.IOException {

        try {

            if (!supportDocumentsRepository.existsByFileName(fileName)
                    && !fileName.toLowerCase().endsWith(".png")) {
                ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND,
                        "No existe el archivo en la base de datos");
                log.error(errorMessage.toString());
                throw new FtpErrors(errorMessage);
            }

            String folderSearch = fileName.endsWith(".pdf") ? Utils.NAME_FOLDER_SUPPORTDOCUMENTS
                    : Utils.NAME_FOLDER_IMAGES_MARCA_GANADERA;

            
            log.info("fileInputStreamFtp primer paso : ");
            // ftpServiceimpl.getallFiles();
            InputStream fileInputStreamFtp = ftpServiceimpl.downloadFileFromFTP(fileName,
                    folderSearch);
            log.info("fileInputStreamFtp : " + fileInputStreamFtp.available()+"$$");
            {
                byte[] bytes = IOUtils.toByteArray(fileInputStreamFtp);
                fileInputStreamFtp.close();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(fileName.endsWith(".png") ? MediaType.IMAGE_PNG : MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", fileName);
                headers.setCacheControl("no-cache, no-store, must-revalidate");
                headers.setPragma("no-cache");

                return ResponseEntity.ok().headers(headers).body(bytes);
            }
        } catch (FtpErrors ftpErrors) {
            System.out.println(ftpErrors.getMessage());
            log.info("ftpErrors " + ftpErrors.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND,
                    "No existe el archivo en la base de datos");
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);

        }catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al descargar el archivo: " + e.getMessage());
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);
        }
        finally {
            ftpServiceimpl.disconnectFTP();
        
        }

    }

    @Transactional(rollbackOn = { FtpErrors.class, IOException.class, Exception.class })
    @Override
    public void UploadMultipleFilesToFTP(MultipartFile[] imageMarcaGanadero, String ftpHostDir, String[] serverFilename)
            throws FtpErrors, java.io.IOException {
        try {

            InputStream[] listFiles = getListFiles(imageMarcaGanadero);

            ftpServiceimpl.connectToFTP();
            ftpServiceimpl.UploadMultipleFilesToFTP(listFiles, ftpHostDir, serverFilename);

            // Crear y guardar instancias de SupportDocument
            List<SupportDocument> supportDocumentsList = new ArrayList<>();

            for (int i = 0; i < serverFilename.length; i++) {
                SupportDocument supportDocument = new SupportDocument();
                String urlFile = Utils.URL_BASE.concat(serverFilename[i]);

                supportDocument.setFileName(serverFilename[i]); // Establecer el nombre del archivo
                supportDocument.setUrlFile(urlFile); // Establecer la URL del archivo (URL_BASE + nombre de archivo
                // Otras configuraciones de la entidad SupportDocument según sea necesario
                supportDocumentsList.add(supportDocument);
            }

            // Guardar todas las instancias de SupportDocument en la base de datos
            supportDocumentsList = supportDocumentsRepository.saveAll(supportDocumentsList);
        } catch (DataAccessException e) {
            throw e;
        }

        catch (SocketException e) {
            ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error de conexión al servidor FTP: " + e.getMessage());
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);
        } catch (IOException e) {
            ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error de entrada/salida al transferir archivos FTP: " + e.getMessage());
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error desconocido al transferir archivos FTP: " + e.getMessage());
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);
        } finally {
            ftpServiceimpl.disconnectFTP();
        }
    }

    private InputStream[] getListFiles(MultipartFile[] imageMarcaGanadero) {
        InputStream[] listFiles = Arrays.stream(imageMarcaGanadero).map(file -> {
            try {
                return file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (java.io.IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }).toArray(InputStream[]::new);
        return listFiles;
    }

}
