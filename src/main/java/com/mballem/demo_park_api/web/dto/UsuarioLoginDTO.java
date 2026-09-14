package com.mballem.demo_park_api.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioLoginDTO {

    @NotBlank
    @Email(message = "Formato do e-mail está invalido.", regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
    public String username;

    @NotBlank
    @Size(min = 6, max = 6)
    public String password;
}
