package com.alcadia.bovid.Service;

import java.util.Date;
import java.util.Set;

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
import com.alcadia.bovid.Service.UserCase.IRequestService;
import com.itextpdf.commons.bouncycastle.cert.ocsp.IReq;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class HistorialAuditoriaServiceImpl implements IHistorialAuditoriaService {

    final private IHistorialAuditoriaRepository historialAuditoriaRepository;
    final private IRequestService requestService;

    @Override
    public String registerHistorial(HttpServletRequest servletRequest, User user) {

        HisotiralAuditor hisotiralAuditor = new HisotiralAuditor();

        final String ipClient = requestService.getClientIp(servletRequest);

        hisotiralAuditor.setIpComputer(ipClient);
        hisotiralAuditor.setUsers(user);

        historialAuditoriaRepository.save(hisotiralAuditor);

        log.info(hisotiralAuditor.toString());

        return null;

    }

    @Override
    public Set<HisotiralAuditor> findByUsersId(Long userId) {

        return historialAuditoriaRepository.findByUsersId(userId);
    }

    @Override
    public Boolean logout(Long idUser, HttpServletRequest servletRequest) {

        Set<HisotiralAuditor> historialEntity = findByUsersId(idUser);
        historialEntity.forEach(historial -> {
            historial.setLogoutDate(new Date());
        });

        historialAuditoriaRepository.saveAll(historialEntity);

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
