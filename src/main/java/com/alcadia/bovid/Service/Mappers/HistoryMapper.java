package com.alcadia.bovid.Service.Mappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.alcadia.bovid.Models.Dto.HistoryAuditordDto;

import com.alcadia.bovid.Models.Entity.HisotiralAuditor;

public enum HistoryMapper implements Function<HisotiralAuditor, HistoryAuditordDto> {
    INSTANCE;

    @SuppressWarnings("null")
    public Page<HistoryAuditordDto> pageHistoryToPageHistoryDto(Page<HisotiralAuditor> historial) {

        if (historial != null) {
            List<HistoryAuditordDto> historialDto = historial.getContent().stream()
                    .map((historyEntity) -> {
                        
                        String formattedSignInDate = getFormattedSignInDate(historyEntity);
                        

                        return new HistoryAuditordDto(historyEntity.getIpComputer(),
                                formattedSignInDate, historyEntity.getActionUser(),
                                UserMapper.INSTANCE.apply(historyEntity.getUsers()));

                    }).collect(Collectors.toList());

            return new PageImpl<>(historialDto, historial.getPageable(), historial.getTotalElements()

            );

        }

        return null;

    }

    private String getFormattedSignInDate(HisotiralAuditor historyEntity) {

        
        // Convertir la fecha de inicio de sesión a LocalDateTime
        LocalDateTime signInDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(historyEntity.getFechaCreacion().getTime()),
                ZoneId.systemDefault());

        // Formatear la fecha y hora
        String formattedSignInDate = signInDateTime
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return formattedSignInDate;
    }

    @Override
    public HistoryAuditordDto apply(HisotiralAuditor t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'apply'");
    }

}
