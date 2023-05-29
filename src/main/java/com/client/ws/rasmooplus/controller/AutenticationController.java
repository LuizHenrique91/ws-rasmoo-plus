package com.client.ws.rasmooplus.controller;

import com.client.ws.rasmooplus.dto.LoginDto;
import com.client.ws.rasmooplus.dto.TokenDto;
import com.client.ws.rasmooplus.dto.UserDetailsDto;
import com.client.ws.rasmooplus.service.UserDetailsServiceNovo;
import com.client.ws.rasmooplus.service.impl.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/auth")
public class AutenticationController {
    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    private UserDetailsServiceNovo userDetailsServiceNovo;

    @PostMapping
    public ResponseEntity<TokenDto> auth(@RequestBody @Valid LoginDto loginDto) {
        return ResponseEntity.ok().body(authenticationService.auth(loginDto));
    }

    @PostMapping(path = "/send-recovery-code/{email}")
    public ResponseEntity<Void> sendRecoveryCode(@PathVariable("email") String email) {
        userDetailsServiceNovo.sendRecoveryCode(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping(path = "/vavlidate-code")
    public ResponseEntity<Boolean> validateCode(@RequestParam("code") String code, @RequestParam("email") String email) {
        return ResponseEntity.ok().body(userDetailsServiceNovo.recoveryCodeIsValid(code, email));
    }

    @PatchMapping(path = "/recovery-code/password")
    public ResponseEntity<Void> sendRecoveryCode(@RequestBody @Valid UserDetailsDto userDetailsDto) {
        userDetailsServiceNovo.updatePasswordByRecoveryCode(userDetailsDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
