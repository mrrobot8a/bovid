package com.alcadia.bovid.Service;

import org.springframework.stereotype.Service;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import jakarta.servlet.http.HttpServletRequest;

import com.alcadia.bovid.Service.UserCase.IRequestService;

import io.micrometer.common.util.StringUtils;

@Service
public class RequestServiceImpl implements IRequestService {

    private final String LOCALHOST_IPV4 = "127.0.0.1";
    private final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    @Override
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

}