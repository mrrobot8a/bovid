


package com.alcadia.bovid.Service.UserCase;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.alcadia.bovid.Models.Dto.GanaderoDto;
import com.alcadia.bovid.Models.Entity.MarcaGanadera;

public interface IMarcaGanaderaService {

 
    List<GanaderoDto> buscarPorEtiqueta(MultipartFile imageData);

}