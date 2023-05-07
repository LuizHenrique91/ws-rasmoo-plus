package com.client.ws.rasmooplus.service.impl;

import com.client.ws.rasmooplus.dto.PaymentProcessDto;
import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.dto.wsraspay.OrderDto;
import com.client.ws.rasmooplus.dto.wsraspay.PaymentDto;
import com.client.ws.rasmooplus.exception.BusinessException;
import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.integration.WsRaspayIntegration;
import com.client.ws.rasmooplus.mapper.UserPaymentInfoMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.CreditCardMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.CustomerMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.OrderMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.PaymentMapper;
import com.client.ws.rasmooplus.model.User;
import com.client.ws.rasmooplus.model.UserPaymentInfo;
import com.client.ws.rasmooplus.repository.UserPaymentInfoRepository;
import com.client.ws.rasmooplus.repository.UserRepository;
import com.client.ws.rasmooplus.service.PaymentoInfoService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PaymentInfoServiceImpl implements PaymentoInfoService {

    private final UserPaymentInfoRepository userPaymentInfoRepository;
    private final UserRepository userRepository;
    private final WsRaspayIntegration wsRaspayIntegration;

    public PaymentInfoServiceImpl(UserPaymentInfoRepository userPaymentInfoRepository, UserRepository userRepository, WsRaspayIntegration wsRaspayIntegration) {
        this.userPaymentInfoRepository = userPaymentInfoRepository;
        this.userRepository = userRepository;
        this.wsRaspayIntegration = wsRaspayIntegration;
    }

    @Override
    public Boolean paymentProcessDto(PaymentProcessDto paymentProcessDto) {
        var userOpt = userRepository.findById(paymentProcessDto.getUserPaymentInfoDto().getUserId());

        if(userOpt.isEmpty()){
            throw new NotFoundException("Usuário não encontrado");
        }

        User user = userOpt.get();
        if(Objects.nonNull(user.getSubscriptionType())){
            throw new BusinessException("Pagamento não pode ser processado, pois o usuário possui assinatura");
        }

        CustomerDto customerDto = wsRaspayIntegration.createCustomer(CustomerMapper.build(user));

        OrderDto orderDto = wsRaspayIntegration.createOrder(OrderMapper.build(customerDto.getId(), paymentProcessDto));

        PaymentDto paymentDto = PaymentMapper.build(
                orderDto.getId(),
                orderDto.getCustomerId(),
                CreditCardMapper.build(user.getCpf(), paymentProcessDto.getUserPaymentInfoDto()));

        Boolean successPayment = wsRaspayIntegration.processPayment(paymentDto);

        if (successPayment){
            UserPaymentInfo userPaymentInfo = UserPaymentInfoMapper.fromDtoToEntity(paymentProcessDto.getUserPaymentInfoDto(), user);
            this.userPaymentInfoRepository.save(userPaymentInfo);
        }

        return null;
    }
}
