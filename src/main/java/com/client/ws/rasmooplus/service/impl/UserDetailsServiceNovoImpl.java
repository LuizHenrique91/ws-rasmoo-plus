package com.client.ws.rasmooplus.service.impl;

import com.client.ws.rasmooplus.dto.UserDetailsDto;
import com.client.ws.rasmooplus.exception.BadRequestException;
import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.integration.MailIntegration;
import com.client.ws.rasmooplus.model.jpa.UserCredentials;
import com.client.ws.rasmooplus.model.redis.UserRecoveryCode;
import com.client.ws.rasmooplus.repository.jpa.UserDetailsRepository;
import com.client.ws.rasmooplus.repository.redis.UserRecoveryCodeRepository;
import com.client.ws.rasmooplus.service.UserDetailsServiceNovo;
import com.client.ws.rasmooplus.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserDetailsServiceNovoImpl implements UserDetailsServiceNovo {

    @Value("${webservice.rasplus.redis.recoverycode.timeout}")
    private Long recoveryCodeTimeout;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private UserRecoveryCodeRepository userRecoveryCodeRepository;

    @Autowired
    private MailIntegration mailIntegration;

    @Override
    public UserCredentials loadUserByUsername(String userName, String password) {
        var userCredentialsOpt = userDetailsRepository.findByUserName(userName);

        if (userCredentialsOpt.isEmpty()) {
            throw new BadRequestException("Usuário não existe");
        }
        UserCredentials userCredentials = userCredentialsOpt.get();

        if (PasswordUtils.matches(password, userCredentials.getPassword())) {
            return userCredentials;
        }
        throw new BadRequestException("Usuário ou Senha inválidos");
    }

    @Override
    public void sendRecoveryCode(String email) {
        String code = String.format("%04d", new Random().nextInt(10000));

        UserRecoveryCode userRecoveryCode;

        var userRecoveryCodeOpt = userRecoveryCodeRepository.findByEmail(email);
        if (userRecoveryCodeOpt.isEmpty()) {

            var user = userDetailsRepository.findByUserName(email);
            if (user.isEmpty()) {
                throw new NotFoundException("Usuário não encontrado");
            }
            userRecoveryCode = new UserRecoveryCode();
            userRecoveryCode.setEmail(email);

        } else {
            userRecoveryCode = userRecoveryCodeOpt.get();
        }
        userRecoveryCode.setCode(code);
        userRecoveryCode.setCreateDate(LocalDateTime.now());

        userRecoveryCodeRepository.save(userRecoveryCode);
        mailIntegration.send(email, "Código de recuperação de conta: " + code, "Código de recuperação");
    }

    @Override
    public boolean recoveryCodeIsValid(String recoveryCode, String email) {
        var userRecoveryCodeOpt = userRecoveryCodeRepository.findByEmail(email);
        if (userRecoveryCodeOpt.isEmpty()) {
            throw new NotFoundException("Usuário não encontrado");
        }

        UserRecoveryCode userRecoveryCode = userRecoveryCodeOpt.get();

        LocalDateTime timeout = userRecoveryCode.getCreateDate().plusMinutes(recoveryCodeTimeout);
        LocalDateTime now = LocalDateTime.now();

        return recoveryCode.equals(userRecoveryCode.getCode()) && now.isBefore(timeout);
    }

    @Override
    public void updatePasswordByRecoveryCode(UserDetailsDto userDetailsDto) {

        if (recoveryCodeIsValid(userDetailsDto.getRecoveryCode(), userDetailsDto.getEmail())) {
            var userDetails = userDetailsRepository.findByUserName(userDetailsDto.getEmail());
            UserCredentials userCredentials = userDetails.get();

            userCredentials.setPassword(PasswordUtils.encode(userDetailsDto.getPassword()));

            userDetailsRepository.save(userCredentials);
        }
    }
}
