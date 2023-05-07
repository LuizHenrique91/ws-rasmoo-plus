package com.client.ws.rasmooplus.mapper.wsraspay;

import com.client.ws.rasmooplus.dto.UserPaymentInfoDto;
import com.client.ws.rasmooplus.dto.wsraspay.CreditCardDto;

public class CreditCardMapper {

    public static CreditCardDto build(String document, UserPaymentInfoDto userPaymentInfoDto) {
        return CreditCardDto.builder()
                .cvv(Long.valueOf(userPaymentInfoDto.getCardSecurityCode()))
                .documentNumber(document)
                .month(userPaymentInfoDto.getCardExpirationMonth())
                .number(userPaymentInfoDto.getCardNumber())
                .year(userPaymentInfoDto.getGetCardExpirationYear())
                .installments(userPaymentInfoDto.getInstallments())
                .build();
    }
}
