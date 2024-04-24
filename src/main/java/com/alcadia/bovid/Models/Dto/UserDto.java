package com.alcadia.bovid.Models.Dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String fullName;

    private Long id;

    private String firstName;

    private String lastName;

    private String position;

    private String numberPhone;

    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    private List<RoleDto> roles;
    private boolean enabled;

    // @JsonInclude(JsonInclude.Include.NON_NULL)
    // private boolean isEnabled;

    public UserDto(Long id, String firsName, String lastName, String email, String password, List<RoleDto> roles,
            String position,
            String numberPhone, boolean enabled) {
        this.id = id;
        this.firstName = firsName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.position = position;
        this.numberPhone = numberPhone;
        this.enabled = enabled;
    }

    // Método estático para crear una instancia del Builder
    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    // Clase Builder interna
    public static class UserDtoBuilder {
        private String fullname;

        private String firstName;

        private String lastName;
        private String email;
        private String password;
        private List<RoleDto> roles;
        // private boolean isEnabled;
        private boolean enabled;

        public UserDtoBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserDtoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

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

        public UserDtoBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }
        // public UserDtoBuilder isEnabled(boolean isEnabled) {
        // this.isEnabled = isEnabled;
        // return this;
        // }

        // Método para construir una instancia de UserDto
        public UserDto build() {
            UserDto userDto = new UserDto();
            userDto.fullName = this.fullname;
            userDto.firstName = this.firstName;
            userDto.lastName = this.lastName;
            userDto.email = this.email;
            userDto.password = this.password;
            userDto.roles = this.roles;
            // userDto.isEnabled = this.isEnabled;
            userDto.enabled = this.enabled;
            return userDto;
        }
    }
}
