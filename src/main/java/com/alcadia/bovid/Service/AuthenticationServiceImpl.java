package com.alcadia.bovid.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alcadia.bovid.Exception.CustomerNotExistException;
import com.alcadia.bovid.Exception.PasswordIncorrectException;
import com.alcadia.bovid.Models.Dto.RegistrationRequest;
import com.alcadia.bovid.Models.Dto.UserDto;
import com.alcadia.bovid.Models.Entity.HisotiralAuditor;
import com.alcadia.bovid.Models.Entity.User;
import com.alcadia.bovid.Repository.Dao.IUserRepository;
import com.alcadia.bovid.Security.JwtAuthenticationProvider;
import com.alcadia.bovid.Service.Mappers.UserToUserDto;
import com.alcadia.bovid.Service.UserCase.IAuthenticationService;
import com.alcadia.bovid.Service.UserCase.IHistorialAuditoriaService;
import com.auth0.jwt.interfaces.Claim;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements IAuthenticationService {

    /**
     * Clase que administra los JWTs
     */
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    /**
     * Clase que encripta contraseñas
     */
    private final PasswordEncoder passwordEncoder;

    private final IUserRepository userRepository;

    private final IHistorialAuditoriaService historialAuditoriaService;

    @Override
    public String signIn(RegistrationRequest userRegistrationRequest, HttpServletRequest servletRequest)
            throws CustomerNotExistException {

        // buscamos el usuario por email
        User userEntity = userRepository.findByEmail(userRegistrationRequest.email())
                .orElseThrow(() -> new CustomerNotExistException("El usuario ingresado no existe."));

        System.out.println(
                "==========================roles entity authentication============" + userEntity.getAuthorities());

        // if (!userEntity.isPresent()) {
        // System.out.println("no existe el usuario");
        // throw new CustomerNotExistException();
        // }

        if (!passwordEncoder.matches(userRegistrationRequest.password(), userEntity.getPassword())) {
            throw new PasswordIncorrectException();
        }

        // ApplicationUserDTO userDTO =
        // ApplicaciontionUserMapper.INSTANCE.apply(userEntity.get());
        // System.out.println("===================DTO================================="
        // + userDTO.getAuthorities());

        String token = jwtAuthenticationProvider.createToken(UserToUserDto.INSTANCE.apply(userEntity));

        historialAuditoriaService.registerHistorial(servletRequest, userEntity);

        return token;
    }

    @Override
    public String signOut(String jwt) {

        String[] authElements = jwt.split(" ");
        String email = jwtAuthenticationProvider.deleteToken(authElements[1]);
        User user = userRepository.findByEmail(email).get();

        boolean isOK = historialAuditoriaService.logout(user.getId(), null);
        if (isOK) {
            return "Sesión cerrada exitosamente";
        }
        return null;
    }

}
