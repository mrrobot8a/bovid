package com.alcadia.bovid.Models.Dto;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HistoryAuditordDto {

    private String ipComputer;
    private Date fechaIncio;
    private Date LogoutDate;

    private UserDto users;

}
