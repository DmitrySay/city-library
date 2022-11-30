package com.example.service;

import com.example.dto.AuthRequestDto;
import com.example.dto.PasswordResetConfirmationRequest;
import com.example.dto.UserDto;
import com.example.exception.PasswordResetException;
import com.example.exception.RegistrationException;
import com.example.exception.RestException;
import com.example.mapper.UserMapper;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.UserStatus;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.model.RoleNames.ROLE_USER;
import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailNotificationService emailNotificationService;


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RestException(format("User with email [%s] does not exist", email)));
    }

    public boolean userExistByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.isPresent();
    }

    @Transactional
    public void createNewRegisterUser(AuthRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();
        if (userExistByEmail(email)) {
            throw new RestException(format("User with email [%s] already exist", email));
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserStatus(UserStatus.PENDING_VERIFICATION);
        Role role = roleRepository.findByName(ROLE_USER.name());
        user.getRoles().add(role);
        userRepository.save(user);

        String token = jwtTokenProvider.createToken(email, user.getRoles());
        emailNotificationService.sendEmailVerification(email, token);
    }

    public UserDto confirmNewUserRegistration(String token) {
        try {
            if (jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getUsername(token);
                Optional<User> userOptional = userRepository.findByEmail(email);
                if (userOptional.isPresent() && UserStatus.PENDING_VERIFICATION.equals(userOptional.get().getUserStatus())) {
                    User user = userOptional.get();
                    user.setUserStatus(UserStatus.ACTIVE);
                    return userMapper.toDto(userRepository.save(user));
                } else {
                    String errorMsg = format("Email verification failed. User with email [%s] does not exist or user does not have status pending verification ", email);
                    throw new RegistrationException(errorMsg);
                }
            }
            throw new RegistrationException("Email verification failed. JWT token is expired.");
        } catch (Exception exception) {
            throw new RegistrationException(exception.getMessage());
        }
    }

    public void processPasswordResetRequest(String email) {
        User user = getUserByEmail(email);
        String token = jwtTokenProvider.createToken(email, user.getRoles());
        emailNotificationService.sendPasswordResetEmail(email, token);
    }

    @Transactional
    public UserDto confirmPasswordReset(PasswordResetConfirmationRequest passwordResetConfirmationRequest) {
        String newPassword = passwordResetConfirmationRequest.getPassword();
        String token = passwordResetConfirmationRequest.getToken();
        try {
            if (jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getUsername(token);
                return userRepository
                        .findByEmail(email)
                        .map(user -> {
                            user.setPassword(passwordEncoder.encode(newPassword));
                            userRepository.save(user);
                            return user;
                        })
                        .map(userMapper::toDto)
                        .orElseThrow(() -> new PasswordResetException(format("Password reset confirmation failed. User with email [%s] does not exist", email)));
            } else {
                throw new PasswordResetException("Password reset confirmation failed. JWT token is expired.");
            }
        } catch (Exception exception) {
            throw new PasswordResetException(exception.getMessage());
        }
    }

}

