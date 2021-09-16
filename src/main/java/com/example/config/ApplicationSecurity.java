package com.example.config;


import com.example.security.JwtAuthenticationEntryPoint;
import com.example.security.JwtConfigurer;
import com.example.security.JwtTokenProvider;
import com.example.security.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

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
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
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
                .antMatchers(HttpMethod.POST, LOGIN_ENDPOINT).permitAll()
                .antMatchers(SWAGGER_ENDPOINT, SWAGGER_API_DOCS_ENDPOINT).permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(
                "X-Requested-With",
                "Origin",
                "Content-Type",
                "Accept",
                "Authorization",
                "Allow"
        ));

        configuration.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Headers",
                "Authorization",
                "x-xsrf-token",
                "Access-Control-Allow-Methods",
                "Origin",
                "Accept",
                "X-Requested-With",
                "Content-Type",
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