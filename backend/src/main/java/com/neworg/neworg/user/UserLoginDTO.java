package com.neworg.neworg.user;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
    @NotBlank(message = "email must not be blank")
    String email,
    @NotBlank(message = "password must not be blank")
    String password
) {
    
}
