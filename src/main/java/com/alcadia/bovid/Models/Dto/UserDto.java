package com.alcadia.bovid.Models.Dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

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

    // Método estático para crear una instancia del Builder
    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    // Clase Builder interna
    public static class UserDtoBuilder {
        private String fullname;
        private String email;
        private String password;
        private List<RoleDto> roles;
        private boolean isEnabled;

        public UserDtoBuilder fullname(String fullname) {
            this.fullname = fullname;
            return this;
        }

        public UserDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserDtoBuilder roles(List<RoleDto> roles) {
            this.roles = roles;
            return this;
        }

        public UserDtoBuilder isEnabled(boolean isEnabled) {
            this.isEnabled = isEnabled;
            return this;
        }

        // Método para construir una instancia de UserDto
        public UserDto build() {
            UserDto userDto = new UserDto();
            userDto.fullname = this.fullname;
            userDto.email = this.email;
            userDto.password = this.password;
            userDto.roles = this.roles;
            userDto.isEnabled = this.isEnabled;
            return userDto;
        }
    }
}
