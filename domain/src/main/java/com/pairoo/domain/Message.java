package com.pairoo.domain;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.pairoo.domain.enums.MessageState;
import com.pairoo.domain.enums.MessageType;

/**
 * @author Ralf Eichinger
 */
public class Message extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private MessageState messageStateReceiver = MessageState.UNREAD;
    private MessageState messageStateSender = MessageState.UNREAD;

    private MessageType messageType;
    private User receiver;
    private User sender;
    private String subject;
    @Length(max = 4000)
    private String text;
    private Date timeStamp;

    /**
     * @return the messageStateReceiver
     */
    public MessageState getMessageStateReceiver() {
	return messageStateReceiver;
    }

    /**
     * @return the messageStateSender
     */
    public MessageState getMessageStateSender() {
	return messageStateSender;
    }

    /**
     * @return the messageType
     */
    public MessageType getMessageType() {
	return messageType;
    }

    /**
     * @return the receiver
     */
    public User getReceiver() {
	return receiver;
    }

    /**
     * @return the sender
     */
    public User getSender() {
	return sender;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
	return subject;
    }

    /**
     * @return the text
     */
    public String getText() {
	return text;
    }

    /**
     * @return the timeStamp
     */
    public Date getTimeStamp() {
	return timeStamp;
    }

    /**
     * @param messageStateReceiver
     *            the messageStateReceiver to set
     */
    public void setMessageStateReceiver(final MessageState messageStateReceiver) {
	this.messageStateReceiver = messageStateReceiver;
    }

    /**
     * @param messageStateSender
     *            the messageStateSender to set
     */
    public void setMessageStateSender(final MessageState messageStateSender) {
	this.messageStateSender = messageStateSender;
    }

    /**
     * @param messageType
     *            the messageType to set
     */
    public void setMessageType(final MessageType messageType) {
	this.messageType = messageType;
    }

    /**
     * @param receiver
     *            the receiver to set
     */
    public void setReceiver(final User receiver) {
	this.receiver = receiver;
    }

    /**
     * @param sender
     *            the sender to set
     */
    public void setSender(final User sender) {
	this.sender = sender;
    }

    /**
     * @param subject
     *            the subject to set
     */
    public void setSubject(final String subject) {
	this.subject = subject;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(final String text) {
	this.text = text;
    }

    /**
     * @param timeStamp
     *            the timeStamp to set
     */
    public void setTimeStamp(final Date timeStamp) {
	this.timeStamp = timeStamp;
    }

}
