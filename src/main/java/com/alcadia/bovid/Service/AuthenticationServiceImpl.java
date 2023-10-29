package com.alcadia.bovid.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alcadia.bovid.Component.InfoRequestClientComponet;
import com.alcadia.bovid.Exception.CustomerNotExistException;
import com.alcadia.bovid.Exception.PasswordIncorrectException;
import com.alcadia.bovid.Models.Dto.RegistrationRequest;

import com.alcadia.bovid.Models.Entity.User;
import com.alcadia.bovid.Repository.Dao.IUserRepository;
import com.alcadia.bovid.Security.JwtAuthenticationProvider;
import com.alcadia.bovid.Service.Mappers.UserMapper;
import com.alcadia.bovid.Service.UserCase.IAuthenticationService;
import com.alcadia.bovid.Service.UserCase.IHistorialAuditoriaService;

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

    @Autowired
    private InfoRequestClientComponet getInfoClientComponet;


    @Override
    public String signIn(RegistrationRequest userRegistrationRequest, HttpServletRequest request)
            throws CustomerNotExistException {

              
            // Obtiene el método HTTP y la URL
            String httpMethod = request.getMethod();
            String url = request.getRequestURL().toString();
            String ipclient = getInfoClientComponet.getClientIp(request);
            String path = request.getServletPath();
            String actionUser = "signIn";
            String jwt = request.getHeader("Authorization");


             
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

        String token = jwtAuthenticationProvider.createToken(UserMapper.INSTANCE.apply(userEntity));

        historialAuditoriaService.registerHistorial(actionUser, ipclient, httpMethod, url, userRegistrationRequest.email());


        return token;
    }

    @Override
    public String signOut(String jwt) {

        try {
            String[] authElements = jwt.split(" ");

            String isOK = jwtAuthenticationProvider.deleteToken(authElements[1]);

            return isOK;

        } catch (Exception e) {
            System.out.println(
                    "error al cerrar sesion" + e.getMessage());
            return "No se pudo cerrar la sesión";
        }

    }

}
