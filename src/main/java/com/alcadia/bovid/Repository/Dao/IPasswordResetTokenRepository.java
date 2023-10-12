package com.alcadia.bovid.Repository.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alcadia.bovid.Models.Entity.PasswordResetToken;



/**
 * interface que extiende de jpa para guardar el token de restablecer contraseña en la base de datos
 * @author jhon peralta
 */

public interface IPasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String passwordResetToken);
}