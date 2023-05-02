package com.client.ws.rasmooplus.mapper;

import com.client.ws.rasmooplus.dto.UserDto;
import com.client.ws.rasmooplus.model.SubscriptionType;
import com.client.ws.rasmooplus.model.User;
import com.client.ws.rasmooplus.model.UserType;

public class UserMapper {

    public static User fromDtoToEntity(UserDto userDto, UserType userType, SubscriptionType subscriptionType) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .cpf(userDto.getCpf())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .dtSubscription(userDto.getDtSubscription())
                .dtExpiration(userDto.getDtExpiration())
                .userType(userType)
                .subscriptionType(subscriptionType)
                .build();
    }
}
