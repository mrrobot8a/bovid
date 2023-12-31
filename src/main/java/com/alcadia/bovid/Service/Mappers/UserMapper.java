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

public enum UserMapper implements Function<User, UserDto> {
    INSTANCE;

    @Override
    public UserDto apply(User userEntity) {

        if (userEntity != null) {

            UserDto userDto = new UserDto();

            userDto.setId(userEntity.getId());
            
            userDto.setFullname(userEntity.getFullname());

            userDto.setEmail(userEntity.getEmail());

            userDto.setEnabled(userEntity.isEnabled());

            List<RoleDto> roles = new ArrayList<>();

            for (GrantedAuthority grantedAuthority : userEntity.getAuthorities()) {

                roles.add(new RoleDto(grantedAuthority.getAuthority(), grantedAuthority.getAuthority(), null));

            }

            userDto.setRoles(roles);

            return userDto;

        }

        return null;

    }

    public User UserToUserDto(UserDto userDto) {

        if (userDto != null) {

            User userEntity = new User();

            userEntity.setId(userDto.getId());

            userEntity.setFullname(userDto.getFullname());

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
                    .map(user -> new UserDto(user.getFullname(), user.getEmail(),
                            user.getAuthorities().stream()
                                    .map(role -> new RoleDto(null, role.getAuthority(), null))
                                    .collect(Collectors.toList())))
                    .collect(Collectors.toList());

            System.out.println("============userDtos================>" + userDtos.toString());

            return new PageImpl<>(userDtos, users.getPageable(), users.getTotalElements());
        }
        return Page.empty(); // Otra opción es return null, dependiendo de cómo quieras manejarlo.
    }

}
