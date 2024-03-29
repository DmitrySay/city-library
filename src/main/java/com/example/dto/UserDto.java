package com.example.dto;

import com.example.model.Role;
import com.example.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    private String email;

    private UserStatus userStatus;

    private Set<Role> roles;
}
