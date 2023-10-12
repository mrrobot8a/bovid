package com.alcadia.bovid.Service.UserCase;

import org.springframework.web.multipart.MultipartFile;

import com.alcadia.bovid.Models.Dto.GanderoDto;

public interface IGanaderoService {


    Boolean SaveGanadero(MultipartFile file, GanderoDto ganderoDto);
    
}
