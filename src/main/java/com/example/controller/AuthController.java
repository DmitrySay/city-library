package com.example.controller;

import com.example.dto.AuthRequestDto;
import com.example.dto.AuthResponseDto;
import com.example.dto.UserDto;
import com.example.mapper.UserMapper;
import com.example.security.JwtTokenProvider;
import com.example.security.JwtUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Operation(summary = "User login endpoint", description = "User login endpoint")
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDto.class)))
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody AuthRequestDto requestDto) {
        try {
            String email = requestDto.getEmail();
            String password = requestDto.getPassword();

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));

            JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
            String token = jwtTokenProvider.createToken(email, jwtUser.getRoles());
            return new AuthResponseDto(email, token);

        } catch (Exception ex) {
            log.warn("Invalid email or password." + ex.getMessage());
            throw new BadCredentialsException("Invalid email or password.");
        }
    }

    @GetMapping("/me")
    public UserDto me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return userMapper.toDto(jwtUser);
    }
}
