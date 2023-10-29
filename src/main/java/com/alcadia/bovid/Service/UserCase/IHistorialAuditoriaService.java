package com.alcadia.bovid.Service.UserCase;


import java.util.Set;

import org.springframework.data.domain.Page;


import com.alcadia.bovid.Models.Dto.HistoryAuditordDto;

import com.alcadia.bovid.Models.Entity.HisotiralAuditor;


public interface IHistorialAuditoriaService {

    void  registerHistorial(String actionUser, String ipClient,String httpMethod,String url,String emailUser);
    
    Set<HisotiralAuditor> findByUsersId(Long userId);

    Boolean logout(String emailUser);

    Page<HistoryAuditordDto> getAllHisotiralesPage(int page, int size);
     



    

    
}
