package com.alcadia.bovid.Service;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.SysexMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alcadia.bovid.Exception.CustomerNotExistException;
import com.alcadia.bovid.Exception.InvalidVerificationTokenException;
import com.alcadia.bovid.Exception.PasswordIncorrectException;
import com.alcadia.bovid.Models.Dto.RegistrationRequest;
import com.alcadia.bovid.Models.Dto.UserDto;
import com.alcadia.bovid.Models.Entity.User;
import com.alcadia.bovid.Repository.Dao.IUserRepository;
import com.alcadia.bovid.Security.JwtAuthenticationProvider;
import com.alcadia.bovid.Service.Mappers.UserMapper;
import com.alcadia.bovid.Service.UserCase.IAuthenticationService;
import com.alcadia.bovid.Service.UserCase.IHistorialAuditoriaService;
import com.alcadia.bovid.Service.Util.KeyGeneratorUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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

    @Transactional
    @Override
    public Map<String, Object> signIn(RegistrationRequest userRegistrationRequest, HttpServletRequest request)
            throws CustomerNotExistException, JsonProcessingException {

        try {

            Map<String, Object> response = new HashMap<>();
            // // Obtiene el método HTTP y la URL
            // String httpMethod = request.getMethod();
            // String url = request.getRequestURL().toString();
            // String ipclient = getInfoClientComponet.getClientIp(request);
            // String actionUser = "signIn";

            // buscamos el usuario por email
            User userEntity = userRepository.findByEmail(userRegistrationRequest.getEmail()).orElse(null);

            if (userEntity == null
                    || !verificarPassword(userRegistrationRequest.getPassword(), userEntity.getPassword())) {
                throw new CustomerNotExistException("Usuario o contraseña incorrecta");
            }

            if (!userEntity.isAccountNonLocked()) {
                response.put("success", false);
                response.put("status", HttpStatus.FORBIDDEN.value());
                response.put("error", "Usuario bloqueado o deshabilitado");
                return response;
            }

            System.out.println(
                    "==========================roles entity authentication============" + userEntity.getAuthorities());

            UserDto userDto = UserMapper.INSTANCE.apply(userEntity);

            System.out.println("============================= USER AUTENTICAITON" + userDto);

            String token = jwtAuthenticationProvider.createToken(userDto);

            // historialAuditoriaService.registerHistorial(actionUser, ipclient, httpMethod,
            // url,
            // userRegistrationRequest.email());

            response.put("user", userDto);
            response.put("token", token);
            response.put("success", true);
            response.put("message", "Inicio de sesion exitoso");

            return response;
        } catch (CustomerNotExistException e) {
            // Manejar específicamente la excepción CustomerNotExistException
            System.out.println("Error al iniciar sesión: " + e.getMessage());
            throw e;
        } catch (JsonProcessingException e) {
            throw e;
        } catch (Exception e) {
            // Manejar otras excepciones
            System.out.println("Error No Manejado: " + e.getMessage());
            throw e;
        }
    }

    private boolean verificarPassword(String password, String passwordEncriptado) {

        String passswordUserEncript = KeyGeneratorUtil.decryptString(passwordEncriptado);
        System.out.println("password: " + passswordUserEncript);
        System.out.println("passwordEncriptado: " + passwordEncriptado);
        return passswordUserEncript.equals(password);
    }

    @Override
    public String signOut(String jwt) {
        try {
            String[] authElements = jwt.split(" ");
            String isOK = jwtAuthenticationProvider.deleteToken(authElements[1]);
            return isOK;
        } catch (InvalidVerificationTokenException e) {
            // Manejar específicamente la excepción InvalidVerificationTokenException
            System.out.println("Error al cerrar sesión: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Manejar otras excepciones
            System.out.println("Error al cerrar sesión: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Map<String, Object> checkStatusUser(String jwt) throws JsonProcessingException {

        Map<String, Object> response = new HashMap<>();

        try {
            String[] token = jwt.split(" ");
            boolean tokenValid = jwtAuthenticationProvider.validateToken(token[1]);
            String newToken = jwtAuthenticationProvider.refresToken(token[1]);
            if (tokenValid && !newToken.equals("")) {

                response.put("user", jwtAuthenticationProvider.getUserDto(token[1]));
                response.put("token", newToken);
                response.put("success", true);
                response.put("message", "Refresh token success");

            }

            return response;
        } catch (InvalidVerificationTokenException e) {
            // Manejar específicamente la excepción InvalidVerificationTokenException
            System.out.println("Error al cerrar sesión: " + e.getMessage());
            throw e;
        } catch (JsonProcessingException e) {
            throw e;
        } catch (Exception e) {
            // Manejar otras excepciones
            System.out.println("Error No Manejado: " + e.getMessage());
            throw e;
        }
    }

}
