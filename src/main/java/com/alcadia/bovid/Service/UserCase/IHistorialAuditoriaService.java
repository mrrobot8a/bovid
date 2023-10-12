package com.alcadia.bovid.Service.UserCase;


import java.util.Set;

import org.springframework.data.domain.Page;


import com.alcadia.bovid.Models.Dto.HistoryAuditordDto;
import com.alcadia.bovid.Models.Entity.HisotiralAuditor;
import com.alcadia.bovid.Models.Entity.User;

import jakarta.servlet.http.HttpServletRequest;

public interface IHistorialAuditoriaService {

    String registerHistorial(final HttpServletRequest servletRequest, User user);

    Set<HisotiralAuditor> findByUsersId(Long userId);

    Boolean logout(Long idHistorial, HttpServletRequest servletRequest);

    Page<HistoryAuditordDto> getAllHisotiralesPage(int page, int size);
     



    

    
}
