package com.example.security;

import com.example.exception.NotFoundException;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

@NoArgsConstructor
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private Converter<Jwt, Collection<GrantedAuthority>> grantedAuthoritiesConverter;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);
        User user = userRepository
                .findByEmail(jwt.getClaimAsString("email"))
                .orElseThrow(() -> new NotFoundException("User is not found."));

        JwtUser jwtUserInfo = new JwtUser(jwt, user.getId(), user.getEmail());
        return new JwtAuthenticationToken(jwtUserInfo, authorities);
    }

    public void setJwtGrantedAuthoritiesConverter(Converter<Jwt, Collection<GrantedAuthority>> grantedAuthoritiesConverter) {
        if (grantedAuthoritiesConverter != null) {
            this.grantedAuthoritiesConverter = grantedAuthoritiesConverter;
        }
    }
}
