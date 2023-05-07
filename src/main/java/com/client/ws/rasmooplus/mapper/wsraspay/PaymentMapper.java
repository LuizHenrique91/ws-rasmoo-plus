package com.client.ws.rasmooplus.mapper.wsraspay;

import com.client.ws.rasmooplus.dto.wsraspay.CreditCardDto;
import com.client.ws.rasmooplus.dto.wsraspay.PaymentDto;

public class PaymentMapper {
    public static PaymentDto build(String orderId, String customerId, CreditCardDto creditCardDto) {
        return PaymentDto.builder()
                .customerId(customerId)
                .orderId(orderId)
                .creditCardDto(creditCardDto)
                .build();
    }
}
