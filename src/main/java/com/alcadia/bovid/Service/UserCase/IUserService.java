package com.alcadia.bovid.Service.UserCase;


import java.util.Optional;

import org.springframework.data.domain.Page;

import com.alcadia.bovid.Models.Dto.RegistrationRequest;
import com.alcadia.bovid.Models.Dto.RegistrationResponse;
import com.alcadia.bovid.Models.Dto.UserDto;
import com.alcadia.bovid.Models.Entity.User;

public interface IUserService {

    Page<UserDto> getAllUsers(int page, int size);

    RegistrationResponse registerUser(RegistrationRequest request);

    UserDto updateUser(UserDto userDto);

    void  deleteUser(UserDto userDto);

    Optional<User> findByEmail(String email);

    void saveUserVerificationToken(User theUser, String verificationToken);

    String validateToken(String theToken);

    // VerificationToken generateNewVerificationToken(String oldToken);
    void changePassword(User theUser, String newPassword);

    String validatePasswordResetToken(String token);

    User findUserByPasswordToken(String token);

    void createPasswordResetTokenForUser(User user, String passwordResetToken);

    boolean oldPasswordIsValid(User user, String oldPassword);

    boolean deleteUser(RegistrationRequest userRequest);
}
