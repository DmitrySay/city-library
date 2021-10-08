package com.example.controller;

import com.example.dto.AuthRequestDto;
import com.example.dto.AuthResponseDto;
import com.example.dto.UserDto;
import com.example.exception.NotFoundException;
import com.example.mapper.UserMapper;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.JwtUser;
import com.example.security.KeycloakAuthClient;
import com.example.security.KeycloakAuthResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
@Tag(name = "Security")
public class AuthController {
    private final KeycloakAuthClient keycloakAuthClient;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody AuthRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();
        KeycloakAuthResponse response = keycloakAuthClient.authenticate(email, password);
        return new AuthResponseDto(email, response.getAccessToken());
    }

    @GetMapping("/me")
    public UserDto me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        User user = userRepository
                .findById(jwtUser.getUserId())
                .orElseThrow(() -> new NotFoundException("User is not found."));

        return userMapper.toDto(user);
    }
}
