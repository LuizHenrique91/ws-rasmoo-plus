package com.client.ws.rasmooplus.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailIntegrationImplTest {

    @Autowired
    private MailIntegration mailIntegration;

    @Test
    void sendMailWhenDtoOk() {
        mailIntegration.send("luizhenriquefjs@hotmail.com", "Olá Hotmail", "Acesso Liberado");
    }
}
