package com.example.config;

import com.example.security.CustomJwtAuthenticationConverter;
import com.example.security.JwtAuthenticationEntryPoint;
import com.example.security.KeycloakAuthorityConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
    private static final String SWAGGER_ENDPOINT = "/swagger-ui/**";
    private static final String SWAGGER_API_DOCS_ENDPOINT = "/v3/api-docs/**";
    public static final String LOGIN_ENDPOINT = "/api/auth/login";
    public static final String CITY_ENDPOINT = "/api/cities/**";

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, LOGIN_ENDPOINT).permitAll()
                .antMatchers(CITY_ENDPOINT).permitAll()
                .antMatchers(SWAGGER_ENDPOINT, SWAGGER_API_DOCS_ENDPOINT).permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                .and().authenticationEntryPoint(jwtAuthenticationEntryPoint);
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        CustomJwtAuthenticationConverter jwtAuthenticationConverter = new CustomJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakAuthorityConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(
                Arrays.asList("http://localhost:3000", "http://localhost") //only for demo purpose
        );
        configuration.setAllowedMethods(Arrays.asList("GET", "OPTIONS", "POST", "PUT"));
        configuration.setAllowedHeaders(Arrays.asList(
                "X-Requested-With",
                "Origin",
                "Content-Type",
                "Accept",
                "Authorization",
                "Allow"
        ));
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "x-xsrf-token",
                "Origin",
                "Accept",
                "X-Requested-With",
                "Content-Type",
                "Access-Control-Allow-Methods",
                "Access-Control-Allow-Headers",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));
        configuration.applyPermitDefaultValues();
        configuration.setMaxAge(3600L);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}