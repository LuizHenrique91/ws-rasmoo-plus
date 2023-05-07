package com.client.ws.rasmooplus.integration.impl;

import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.dto.wsraspay.OrderDto;
import com.client.ws.rasmooplus.dto.wsraspay.PaymentDto;
import com.client.ws.rasmooplus.integration.WsRaspayIntegration;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WsRaspayIntegrationImpl implements WsRaspayIntegration {

    @Value("${webservices.raspay.host}")
    private String raspayHost;

    @Value("${webservices.raspay.v1.customer}")
    private String customerUrl;

    @Value("${webservices.raspay.v1.order}")
    private String orderUrl;

    @Value("${webservices.raspay.v1.payment}")
    private String paymentUrl;

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    public WsRaspayIntegrationImpl() {
        this.restTemplate = new RestTemplate();
        this.headers =  getHttpHeaders();
    }

    @Override
    public CustomerDto createCustomer(CustomerDto dto) {
        System.out.println(raspayHost+customerUrl);
        try{
            HttpEntity<CustomerDto> request = new HttpEntity<>(dto, this.headers);
            ResponseEntity<CustomerDto> response =
                    restTemplate.exchange(raspayHost+customerUrl, HttpMethod.POST, request, CustomerDto.class);

            return response.getBody();
        }catch (Exception e){
            throw e;
        }
    }
    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        try{
            HttpEntity<OrderDto> request = new HttpEntity<>(orderDto, this.headers);
            ResponseEntity<OrderDto> response =
                    restTemplate.exchange(raspayHost+orderUrl, HttpMethod.POST, request, OrderDto.class);

            return response.getBody();
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Boolean processPayment(PaymentDto paymentDto) {
        try{
            HttpEntity<PaymentDto> request = new HttpEntity<>(paymentDto, this.headers);
            ResponseEntity<Boolean> response =
                    restTemplate.exchange(raspayHost+paymentUrl, HttpMethod.POST, request, Boolean.class);

            return response.getBody();
        }catch (Exception e){
            throw e;
        }
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String credential = "rasmooplus:r@sm00";
        String base64 = Base64.encodeBase64String(credential.getBytes());
        headers.add("Authorization", "Basic " + base64);
        return headers;
    }
}
