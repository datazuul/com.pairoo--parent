package com.pairoo.backend.impl.email;

import com.pairoo.domain.Message;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;

/**
 * @author Ralf Eichinger
 */
public class MessageToMailMessageTransformer {
    public MailMessage transform(final Message msg) {
        final MailMessage result = new SimpleMailMessage();
        if (msg.getSender() != null) {
            result.setFrom(msg.getSender().getEmail());
        }
        result.setTo(msg.getReceiver().getEmail());
        result.setSubject(msg.getSubject());
        result.setText(msg.getText());
        return result;
    }
}
