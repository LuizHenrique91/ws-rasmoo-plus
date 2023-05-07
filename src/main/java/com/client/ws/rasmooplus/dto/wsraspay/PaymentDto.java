package com.client.ws.rasmooplus.dto.wsraspay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {

    @JsonProperty("creditCard")
    private CreditCardDto creditCardDto;

    private String customerId;

    private String orderId;
}
