package com.alcadia.bovid.Exception;

import java.net.http.HttpHeaders;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.web.HttpRequestMethodNotSupportedException;

import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class HttpRequestMethodNotSupportedAdvice {

    private final RequestMappingHandlerMapping handlerMapping;

    // // public HttpRequestMethodNotSupportedAdvice(RequestMappingHandlerMapping
    // // handlerMapping) {
    // // this.handlerMapping = handlerMapping;
    // // }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String handleMethodNotSupported(HttpServletRequest request, Exception ex) {
        String requestURL = request.getRequestURI();
        log.info("requestURL: " + requestURL);
        String supportedMethods = getSupportedMethods(requestURL);

        log.info(supportedMethods.toString());

        return "Método no permitido: " + ((HttpRequestMethodNotSupportedException) ex).getMethod() + " al Url: "
                + requestURL +
                ". Métodos permitidos: " + supportedMethods;
    }

    private String getSupportedMethods(String requestURL) {

        List<String> methods = handlerMapping.getHandlerMethods().entrySet().stream()
                .filter(entry -> {

                    RequestMappingInfo mappingInfo = entry.getKey();
                    return null != mappingInfo && mappingInfo.toString().contains(requestURL);
                    // PatternsRequestCondition patternsCondition =
                    // // entry.getKey().getPatternsCondition();
                    // return patternsCondition != null &&
                    // patternsCondition.getPatterns().contains(requestURL);
                })
                .flatMap(entry -> entry.getKey().getMethodsCondition().getMethods().stream())
                .map(Enum::name)
                .collect(Collectors.toList());

        return String.join(", ", methods);
    }

    // public List<String> getAllowedMethodsForEndpoint(String endpoint) {

    // return handlerMapping.getHandlerMethods().entrySet().stream()
    // .map(entry -> {
    // RequestMappingInfo mappingInfo = entry.getKey();
    // log.info("patternsCondition: " + mappingInfo.getName() + "ooooo:" +
    // entry.getKey());

    // if (null != mappingInfo && mappingInfo.toString().contains(endpoint)) {

    // return mappingInfo.toString();
    // }
    // return " ";

    // })
    // .collect(Collectors.toList());

    // }

}