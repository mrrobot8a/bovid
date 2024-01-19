package com.alcadia.bovid.Service.UserCase;

import java.io.UnsupportedEncodingException;

import org.springframework.data.domain.Page;

import com.alcadia.bovid.Models.Dto.RegistrationRequest;
import com.alcadia.bovid.Models.Dto.RegistrationResponse;
import com.alcadia.bovid.Models.Dto.UserDto;
import com.alcadia.bovid.Models.Entity.User;
import com.alcadia.bovid.Service.Util.PasswordRequestUtil;

import jakarta.mail.MessagingException;

public interface IUserService {

    Page<UserDto> getAllUsers(int page, int size);

    RegistrationResponse registerUser(RegistrationRequest request)
            throws UnsupportedEncodingException, MessagingException;

    UserDto updateUser(UserDto userDto);

    void deleteUser(UserDto userDto);

    User findByEmail(String email);

    void saveUserVerificationToken(User theUser, String verificationToken);

    String validateToken(String theToken);

    // VerificationToken generateNewVerificationToken(String oldToken);
    String changePassword(String token, PasswordRequestUtil passwordResetToken);

    String validatePasswordResetToken(String token);

    User findUserByPasswordToken(String token);

    void createPasswordResetTokenForUser(User user, String passwordResetToken);

    boolean oldPasswordIsValid(User user, String oldPassword);

    boolean deleteUser(RegistrationRequest userRequest);
}
