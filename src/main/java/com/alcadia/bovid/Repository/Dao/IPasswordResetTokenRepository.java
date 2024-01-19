package com.alcadia.bovid.Repository.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alcadia.bovid.Models.Entity.PasswordResetToken;
import com.alcadia.bovid.Models.Entity.User;



/**
 * interface que extiende de jpa para guardar el token de restablecer contraseña en la base de datos
 * @author jhon peralta
 */

public interface IPasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String passwordResetToken);

    // Ejemplo: Buscar un usuario por token
    Optional<PasswordResetToken> findByUser(User user);
}