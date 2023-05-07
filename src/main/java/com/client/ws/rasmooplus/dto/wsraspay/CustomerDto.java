package com.client.ws.rasmooplus.dto.wsraspay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {

    private String id;

    @JsonProperty(value = "cpf")
    private String cpfCliente;

    private String email;

    private String firstName;

    private String lastName;
}
