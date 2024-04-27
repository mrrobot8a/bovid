package com.alcadia.bovid.Service.UserCase;

import java.io.InputStream;
import java.net.SocketException;


import org.springframework.web.multipart.MultipartFile;

import com.alcadia.bovid.Exception.FtpErrors;
import com.alcadia.bovid.Models.Entity.SupportDocument;
import com.itextpdf.io.exceptions.IOException;

public interface ISupportDocumentsService {

    SupportDocument saveFileDocument(MultipartFile file, String nameFile, String folder)
            throws IOException, SocketException, java.io.IOException;


    default SupportDocument saveFileImage(MultipartFile file, String folder, String nameFile)
            throws IOException, SocketException, java.io.IOException {
        // Proporciona valores predeterminados para "urlFile" y "folder"
        return saveFileDocument(file, nameFile, folder);
    }

    void UploadMultipleFilesToFTP(MultipartFile[] imageMarcaGanadero, String ftpHostDir, String[] serverFilename)
            throws FtpErrors, java.io.IOException;  

    public InputStream download(String filName) throws IOException, SocketException, java.io.IOException;

}
