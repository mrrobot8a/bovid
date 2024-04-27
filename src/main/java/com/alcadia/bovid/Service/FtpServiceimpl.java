package com.alcadia.bovid.Service;

import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import com.alcadia.bovid.Exception.ErrorMessage;
import com.alcadia.bovid.Exception.FtpErrors;
import com.alcadia.bovid.Service.UserCase.IFtpService;

import com.itextpdf.io.exceptions.IOException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class FtpServiceimpl implements IFtpService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final static String UPLOADS_FOLDER = "uploads/";

    private FTPClient ftpconnection;

    @Value("${ftp.server}")
    private String server;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.username}")
    private String user;

    @Value("${ftp.password}")
    private String pass;

    @Override
    public void connectToFTP() throws FtpErrors, SocketException, java.io.IOException {
        ftpconnection = new FTPClient();
        ftpconnection.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;

        try {
            ftpconnection.connect(server);
            log.info("logservide", ftpconnection.getReplyCode());

        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible conectarse al FTP a través del host=" + server);
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);
        }

        reply = ftpconnection.getReplyCode();

        if (!FTPReply.isPositiveCompletion(reply)) {

            try {
                ftpconnection.disconnect();
            } catch (Exception e) {
                ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                        "No fue posible conectarse al FTP, el host=" + server + " entregó la respuesta=" + reply);
                log.error(errorMessage.toString());
                throw new FtpErrors(errorMessage);
            }
        }

        try {
            ftpconnection.login(user, pass);
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage(HttpStatus.UNAUTHORIZED,
                    "El usuario=" + user + ", y el pass=**** no fueron válidos para la autenticación.");
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);
        }

        try {
            ftpconnection.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,
                    "El tipo de dato para la transferencia no es válido.");
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);
        }

        ftpconnection.enterLocalPassiveMode();
    }

    @Override
    public void uploadFileToFTP(InputStream file, String ftpHostDir, String serverFilename)
            throws FtpErrors, java.io.IOException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {

            try {
                // InputStream input = new FileInputStream(file);
                this.ftpconnection.storeFile(ftpHostDir + serverFilename, file);

            } catch (Exception e) {
                log.error("Error al subir archivo al servidor FTP", e.getCause() + e.getMessage());
                ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                        "No se pudo subir el archivo al servidor SOLO UN ARCHIVO." + e.getMessage());
                log.error(errorMessage.toString());
                throw new FtpErrors(errorMessage);
            }
        }).exceptionally(ex -> {
            log.error("Error al subir archivo al servidor SOLO UN ARCHIVO FTP", ex);
            return null;
        });

        // Esperar a que todas las operaciones asincrónicas se completen
        future.join();
    }

    @Override
    public InputStream downloadFileFromFTP(String ftpRelativePath, String folder)
            throws FtpErrors, java.io.IOException {
        InputStream inputStream;
        try {
            inputStream = ftpconnection.retrieveFileStream(folder + ftpRelativePath);

            if (inputStream == null) {
                ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                        "No se pudo descargar el archivo.");
                log.error("EROOR DESDE LA CLASEFTPSERVICE Ftp", errorMessage.toString());
                throw new FtpErrors(errorMessage);
            }
            log.info("Archivo descargado correctamente"+inputStream.available() + " bytes.");
            return inputStream;
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo descargar el archivo.");
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);
        }

    }

    @Override
    public void disconnectFTP() throws FtpErrors, java.io.IOException {
        if (this.ftpconnection.isConnected()) {
            try {
                this.ftpconnection.logout();
                this.ftpconnection.disconnect();
            } catch (IOException f) {
                throw new FtpErrors(
                        new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Ha ocurrido un error al realizar la desconexión del servidor FTP"));
            }
        }
    }

    @Override
    public void getallFiles() {
        FTPFile[] list;
        try {

            list = this.ftpconnection.listFiles(UPLOADS_FOLDER);

            for (FTPFile ftpFile : list) {
                System.out.println("File " + ftpFile.getName());
            }

        } catch (java.io.IOException e) {
            //
            e.printStackTrace();
        }
    }

    @Override
    public void UploadMultipleFilesToFTP(InputStream[] files, String ftpHostDir, String[] serverFilename)
            throws FtpErrors, java.io.IOException {

        try {

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    for (int i = 0; i < files.length; i++) {
                        this.ftpconnection.storeFile(ftpHostDir + serverFilename[i], files[i]);
                    }
                } catch (Exception e) {
                    ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                            "No se pudo subir el archivo al servidor. VARIOS ARCHIVOS" + e.getMessage());
                    log.error(errorMessage.toString());
                    throw new FtpErrors(errorMessage);
                }
            });

            // Esperar a que todas las operaciones asincrónicas se completen
            future.join();
        } catch (Exception e) {
            // TODO: handle exception
            ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo subir el archivo al servidor. VARIOS ARCHIVOS" + e.getMessage());
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);
        }
    }
}
