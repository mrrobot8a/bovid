package com.alcadia.bovid.Models.Dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto {

    public RoleDto (String nameRole, String codRole, String description) {
        this.nameRole = nameRole;
        
        this.codRole = codRole;
        this.description = description;
    }

    private String nameRole;
    private String oldNameRole;
    private String codRole;
    private boolean status;
    private String description;
    private String authority;
    private List<UserDto> users;

}
