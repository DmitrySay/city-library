package com.example.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class KeycloakAuthClient {
    private static final String TOKEN_PATH = "/token";
    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_ID = "client_id";
    public static final String SCOPE = "scope";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    @Value("${app.keycloak.auth-url}")
    private String authUrl;

    @Value("${app.keycloak.client-id}")
    private String clientId;

    private final RestTemplate restTemplate;

    public KeycloakAuthClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public KeycloakAuthResponse authenticate(String email, String password) {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add(CLIENT_ID, clientId);
        paramMap.add(GRANT_TYPE, "password");
        paramMap.add(SCOPE, "openid");
        paramMap.add(USERNAME, email);
        paramMap.add(PASSWORD, password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String url = authUrl + TOKEN_PATH;

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramMap, headers);
        log.info("Try to authenticate");

        ResponseEntity<KeycloakAuthResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, KeycloakAuthResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Failed to authenticate");
            throw new RuntimeException("Failed to authenticate");
        }
        log.info("Authentication success");

        return response.getBody();
    }
}