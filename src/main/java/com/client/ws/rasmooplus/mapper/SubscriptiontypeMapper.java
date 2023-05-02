package com.client.ws.rasmooplus.mapper;

import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.model.SubscriptionType;

public class SubscriptiontypeMapper {

    public static SubscriptionType fromDtoToEntity(SubscriptionTypeDto subscriptionTypeDto) {
        return SubscriptionType.builder()
                .id(subscriptionTypeDto.getId())
                .accessMonths(subscriptionTypeDto.getAccessMonths())
                .price(subscriptionTypeDto.getPrice())
                .name(subscriptionTypeDto.getName())
                .productKey(subscriptionTypeDto.getProductKey())
                .build();
    }
}
