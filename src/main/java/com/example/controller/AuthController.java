package com.example.controller;

import com.example.dto.AuthRequestDto;
import com.example.dto.UserDto;
import com.example.mapper.UserMapper;
import com.example.security.JwtUser;
import com.example.service.UserService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
@Tag(name = "Security")
public class AuthController {

    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping("/me")
    public UserDto me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return userMapper.toDto(jwtUser);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration/user")
    public void register(@RequestBody AuthRequestDto requestDto) {
        userService.createNewRegisterUser(requestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/email-verification")
    public UserDto confirmRegistration(@RequestParam String token) {
        return userService.confirmNewUserRegistration(token);
    }

}
