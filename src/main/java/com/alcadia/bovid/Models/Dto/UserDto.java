package com.alcadia.bovid.Models.Dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fullname;

    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    private List<RoleDto> roles;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isEnabled;

    public UserDto(String fullname, String email, List<RoleDto> roles) {
        this.fullname = fullname;
        this.email = email;
        this.roles = roles;
    }

}
