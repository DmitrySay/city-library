package com.example.security;

import com.example.exception.RestException;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.model.UserStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RestException("User is not found."));
        return new JwtUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getUserStatus(),
                user.getRoles(),
                ACTIVE.equals(user.getUserStatus())
        );
    }
}
