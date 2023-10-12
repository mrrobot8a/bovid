package com.alcadia.bovid.Service.UserCase;

import java.util.Optional;

import com.alcadia.bovid.Models.Entity.PasswordResetToken;
import com.alcadia.bovid.Models.Entity.User;

public interface IPasswordResetTokenService {



    /**
     * METEDO PARA CREAR EL TOKEN DE RESTABLECIMIENTO DE CONTRASEÑA
     * @param user
     * @param passwordToken
     */
    public void createPasswordResetTokenForUser(User user, String passwordToken);

    public String validatePasswordResetToken(String passwordResetToken) ;

    public Optional<User> findUserByPasswordToken(String passwordResetToken);

    public PasswordResetToken findPasswordResetToken(String token);
    
    
}
