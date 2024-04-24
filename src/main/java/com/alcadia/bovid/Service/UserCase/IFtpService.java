package com.alcadia.bovid.Service.UserCase;

import java.io.InputStream;
import java.net.SocketException;

import com.alcadia.bovid.Exception.FtpErrors;

public interface IFtpService {

    void connectToFTP() throws FtpErrors, SocketException, java.io.IOException;

    void uploadFileToFTP(InputStream file, String ftpHostDir, String serverFilename)
            throws FtpErrors, java.io.IOException;

    void UploadMultipleFilesToFTP(InputStream[] files, String ftpHostDir, String[] serverFilename)
            throws FtpErrors, java.io.IOException;        

    InputStream downloadFileFromFTP(String ftpRelativePath, String folder) throws FtpErrors, java.io.IOException;

    void getallFiles();

    void disconnectFTP() throws FtpErrors, java.io.IOException;



}
