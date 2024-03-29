package com.example.config;


import com.example.security.JwtAuthenticationEntryPoint;
import com.example.security.JwtAuthenticationFilter;
import com.example.security.JwtLoginFilter;
import com.example.security.JwtTokenProvider;
import com.example.security.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    private static final String SWAGGER_ENDPOINT = "/swagger-ui/**";
    private static final String SWAGGER_API_DOCS_ENDPOINT = "/v3/api-docs/**";
    public static final String LOGIN_ENDPOINT = "/api/auth/login";
    public static final String REGISTRATION_USER_ENDPOINT = "/api/auth/registration";
    public static final String EMAIL_VERIFICATION_ENDPOINT = "/api/auth/email-verification";
    public static final String EMAIL_PASSWORD_RESET_ENDPOINT = "/api/auth/password-reset-request";
    public static final String EMAIL_CONFIRM_PASSWORD_RESET_ENDPOINT = "/api/auth/password-reset-confirmation";
    public static final String CITY_ENDPOINT = "/api/cities/**";

    private final JwtUserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(POST, LOGIN_ENDPOINT).permitAll()
                .antMatchers(POST, REGISTRATION_USER_ENDPOINT).permitAll()
                .antMatchers(POST, EMAIL_PASSWORD_RESET_ENDPOINT).permitAll()
                .antMatchers(POST, EMAIL_CONFIRM_PASSWORD_RESET_ENDPOINT).permitAll()
                .antMatchers(GET, CITY_ENDPOINT).permitAll()
                .antMatchers(GET, EMAIL_VERIFICATION_ENDPOINT).permitAll()
                .antMatchers(SWAGGER_ENDPOINT, SWAGGER_API_DOCS_ENDPOINT).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterAfter(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)  //verify token
                .addFilterAfter(jwtLoginFilter(), JwtAuthenticationFilter.class);  // login and create token
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public JwtLoginFilter jwtLoginFilter() throws Exception {
        return new JwtLoginFilter(LOGIN_ENDPOINT, authenticationManager(), jwtTokenProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
