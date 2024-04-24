package com.alcadia.bovid.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.crypto.KeyGenerator;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.alcadia.bovid.Event.Listener.RegistrationCompleteEventListener;
import com.alcadia.bovid.Exception.CustomerNotExistException;
import com.alcadia.bovid.Exception.RoleNoExistsException;
import com.alcadia.bovid.Exception.UserAlreadyExistsException;
import com.alcadia.bovid.Models.Dto.RegistrationRequest;
import com.alcadia.bovid.Models.Dto.RegistrationResponse;
import com.alcadia.bovid.Models.Dto.RoleDto;
import com.alcadia.bovid.Models.Dto.UserDto;
import com.alcadia.bovid.Models.Entity.Funcionario;
import com.alcadia.bovid.Models.Entity.Role;
import com.alcadia.bovid.Models.Entity.User;
import com.alcadia.bovid.Repository.Dao.IFuncionarioRepository;
import com.alcadia.bovid.Repository.Dao.IRoleRepository;
import com.alcadia.bovid.Repository.Dao.IUserRepository;
import com.alcadia.bovid.Service.Mappers.UserToRegistrationResponse;
import com.alcadia.bovid.Service.Mappers.UserMapper;
import com.alcadia.bovid.Service.UserCase.IUserService;
import com.alcadia.bovid.Service.Util.KeyGeneratorUtil;
import com.alcadia.bovid.Service.Util.PasswordRequestUtil;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;

    private final IRoleRepository roleRepository;

    private final IFuncionarioRepository funcionarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final PasswordResetTokenServiceImple passwordResetTokenService;

    private final RegistrationCompleteEventListener eventListener;

    @Transactional
    @Override
    public void deleteUser(UserDto userDto) {
        try {
            User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(
                    () -> new RoleNoExistsException("Usuario no encontrado"));

            // // Eliminar registros relacionados en la tabla de historial
            // historialAuditoriaRepository.deleteByUsersId(user.getId());

            user.deleteUsersFromRoles();

            // // Iterar sobre los roles del usuario
            // for (GrantedAuthority authority : user.getAuthorities()) {
            // if (authority instanceof Role) {
            // Role role = (Role) authority;

            // // Eliminar la relación entre el usuario y el rol
            // role.getUsers().remove(user);
            // }
            // }

            // // Limpiar la colección de roles del usuario
            // user.getAuthorities().clear();

            // Eliminar el usuario
            userRepository.deleteById(user.getId());
        } catch (RoleNoExistsException e) {
            // Manejar excepción personalizada para usuario no encontrado
            // Puedes devolver un mensaje de error personalizado al front-end
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public UserDto updateUser(UserDto userDto) {

        try {

            User user = userRepository.findById(userDto.getId()).orElseThrow(
                    () -> new CustomerNotExistException(
                            "Usuario no encotrado"));
                            
            String encodedPassword = KeyGeneratorUtil.encryptString(userDto.getPassword());

            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setPassword(encodedPassword);
            user.setEmail(userDto.getEmail());
            user.setAuthorities(getRolesFromRoleNames(userDto.getRoles()));
            user.getFuncionario().setNumberPhone(userDto.getNumberPhone());
            user.getFuncionario().setPosition(userDto.getPosition());
            user.getFuncionario().setFirtsName(userDto.getFirstName());
            user.getFuncionario().setLastName(userDto.getLastName());
            user.setEnabled(userDto.isEnabled());

            user = userRepository.save(user);

            return UserMapper.INSTANCE.apply(user);

        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            System.out.println("=============" + response);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());

        }

    }

    /**
     * Convierte una lista de objetos RoleDto en un conjunto de objetos Role
     * buscados
     * por su nombre en la base de datos.
     *
     * @param roleDtos La lista de objetos RoleDto que se deben buscar en la base de
     *                 datos.
     * @return Un conjunto de objetos Role correspondientes a los nombres
     *         proporcionados.
     * @throws RoleNoExistsException Si alguno de los roles especificados no existe
     *                               en la base de datos.
     */
    private Set<Role> getRolesFromRoleNames(List<RoleDto> roleDtos) {
        // Inicializa un conjunto para almacenar los roles encontrados.
        Set<Role> roles = new HashSet<>();

        // Itera a través de la lista de RoleDto.
        for (RoleDto roleDto : roleDtos) {
            // Busca un objeto Role en la base de datos por su nombre.
            Role role = roleRepository.findByAuthority(roleDto.getAuthority())
                    .orElseThrow(() -> new RoleNoExistsException(
                            "El rol asignado al usuario no existe en la base de datos"));

            // Agrega el objeto Role al conjunto de roles encontrados.
            roles.add(role);
        }

        // Retorna el conjunto de roles correspondientes a los nombres proporcionados.
        return roles;
    }

    @Override
    public Page<UserDto> getAllUsers(int page, int size) {

        try {

            Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "id"));

            Page<User> users = userRepository.findAll(pageable);
            System.out.println("users=============GetALLUSER=======" + users.getContent().toString());

            log.info(users.getContent().toString());

            if (users.isEmpty()) {
                throw new CustomerNotExistException("No hay usuarios registrados");
            }

            return UserMapper.INSTANCE.ListUserToListUserDto(users);

        } catch (DataAccessException e) {

            // Imprimir detalles de la excepción en una sola línea
            System.err.println("Mensaje de la excepción: " + e.getMessage() +
                    ", Causa raíz (si existe): " + e.getCause() +
                    ", Nombre de la clase de la excepción: " + e.getClass().getName());

        }

        return null;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public RegistrationResponse registerUser(RegistrationRequest registerUserRequest)
            throws UnsupportedEncodingException, MessagingException {

        try {

            if (userRepository.existsByEmail(registerUserRequest.getEmail())) {

                throw new UserAlreadyExistsException(
                        "El correo electrónico '" + registerUserRequest.getEmail() + "' ya está en uso.");

            }

            Set<Role> roles = getRolesFromRoleNames(registerUserRequest.getRoles());

            // Se envia el correo al usuario con las credenciales
            if (!eventListener.sendCredencial(registerUserRequest)) {
                log.info("mail no enviado");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo enviar el correo");
            }

            String encodedPassword = KeyGeneratorUtil.encryptString(registerUserRequest.getPassword());

            // Set<Role> authorities = new HashSet<>();

            User applicationUser = new User();

            Funcionario funcionarioEntity = new Funcionario();

            // valida si el codeAdmin es vacio o no
            // userRoles.setAuthority( registerUserRequest.codeAdmin() == nulls ?
            // Roles.FUNCIONARIO:Roles.ADMIN);

            // authorities.add(role);

            applicationUser.setFirstName(registerUserRequest.getFirstName());
            applicationUser.setLastName(registerUserRequest.getLastName());
            applicationUser.setPassword(encodedPassword);
            // applicationUser.setAuthorities(authorities);
            applicationUser.setAuthorities(roles);
            applicationUser.setEmail(registerUserRequest.getEmail());
            applicationUser.setEnabled(registerUserRequest.getEnabled());
            funcionarioEntity.setFirtsName(registerUserRequest.getFirstName());
            funcionarioEntity.setLastName(registerUserRequest.getLastName());
            funcionarioEntity.setNumberPhone(registerUserRequest.getNumberPhone());
            funcionarioEntity.setPosition(registerUserRequest.getPosition());

            applicationUser.setFuncionario(funcionarioEntity);
            funcionarioEntity.setUser(applicationUser);

            applicationUser = userRepository.save(applicationUser);

            return UserToRegistrationResponse.INSTANCE.apply(applicationUser);

        } catch (DataAccessException e) {

            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            System.out.println("=============" + response);
        }

        return null;
    }

    private boolean verificarPassword(String password, String passwordEncriptado) {

        String passswordUserEncript = KeyGeneratorUtil.decryptString(passwordEncriptado);
        System.out.println("password: " + passswordUserEncript);
        System.out.println("passwordEncriptado: " + passwordEncriptado);
        return passswordUserEncript.equals(password);
    }

    @Override
    @Transactional
    public User findByEmail(String email) {

        try {

            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new CustomerNotExistException(
                            "El usuario ingresado no existe." + "trace: class UserServiceImpl, method findByEmail"));

            return user;
        } catch (DataAccessException ex) {
            return null;
        }

    }

    @Override
    public void saveUserVerificationToken(User theUser, String verificationToken) {

    }

    @Override
    public String validateToken(String theToken) {

        throw new UnsupportedOperationException("Unimplemented method 'validateToken'");
    }

    @Override
    public String changePassword(String token, PasswordRequestUtil passwordResetToken) {
        return Optional.ofNullable(findUserByPasswordToken(token))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(passwordResetToken.getNewPassword()));
                    try {
                        userRepository.save(user);
                        passwordResetTokenService.deleteToken(token);
                        return "Se realizo con exito el cambio de  password";
                    } catch (Exception e) {
                        // Manejar la excepción de guardado, loggear o notificar según sea necesario.
                        return "Hubo un error al realizar la operacion";
                    }
                })
                .orElse("Hubo un error al realizar la operacion");
    }

    @Override
    public String validatePasswordResetToken(String token) {
        return passwordResetTokenService.validatePasswordResetToken(token);
    }

    @Override
    public User findUserByPasswordToken(String token) {

        return passwordResetTokenService.findUserByPasswordToken(token).get();
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordResetToken) {

        passwordResetTokenService.createPasswordResetTokenForUser(user, passwordResetToken);
    }

    @Override
    public boolean oldPasswordIsValid(User user, String oldPassword) {

        throw new UnsupportedOperationException("Unimplemented method 'oldPasswordIsValid'");
    }

    @Override
    public boolean deleteUser(RegistrationRequest userRequest) {

        try {
            User user = userRepository.findByEmail(userRequest.getEmail()).orElseThrow(
                    () -> new RoleNoExistsException(
                            "Usuario no encotrado"));

            userRepository.deleteById(user.getId());

            return true;

        } catch (Exception e) {

        }

        return false;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
