package com.alcadia.bovid.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alcadia.bovid.Component.InfoRequestClientComponet;
import com.alcadia.bovid.Exception.CustomerNotExistException;
import com.alcadia.bovid.Exception.PasswordIncorrectException;
import com.alcadia.bovid.Models.Dto.RegistrationRequest;
import com.alcadia.bovid.Models.Dto.UserDto;
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
    public Map<String, Object> signIn(RegistrationRequest userRegistrationRequest, HttpServletRequest request)
            throws CustomerNotExistException {

        Map<String, Object> response = new HashMap<>();
        // Obtiene el método HTTP y la URL
        String httpMethod = request.getMethod();
        String url = request.getRequestURL().toString();
        String ipclient = getInfoClientComponet.getClientIp(request);
        String actionUser = "signIn";

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
        UserDto userDto = UserMapper.INSTANCE.apply(userEntity);

        System.out.println("============================= USER AUTENTICAITON" + userDto);

        String token = jwtAuthenticationProvider.createToken(userDto);

        historialAuditoriaService.registerHistorial(actionUser, ipclient, httpMethod, url,
                userRegistrationRequest.email());
                
        response.put("user", userDto);
        response.put("token", token);
        response.put("success", true);
        response.put("message", "Inicio de sesión exitoso");
        


        return response;
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
