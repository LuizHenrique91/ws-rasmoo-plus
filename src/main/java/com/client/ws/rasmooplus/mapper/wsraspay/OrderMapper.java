package com.client.ws.rasmooplus.mapper.wsraspay;

import com.client.ws.rasmooplus.dto.PaymentProcessDto;
import com.client.ws.rasmooplus.dto.wsraspay.OrderDto;

public class OrderMapper {
    public static OrderDto build(String customerDto, PaymentProcessDto paymentProcessDto) {
        return OrderDto.builder()
                .customerId(customerDto)
                .productAcronym(paymentProcessDto.getProductKey())
                .discount(paymentProcessDto.getDiscount())
                .build();
    }
}