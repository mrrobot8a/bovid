package com.alcadia.bovid.Service;

import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.SocketException;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import org.springframework.beans.factory.annotation.Value;
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
            ErrorMessage errorMessage = new ErrorMessage(-1,
                    "No fue posible conectarse al FTP a través del host=" + server);
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);
        }

        reply = ftpconnection.getReplyCode();

        if (!FTPReply.isPositiveCompletion(reply)) {

            try {
                ftpconnection.disconnect();
            } catch (Exception e) {
                ErrorMessage errorMessage = new ErrorMessage(-2,
                        "No fue posible conectarse al FTP, el host=" + server + " entregó la respuesta=" + reply);
                log.error(errorMessage.toString());
                throw new FtpErrors(errorMessage);
            }
        }

        try {
            ftpconnection.login(user, pass);
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage(-3,
                    "El usuario=" + user + ", y el pass=**** no fueron válidos para la autenticación.");
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);
        }

        try {
            ftpconnection.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage(-4, "El tipo de dato para la transferencia no es válido.");
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);
        }

        ftpconnection.enterLocalPassiveMode();
    }

    @Override
    public void uploadFileToFTP(InputStream file, String ftpHostDir, String serverFilename)
            throws FtpErrors, java.io.IOException {
        try {
            // InputStream input = new FileInputStream(file);
            this.ftpconnection.storeFile(ftpHostDir + serverFilename, file);

        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage(-5, "No se pudo subir el archivo al servidor." + e.getMessage());
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);
        }
    }

    @Override
    public InputStream downloadFileFromFTP(String ftpRelativePath , String folder) throws FtpErrors, java.io.IOException {
        InputStream inputStream;
        try {
            inputStream = ftpconnection.retrieveFileStream(folder + ftpRelativePath);

            if (inputStream == null) {
                ErrorMessage errorMessage = new ErrorMessage(-7, "No se pudo descargar el archivo.");
                log.error("EROOR DESDE LA CLASEFTPSERVICE Ftp", errorMessage.toString());
                throw new FtpErrors(errorMessage);
            }
            return inputStream;
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage(-7, "No se pudo descargar el archivo.");
            log.error(errorMessage.toString());
            throw new FtpErrors(errorMessage);
        }



        //metodo para guardar los archivos en una carpeta del sistema
        // try {
        // File file = new File(ftpRelativePath);
        // FileOutputStream fos = new FileOutputStream(file);

        // byte[] buffer = new byte[1024];
        // int bytesRead;
        // while ((bytesRead = inputStream.read(buffer)) != -1) {
        // fos.write(buffer, 0, bytesRead);
        // }

        // fos.close();
        // inputStream.close();

        // return file;
        // } catch (Exception e) {
        // ErrorMessage errorMessage = new ErrorMessage(-6, "No se pudo obtener la
        // referencia al archivo descargado.");
        // log.error(errorMessage.toString());
        // throw newFtpErrors(errorMessage);
        // }
    }

    @Override
    public void disconnectFTP() throws FtpErrors, java.io.IOException {
        if (this.ftpconnection.isConnected()) {
            try {
                this.ftpconnection.logout();
                this.ftpconnection.disconnect();
            } catch (IOException f) {
                throw new FtpErrors(
                        new ErrorMessage(-8, "Ha ocurrido un error al realizar la desconexión del servidor FTP"));
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

}
