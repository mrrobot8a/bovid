package com.alcadia.bovid.Service.Mappers;

import java.util.function.Function;

import com.alcadia.bovid.Models.Dto.RegistrationResponse;
import com.alcadia.bovid.Models.Entity.User;

public enum UserToRegistrationResponse implements Function<User, RegistrationResponse> {

    INSTANCE;

    

    @Override
    public RegistrationResponse apply(User userEntity) {

        RegistrationResponse registrationResponse = new RegistrationResponse(userEntity.getUsername(),
                userEntity.getEmail());
        return registrationResponse;

    }

}
