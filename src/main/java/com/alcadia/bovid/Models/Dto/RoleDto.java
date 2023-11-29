package com.alcadia.bovid.Models.Dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String newNameRole;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String authority;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UserDto> users;

}
