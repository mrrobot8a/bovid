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

    public RoleDto(Long id,String codRole, String description,String authority ,boolean status) {
      
        this.codRole = codRole;
        this.description = description;
        this.status = status;
        this.authority = authority;
        this.id = id;
        
    }

    public RoleDto(String authority,Boolean status) {
        this.authority = authority;
        this.status = status;
    }

    private Long id;
    private String codRole;
    private boolean status;
    private String description;
    private String authority;
    private List<UserDto> users;

}
