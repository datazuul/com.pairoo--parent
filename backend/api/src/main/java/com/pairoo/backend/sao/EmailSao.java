package com.pairoo.backend.sao;

import org.springframework.integration.annotation.Gateway;

import com.pairoo.domain.Message;

public interface EmailSao {
    @Gateway(requestChannel = "sendEmailPreProcessingChannel")
    public void sendMail(Message msg);
}
