package com.example.security;

import com.example.model.Permission;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class JwtUser extends User implements UserDetails {

    private final boolean enabled;

    public JwtUser(Long id, String email, String password, UserStatus userStatus, Set<Role> roles, boolean enabled) {
        super(id, email, password, userStatus, roles);
        this.enabled = enabled;
    }

    public Long getId() {
        return super.getId();
    }

    @Override
    public String getUsername() {
        return super.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    /**
     * Get user roles and permissions amd convert then all to GrantedAuthorities
     *
     * @return authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        Set<Permission> permissions = new HashSet<>();
        Set<Role> roles = super.getRoles();
        if (roles == null) {
            return authorities;
        }
        roles.forEach((role) -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            permissions.addAll(role.getPermissions());
        });
        permissions.forEach((permission) -> {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        });
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
