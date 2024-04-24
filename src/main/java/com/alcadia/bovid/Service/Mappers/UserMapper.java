package com.alcadia.bovid.Service.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.GrantedAuthority;

import com.alcadia.bovid.Models.Dto.RoleDto;
import com.alcadia.bovid.Models.Dto.UserDto;

import com.alcadia.bovid.Models.Entity.User;
import com.alcadia.bovid.Models.Entity.Role;

public enum UserMapper implements Function<User, UserDto> {
    INSTANCE;

    @Override
    public UserDto apply(User userEntity) {

        if (userEntity != null) {

            UserDto userDto = new UserDto();
            userDto.setId(userEntity.getId());
            userDto.setFirstName(userEntity.getFirstName());
            userDto.setLastName(userEntity.getLastName());
            userDto.setPassword(userEntity.getPassword());
            userDto.setEmail(userEntity.getEmail());
            userDto.setEnabled(userEntity.isEnabled());
            userDto.setNumberPhone(userEntity.getFuncionario().getNumberPhone());
            userDto.setPosition(userEntity.getFuncionario().getPosition());

            List<RoleDto> roles = new ArrayList<>();

            for (GrantedAuthority grantedAuthority : userEntity.getAuthorities()) {

            roles.add(new RoleDto(grantedAuthority.getAuthority(),
                    ((Role) grantedAuthority).getStatus()));

            }

            // roles = userEntity.getAuthorities().stream()
            //         .map( role -> RoleDto.builder()
            //                 .authority(role.getAuthority()).status(((Role) role).getStatus()).build())
            //         .collect(Collectors.toList());

            userDto.setRoles(roles);

            return userDto;

        }

        return null;

    }

    public User UserToUserDto(UserDto userDto) {

        if (userDto != null) {

            User userEntity = new User();

            userEntity.setFirstName(userDto.getFirstName());
            userEntity.setLastName(userDto.getLastName());

            userEntity.setEmail(userDto.getEmail());

            userEntity.setPassword(userDto.getPassword());

            return userEntity;

        }

        return null;

    }

    public Page<UserDto> ListUserToListUserDto(Page<User> users) {

        if (users != null) {
            List<UserDto> userDtos = users.getContent()
                    .stream()
                    .map(user -> new UserDto(user.getId(),user.getFirstName(), user.getLastName(), user.getEmail(),
                            user.getPassword(),
                            user.getAuthorities().stream()
                                    .map(role -> new RoleDto(role.getAuthority(), ((Role) role).getStatus()))
                                    .collect(Collectors.toList()),
                            user.getFuncionario().getPosition(), user.getFuncionario().getNumberPhone(),
                            user.isEnabled()))
                    .collect(Collectors.toList());

            System.out.println("============userDtos================>" + userDtos.toString());

            return new PageImpl<>(userDtos, users.getPageable(), users.getTotalElements());
        }
        return Page.empty(); // Otra opción es return null, dependiendo de cómo quieras manejarlo.
    }

}
