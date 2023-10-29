package com.alcadia.bovid.Service;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alcadia.bovid.Models.Entity.PasswordResetToken;
import com.alcadia.bovid.Models.Entity.User;
import com.alcadia.bovid.Repository.Dao.IPasswordResetTokenRepository;
import com.alcadia.bovid.Service.UserCase.IPasswordResetTokenService;

import lombok.RequiredArgsConstructor;

/**
 * @author Sampson Alfred
 */
@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImple implements IPasswordResetTokenService {

    private final IPasswordResetTokenRepository passwordResetTokenRepository;

    public void createPasswordResetTokenForUser(User user, String passwordToken) {

        PasswordResetToken passwordRestToken = new PasswordResetToken(passwordToken, user);

        passwordResetTokenRepository.save(passwordRestToken);
    }

    public String validatePasswordResetToken(String passwordResetToken) {

        PasswordResetToken passwordToken = passwordResetTokenRepository.findByToken(passwordResetToken);
        System.out.println("===============================" + passwordResetToken);
        if (passwordToken == null) {
            return "Invalid verification token";
        }

        // User user = passwordToken.getUser();

        Calendar calendar = Calendar.getInstance();

        if ((passwordToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {

            return "Link already expired, resend link";

        }

        return "valid";
    }

    /**
     * @param passworResetToken
     * @apiNote
     * 
     */

    public Optional<User> findUserByPasswordToken(String passwordResetToken) {

        return Optional.ofNullable(passwordResetTokenRepository.findByToken(passwordResetToken).getUser());
    }

    /**
     * @param token
     * @apiNote
     */

    public PasswordResetToken findPasswordResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

}
