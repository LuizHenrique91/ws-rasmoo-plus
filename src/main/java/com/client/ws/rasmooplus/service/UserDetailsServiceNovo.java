package com.client.ws.rasmooplus.service;

import com.client.ws.rasmooplus.dto.UserDetailsDto;
import com.client.ws.rasmooplus.model.jpa.UserCredentials;

public interface UserDetailsServiceNovo {
    UserCredentials loadUserByUsername(String userName, String password);

    void sendRecoveryCode(String email);

    boolean recoveryCodeIsValid(String recoveryCode, String email);

    void updatePasswordByRecoveryCode(UserDetailsDto userDetailsDto);
}
