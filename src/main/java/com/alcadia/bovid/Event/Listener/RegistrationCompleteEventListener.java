package com.alcadia.bovid.Event.Listener;

import java.io.UnsupportedEncodingException;

import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.alcadia.bovid.Event.RegistrationCompleteEvent;

import com.alcadia.bovid.Exception.handleEmailSendingFailure;
import com.alcadia.bovid.Models.Dto.RegistrationRequest;

import com.alcadia.bovid.Models.Entity.User;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;


/**
 * @author Sampson Alfred
 */

@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final JavaMailSender mailSender;

    private User theUser;

    @Override
    public void onApplicationEvent(@SuppressWarnings("null") RegistrationCompleteEvent event) {
        // // 1. Get the newly registered user
        // theUser = event.getUser();
        // // 2. Create a verification token for the user
        // String verificationToken = UUID.randomUUID().toString();
        // // 3. Save the verification token for the user
        // userService.saveUserVerificationToken(theUser, verificationToken);
        // // 4 Build the verification url to be sent to the user
        // String url = event.getApplicationUrl() + "/register/verifyEmail?token=" +
        // verificationToken;
        // // 5. Send the email.
        // try {
        // sendVerificationEmail(url);
        // } catch (MessagingException | UnsupportedEncodingException e) {
        // throw new RuntimeException(e);
        // }
        // log.info("Click the link to verify your registration : {}", url);
    }

    @SuppressWarnings("null")
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, " + theUser.getFirstName() +""+ theUser.getLastName() + ", </p>" +
                "<p>Thank you for registering with us," + "" +
                "Please, follow the link below to complete your registration.</p>" +
                "<a href=\"" + url + "\">Verify your email to activate your account</a>" +
                "<p> Thank you <br> Users Registration Portal Service";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("marcaganado@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sendPasswordResetVerificationEmail(String url, User user)
            throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset Request Verification";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, " + user.getFirstName() +""+ user.getLastName()+ ", </p>" +
                "<p><b>You recently requested to reset your password,</b>" + "" +
                "Please, follow the link below to complete the action.</p>" +
                "<a href=\"" + url + "\">Reset password</a>" +
                "<p> Users Registration Portal Service";

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("marcaganado@gmail.com", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        System.out.println("=====================================" + "envio de email");
        mailSender.send(message);

    }

    public void sendCrendecialtoUserEmail(RegistrationRequest user)
            throws MessagingException, UnsupportedEncodingException {

        String subject = "envio de credenciales de acceso";
        String senderName = "APLICACION DE GANADERIA BOVID";
        String mailContent = "<p> Hi, " + user.getFirstName() + " " + user.getLastName() + ", </p>" +
                "<p><b>You recently requested to reset your password,</b>" + "" +
                "Please, follow the link below to complete the action.</p>" +
                "<p> Your email is : " + user.getEmail() + ". </p>" +
                "<p> Your password is : " + user.getPassword() + ". </p>" +
                "<p> Alcaldia de valledupar " + "</p>" +
                "<p> Users Registration Portal Service ";

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("marcaganado@gmail.com", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        System.out.println("=====================================" + "envio de email");
        mailSender.send(message);

    }

    public boolean sendCredencial(RegistrationRequest userRequest)
            throws UnsupportedEncodingException, MessagingException {

        int maxRetries = 3; // Número máximo de reintentos
        int retryDelayMillis = 1000; // Intervalo de tiempo entre reintentos en milisegundos (1 segundo en este caso)

        for (int retryCount = 0; retryCount < maxRetries; retryCount++) {

            try {
                // Llamar al método que puede lanzar una excepción
                sendCrendecialtoUserEmail(userRequest);

                // Si la operación tiene éxito, sal del bucle
                // break;

                return true;
            } catch (MailSendException r) {
                System.out.println(r.getMessage());
                // Captura y maneja la excepción que indica un error al enviar el correo
                if (retryCount < maxRetries - 1) {
                    // Espera antes de realizar el siguiente intento
                    try {
                        Thread.sleep(retryDelayMillis);
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt(); // Preserve the interrupt status
                    }
                } else {
                    // Si se supera el límite de reintentos, maneja el error de manera adecuada
                    // handleEmailSendingFailure(e);
                    throw new handleEmailSendingFailure(r.getMessage()+"-No se pudo enviar el correo");

                }
            }
        }
        return false;
    }

}
