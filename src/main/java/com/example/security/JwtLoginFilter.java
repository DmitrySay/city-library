package com.example.security;

import com.example.dto.AuthRequestDto;
import com.example.dto.AuthResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtLoginFilter(String loginUrl, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        setFilterProcessesUrl(loginUrl);
        setAuthenticationManager(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AuthRequestDto authRequestDto;
        try {
            authRequestDto = new ObjectMapper().readValue(request.getInputStream(), AuthRequestDto.class);
        } catch (Exception ex) {
            log.error("Failed to read value from authentication request ", ex);
            throw new RuntimeException(ex);
        }
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword());
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String email = authResult.getName();
        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        String token = jwtTokenProvider.createToken(email, jwtUser.getRoles());
        ObjectMapper objectMapper = new ObjectMapper();
        String authResponseDto = objectMapper.writeValueAsString(new AuthResponseDto(email, token));
        printMessage(response, authResponseDto);
    }

    private void printMessage(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().println(msg);
    }

}
