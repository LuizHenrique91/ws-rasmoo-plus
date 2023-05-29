package com.client.ws.rasmooplus.service;

import com.client.ws.rasmooplus.dto.PaymentProcessDto;

public interface PaymentoInfoService {
    Boolean process(PaymentProcessDto paymentProcessDto);
}
