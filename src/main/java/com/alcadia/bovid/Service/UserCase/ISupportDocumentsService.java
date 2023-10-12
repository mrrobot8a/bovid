package com.alcadia.bovid.Service.UserCase;

import java.io.InputStream;
import java.net.SocketException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import com.alcadia.bovid.Models.Entity.SupportDocument;
import com.itextpdf.io.exceptions.IOException;

public interface ISupportDocumentsService {

    public SupportDocument saveFileDocument(MultipartFile File,String nameFile, String urlFile);

    public InputStreamResource download(String filName) throws IOException, SocketException, java.io.IOException;

}
