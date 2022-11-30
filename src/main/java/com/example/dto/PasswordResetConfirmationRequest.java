package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetConfirmationRequest {

    @NotBlank
    private String token;

    @NotBlank
    @Pattern(regexp = "[\\w\\d]{4,64}")
    private String password;
}
