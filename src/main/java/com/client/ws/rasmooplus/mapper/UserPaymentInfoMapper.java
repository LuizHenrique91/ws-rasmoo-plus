package com.client.ws.rasmooplus.mapper;

import com.client.ws.rasmooplus.dto.UserPaymentInfoDto;
import com.client.ws.rasmooplus.model.jpa.User;
import com.client.ws.rasmooplus.model.jpa.UserPaymentInfo;

public class UserPaymentInfoMapper {

    public static UserPaymentInfo fromDtoToEntity(UserPaymentInfoDto userPaymentInfoDto, User user) {
        return UserPaymentInfo.builder()
                .id(userPaymentInfoDto.getId())
                .cardNumber(userPaymentInfoDto.getCardNumber())
                .cardExpirationMonth(userPaymentInfoDto.getCardExpirationMonth())
                .getCardExpirationYear(userPaymentInfoDto.getGetCardExpirationYear())
                .cardSecurityCode(userPaymentInfoDto.getCardSecurityCode())
                .price(userPaymentInfoDto.getPrice())
                .dtPayment(userPaymentInfoDto.getDtPayment())
                .installments(userPaymentInfoDto.getInstallments())
                .user(user)
                .build();

    }
}
