package com.client.ws.rasmooplus.controller;

import com.client.ws.rasmooplus.dto.PaymentProcessDto;
import com.client.ws.rasmooplus.service.PaymentoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/payment")
public class PaymentInfoController {

    @Autowired
    private PaymentoInfoService paymentoInfoService;

    @PostMapping(path = "/process")
    public ResponseEntity<Boolean> process(@RequestBody @Valid PaymentProcessDto paymentProcessDto) {
        return ResponseEntity.ok().body(paymentoInfoService.process(paymentProcessDto));
    }
}
