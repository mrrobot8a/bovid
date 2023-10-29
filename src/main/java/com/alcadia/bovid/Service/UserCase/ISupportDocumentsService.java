package com.alcadia.bovid.Service.UserCase;

import java.net.SocketException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

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

    public InputStreamResource download(String filName) throws IOException, SocketException, java.io.IOException;

}
