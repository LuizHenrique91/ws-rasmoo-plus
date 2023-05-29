package com.client.ws.rasmooplus.service.impl;

import com.client.ws.rasmooplus.dto.PaymentProcessDto;
import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.dto.wsraspay.OrderDto;
import com.client.ws.rasmooplus.dto.wsraspay.PaymentDto;
import com.client.ws.rasmooplus.enums.UserTypeEnum;
import com.client.ws.rasmooplus.exception.BusinessException;
import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.integration.MailIntegration;
import com.client.ws.rasmooplus.integration.WsRaspayIntegration;
import com.client.ws.rasmooplus.mapper.UserPaymentInfoMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.CreditCardMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.CustomerMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.OrderMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.PaymentMapper;
import com.client.ws.rasmooplus.model.jpa.User;
import com.client.ws.rasmooplus.model.jpa.UserCredentials;
import com.client.ws.rasmooplus.model.jpa.UserPaymentInfo;
import com.client.ws.rasmooplus.repository.jpa.*;
import com.client.ws.rasmooplus.service.PaymentoInfoService;
import com.client.ws.rasmooplus.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PaymentInfoServiceImpl implements PaymentoInfoService {

    @Value("${webservice.rasplus.default.password}")
    private String defaultPassword;
    private final UserPaymentInfoRepository userPaymentInfoRepository;
    private final UserRepository userRepository;
    private final WsRaspayIntegration wsRaspayIntegration;
    private final MailIntegration mailIntegration;
    private final UserDetailsRepository userDetailsRepository;
    private final UserTypeRepository userTypeRepository;
    private final SubscriptionTypeRepository subscriptionTypeRepository;

    public PaymentInfoServiceImpl(UserPaymentInfoRepository userPaymentInfoRepository,
                                  UserRepository userRepository, WsRaspayIntegration wsRaspayIntegration,
                                  MailIntegration mailIntegration, UserDetailsRepository userDetailsRepository, UserTypeRepository userTypeRepository,
                                  SubscriptionTypeRepository subscriptionTypeRepository) {

        this.userPaymentInfoRepository = userPaymentInfoRepository;
        this.userRepository = userRepository;
        this.wsRaspayIntegration = wsRaspayIntegration;
        this.mailIntegration = mailIntegration;
        this.userDetailsRepository = userDetailsRepository;
        this.userTypeRepository = userTypeRepository;
        this.subscriptionTypeRepository = subscriptionTypeRepository;
    }

    @Override
    public Boolean process(PaymentProcessDto paymentProcessDto) {
        var userOpt = userRepository.findById(paymentProcessDto.getUserPaymentInfoDto().getUserId());

        if (userOpt.isEmpty()) {
            throw new NotFoundException("Usuário não encontrado");
        }

        User user = userOpt.get();
        if (Objects.nonNull(user.getSubscriptionType())) {
            throw new BusinessException("Pagamento não pode ser processado, pois o usuário possui assinatura");
        }

        Boolean successPayment = getSuccessPayment(paymentProcessDto, user);

        return createUserCredentials(paymentProcessDto, user, successPayment);
    }

    private boolean createUserCredentials(PaymentProcessDto paymentProcessDto, User user, Boolean successPayment) {
        if (Boolean.TRUE.equals(successPayment)) {
            UserPaymentInfo userPaymentInfo = UserPaymentInfoMapper.fromDtoToEntity(paymentProcessDto.getUserPaymentInfoDto(), user);
            this.userPaymentInfoRepository.save(userPaymentInfo);

            var userTypeOpt = userTypeRepository.findById(UserTypeEnum.ALUNO.getId());

            if (userTypeOpt.isEmpty()) {
                throw new NotFoundException("UserType não encontrado");
            }

            UserCredentials userCredentials = new UserCredentials(null, user.getEmail(),
                    PasswordUtils.encode(defaultPassword), userTypeOpt.get());

            userDetailsRepository.save(userCredentials);

            var subScriptionTypeOpt = subscriptionTypeRepository.findByProductKey(paymentProcessDto.getProductKey());
            if (subScriptionTypeOpt.isEmpty()) {
                throw new NotFoundException("Subscription type não encontrado");
            }

            user.setSubscriptionType(subScriptionTypeOpt.get());

            userRepository.save(user);

            sendingEmail(user);
            return true;
        }
        return false;
    }

    private void sendingEmail(User user) {
        mailIntegration.send(user.getEmail(), "Usuário: " + user.getEmail() + " Senha: rasmoo", "Acesso Liberado");
    }

    private Boolean getSuccessPayment(PaymentProcessDto paymentProcessDto, User user) {
        CustomerDto customerDto = wsRaspayIntegration.createCustomer(CustomerMapper.build(user));
        OrderDto orderDto = wsRaspayIntegration.createOrder(OrderMapper.build(customerDto.getId(), paymentProcessDto));
        PaymentDto paymentDto = PaymentMapper.build(
                orderDto.getId(),
                orderDto.getCustomerId(),
                CreditCardMapper.build(user.getCpf(), paymentProcessDto.getUserPaymentInfoDto()));
        return wsRaspayIntegration.processPayment(paymentDto);
    }
}
