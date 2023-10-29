package com.alcadia.bovid.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alcadia.bovid.Exception.RoleNoExistsException;

import com.alcadia.bovid.Models.Dto.HistoryAuditordDto;

import com.alcadia.bovid.Models.Entity.HisotiralAuditor;
import com.alcadia.bovid.Models.Entity.User;
import com.alcadia.bovid.Repository.Dao.IHistorialAuditoriaRepository;
import com.alcadia.bovid.Service.Mappers.HistoryMapper;

import com.alcadia.bovid.Service.UserCase.IHistorialAuditoriaService;
import com.alcadia.bovid.Service.UserCase.IUserService;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class HistorialAuditoriaServiceImpl implements IHistorialAuditoriaService {

    final private IHistorialAuditoriaRepository historialAuditoriaRepository;
    
    final private IUserService userService;

    @Override
    @Transactional
    public void registerHistorial(String actionUser, String ipClient, String httpMethod, String url, String emailUser) {
        try {

            HisotiralAuditor hisotiralAuditor = new HisotiralAuditor();

            if (actionUser.equals("signOut")) {
                hisotiralAuditor.setLogoutDate(new Date());
            }

            //
            User userEntity = userService.findByEmail(emailUser);

            System.out.println("histori====================================" + httpMethod + url);

            hisotiralAuditor.setHttpMethod(httpMethod);
            hisotiralAuditor.setActionUser(actionUser);
            hisotiralAuditor.setUrl(url);
            hisotiralAuditor.setIpComputer(ipClient);

            hisotiralAuditor.setUsers(userEntity);

            historialAuditoriaRepository.save(hisotiralAuditor);

            log.info(hisotiralAuditor.toString());

        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            System.out.println("=============" + response);

        }

    }

    @Override
    public Set<HisotiralAuditor> findByUsersId(Long userId) {

        return historialAuditoriaRepository.findByUsersId(userId);
    }

    @Override
    public Boolean logout(String emailUser) {

        // Set<HisotiralAuditor> historialEntity = userService.findByEmail(emailUser);

        // historialEntity.forEach(historial -> {
        // historial.setLogoutDate(new Date());
        // });

        // // historialAuditoriaRepository.saveAll(historialEntity);

        return true;

    }

    @Override
    public Page<HistoryAuditordDto> getAllHisotiralesPage(int page, int size) {

        try {

            Pageable pageable = PageRequest.of(page, size);

            Page<HisotiralAuditor> historialAuditor = historialAuditoriaRepository.findAll(pageable);

            if (historialAuditor.isEmpty()) {
                throw new RoleNoExistsException("Historial vacio");
            }

            return HistoryMapper.INSTANCE.pageHistoryToPageHistoryDto(historialAuditor);

        } catch (Exception e) {

            throw new RoleNoExistsException("Historial vacio");

        }

    }

}
