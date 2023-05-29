package com.client.ws.rasmooplus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {

    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "Atributo inválido")
    private String password;

    private String recoveryCode;
}
