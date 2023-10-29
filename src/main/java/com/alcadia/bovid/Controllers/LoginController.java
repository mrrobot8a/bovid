package com.alcadia.bovid.Controllers;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alcadia.bovid.Service.UserCase.IAuthenticationService;
import com.alcadia.bovid.Service.UserCase.IUserService;
import com.alcadia.bovid.Service.Util.PasswordRequestUtil;
import com.alcadia.bovid.Event.Listener.RegistrationCompleteEventListener;
import com.alcadia.bovid.Models.Dto.RegistrationRequest;
import com.alcadia.bovid.Models.Dto.RegistrationResponse;
import com.alcadia.bovid.Models.Entity.User;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.mail.MessagingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {

    private final IUserService userService;
    private final IAuthenticationService AuthUseService;

    private final RegistrationCompleteEventListener eventListener;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest userRequest)
            throws MessagingException, UnsupportedEncodingException {

        Map<String, Object> response = new HashMap<>();

        try {

            RegistrationResponse userResponse = userService.registerUser(userRequest);
            // poner el envio de correo en el service del usuario
            // if (!eventListener.sendCredencial(userRequest)) {
            //     userService.deleteUser(userRequest);
            //     response.put("error", "error desde el envio de correo");
            //     response.put("mensaje", "ERROR AL RELIZAR EL REGISTRO");
            //     return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

            // }
            response.put("usuario", userResponse);
            response.put("mensaje", "SUCCESS TO REGISTER USER");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (MessagingException e) {
            // Maneja la excepción aquí si ocurre algún error, como un error en la base de
            // datos
            response.put("error", "Error al registrar usuario: " + e.getMessage());
            response.put("mensaje", "ERROR AL RELIZAR EL REGISTRO");
            response.put("trac", e.getStackTrace() + "\n" + e.getCause());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }
    
    
    @PostMapping(path = "/sign-out")
    public ResponseEntity<String> signOut(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String jwt) {
        return ResponseEntity.ok(AuthUseService.signOut(jwt));
    }

    @PostMapping(path = "/sign-in")
    public ResponseEntity<String> signIn(@RequestBody RegistrationRequest authCustomerDto,
            final HttpServletRequest servletRequest) {
        System.out.println("signIn====================auth======="+authCustomerDto);
        return ResponseEntity.ok(AuthUseService.signIn(authCustomerDto, servletRequest));
    }

    /**
     * @param passwordRequestUtil
     * @param servletRequest
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @PostMapping("/password-reset-request")
    public String resetPasswordRequest(@RequestBody PasswordRequestUtil passwordRequestUtil,
            final HttpServletRequest servletRequest)
            throws MessagingException, UnsupportedEncodingException {
         System.out.println("passwordRequestUtil====================auth======="+passwordRequestUtil);
        User user = userService.findByEmail(passwordRequestUtil.getEmail());

        String passwordResetUrl = "";

        if (user != null) {

            String passwordResetToken = UUID.randomUUID().toString();

            passwordResetUrl = passwordResetEmailLink(user, applicationUrl(servletRequest), passwordResetToken);

            userService.createPasswordResetTokenForUser(user, passwordResetToken);

            return passwordResetUrl;

        }

        return "iInvalid token password reset token";

    }

    /**
     * Maneja la solicitud POST para restablecer la contraseña de un usuario.
     *
     * @param passwordRequestUtil Un objeto que contiene los datos necesarios para
     *                            restablecer la contraseña.
     * @param token               El token de validación necesario para verificar la
     *                            solicitud de restablecimiento de contraseña.
     * @return Un mensaje indicando si la contraseña se restableció con éxito o si
     *         el token es inválido.
     */
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordRequestUtil passwordRequestUtil,
            @RequestParam("token") String token) throws MessagingException, UnsupportedEncodingException {

        // verificamos la authentication del token para verificar que el token sea
        // generado por la application
        String tokenVerificationResult = userService.validatePasswordResetToken(token);

        if (!tokenVerificationResult.equalsIgnoreCase("valid")) {
            return "Invalid token password reset token";
        }

        // Busca al usuario asociado con el token.
        Optional<User> theUser = Optional.ofNullable(userService.findUserByPasswordToken(token));

        if (theUser.isPresent()) {
            // Cambia la contraseña del usuario con la nueva contraseña proporcionada.
            userService.changePassword(theUser.get(), passwordRequestUtil.getNewPassword());
            return "Password has been reset successfully";
        }
        return "Invalid password reset token";
    }

   
    /**
     * @param user
     * @param applicationUrl
     * @param passwordToken
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     *                                      Metodo para enviar el link de
     *                                      restablecimiento de contraseña por via
     *                                      correo
     */
    private String passwordResetEmailLink(User user, String applicationUrl, String passwordToken)
            throws MessagingException, UnsupportedEncodingException {

        String url = applicationUrl + "/auth/reset-password?token=" + passwordToken;

        eventListener.sendPasswordResetVerificationEmail(url, user);

        log.info("Click the link to reset your password :  {}", url);

        return url;
    }

    /**
     * @param request
     * @return
     *         obtener la url de alplicacition
     */
    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":"
                + request.getServerPort() + request.getContextPath();
    }

}
