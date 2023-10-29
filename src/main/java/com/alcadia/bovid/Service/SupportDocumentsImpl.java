package com.alcadia.bovid.Service;

import java.io.InputStream;
import java.net.SocketException;

import org.springframework.core.io.InputStreamResource;
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

    @Override
    @Transactional
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
            ErrorMessage errorMessage = new ErrorMessage(-5,
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
    @Transactional
    public InputStreamResource download(String fileName)
            throws IOException, SocketException, java.io.IOException {

        try {

            if (!supportDocumentsRepository.existsByUrlFile(Utils.URL_BASE.concat(fileName)) && !fileName.toLowerCase().endsWith(".jpg")) {
                ErrorMessage errorMessage = new ErrorMessage(-1,
                        "No existe el archivo en la base de datos");
                log.error(errorMessage.toString());
                throw new FtpErrors(errorMessage);
            }

            ftpServiceimpl.connectToFTP();
            // ftpServiceimpl.getallFiles();
            InputStream fileInputStreamFtp = ftpServiceimpl.downloadFileFromFTP(fileName,Utils.NAME_FOLDER_SUPPORTDOCUMENTS);
            InputStreamResource fileResource = new InputStreamResource(fileInputStreamFtp);
            ftpServiceimpl.disconnectFTP();
            return fileResource;
        } catch (FtpErrors ftpErrors) {
            System.out.println(ftpErrors.getMessage());
            log.info("ftpErrors " + ftpErrors.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(-1,
                    "No existe el archivo en la base de datos");
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);

        }

    }

}
