package com.alcadia.bovid.Service.Util;

import org.springframework.beans.factory.annotation.Value;

public class Utils {

    @Value("${ftp.server}")
    private static String IpServerFtp;

    public final static String UPLOADS_FOLDER = "C:\\temp\\uploads";

    public final static String NAME_FOLDER = "uploads/";

    public final static String URL_BASE = "ftp://" + "192.168.0.20" + "/uploads/";

}
