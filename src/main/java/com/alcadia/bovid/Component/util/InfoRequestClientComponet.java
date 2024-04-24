package com.alcadia.bovid.Component.util;



import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class InfoRequestClientComponet {

    private final String LOCALHOST_IPV4 = "127.0.0.1";
    private final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    public String getClientIp(HttpServletRequest request) {

        String ipv4Address = request.getHeader("X-Forwarded-For");

        String ipv6Local = "vacio";

        if (StringUtils.isEmpty(ipv4Address) || "unknown".equalsIgnoreCase(ipv4Address)) {
            ipv4Address = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ipv4Address) || "unknown".equalsIgnoreCase(ipv4Address)) {
            ipv4Address = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ipv4Address) || "unknown".equalsIgnoreCase(ipv4Address)) {
            ipv4Address = request.getRemoteAddr();
            if (LOCALHOST_IPV4.equals(ipv4Address) || LOCALHOST_IPV6.equals(ipv4Address)) {
                try {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ipv4Address = inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!StringUtils.isEmpty(ipv4Address)
                && ipv4Address.length() > 15
                && ipv4Address.indexOf(",") > 0) {
            System.out.println("===================================ipv4Address: " + ipv4Address);
            ipv4Address = ipv4Address.substring(0, ipv4Address.indexOf(","));
        }

        return "IPV4: " + ipv4Address;
    }

    public String getHostClient(HttpServletRequest request) {
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");

            if (StringUtils.isEmpty(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }

}
