package com.alcadia.bovid.Service.Mappers;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.alcadia.bovid.Models.Dto.HistoryAuditordDto;

import com.alcadia.bovid.Models.Entity.HisotiralAuditor;

public enum HistoryMapper implements Function<HisotiralAuditor, HistoryAuditordDto> {
    INSTANCE;

    public Page<HistoryAuditordDto> pageHistoryToPageHistoryDto(Page<HisotiralAuditor> historial) {

        if (historial != null) {
            List<HistoryAuditordDto> historialDto = historial.getContent().stream()
                    .map(historyEntity -> new HistoryAuditordDto(historyEntity.getIpComputer(),
                            historyEntity.getFechaIncio(), historyEntity.getLogoutDate(),
                            UserToUserDto.INSTANCE.apply(historyEntity.getUsers())))
                    .collect(Collectors.toList());

            return new PageImpl<>(historialDto, historial.getPageable(), historial.getTotalElements());

        }
        
        return null;

    }

    @Override
    public HistoryAuditordDto apply(HisotiralAuditor t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'apply'");
    }

}
