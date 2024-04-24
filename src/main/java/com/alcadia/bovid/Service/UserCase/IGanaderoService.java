package com.alcadia.bovid.Service.UserCase;

import java.net.SocketException;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.alcadia.bovid.Models.Dto.GanaderoDto;
import com.itextpdf.io.exceptions.IOException;

public interface IGanaderoService {


    Boolean SaveGanadero(MultipartFile file, GanaderoDto ganderoDto, MultipartFile[] imageMarcaGana) throws IOException, SocketException, java.io.IOException;
    Page<GanaderoDto> getAllGanaderoPage(int page, int size);
    Boolean UpdateGanadero(MultipartFile file, GanaderoDto ganderoDto, MultipartFile[] imageMarcaGana);

}
