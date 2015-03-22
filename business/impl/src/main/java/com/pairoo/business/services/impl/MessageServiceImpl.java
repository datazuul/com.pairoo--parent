package com.pairoo.business.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.CharEncoding;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.datazuul.framework.i18n.ResourceBundleUtil;
import com.googlecode.genericdao.search.Search;
import com.pairoo.backend.dao.MessageDao;
import com.pairoo.business.LocalizedStrings;
import com.pairoo.business.api.MessageService;
import com.pairoo.domain.Message;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.MessageState;
import com.pairoo.domain.enums.MessageType;

/**
 * @author Ralf Eichinger
 */
public class MessageServiceImpl extends AbstractDomainObjectServiceImpl<Long, Message> implements MessageService {
    static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final VelocityEngine velocityEngine;

    /**
     * Constructor needed for test handing over a mock dao.
     * 
     * @param dao
     *            the data access interface
     */
    public MessageServiceImpl(final MessageDao dao, final VelocityEngine velocityEngine) {
	super(dao);
	this.velocityEngine = velocityEngine;
    }

    @Override
    public void changeState(final Message message, final User user, final MessageState state) {
	if (message.getReceiver() == user) {
	    // user is receiver
	    if (message.getMessageStateReceiver() != state) {
		message.setMessageStateReceiver(state);
		save(message);
	    }
	} else {
	    // user is sender
	    if (message.getMessageStateSender() != state) {
		message.setMessageStateSender(state);
		save(message);
	    }
	}
    }

    @Override
    public long countUnreadInboxMessages(final User user) {
	final Search unreadInboxSearchCriteria = getInboxSearchCriteria(user);
	unreadInboxSearchCriteria.addFilterEqual("messageStateReceiver", MessageState.UNREAD);
	final long count = count(unreadInboxSearchCriteria);
	return count;
    }

    @Override
    public Message createAnswer(final Message message) {
	final Message answerMessage = new Message();
	// set sender and receiver
	answerMessage.setMessageStateReceiver(MessageState.UNREAD);
	answerMessage.setMessageStateSender(MessageState.UNREAD);
	answerMessage.setMessageType(MessageType.TEXT);
	answerMessage.setReceiver(message.getSender());
	answerMessage.setSender(message.getReceiver());
	String subject = message.getSubject();
	if (subject == null) {
	    subject = "";
	}
	answerMessage.setSubject("Re: " + subject);

	// add comment and header to message text
	final String messageText = message.getText();
	final StringBuffer commentedText = new StringBuffer();
	commentedText.append("\n\n\n>>>>>>> ").append(message.getSender().getUserAccount().getUsername()).append(" ")
		.append(df.format(message.getTimeStamp())).append(" >>>>>>>\n");
	if (messageText != null) {
	    final BufferedReader bufferedReader = new BufferedReader(new StringReader(messageText));
	    String line;
	    try {
		while ((line = bufferedReader.readLine()) != null) {
		    commentedText.append("> ").append(line).append("\n");
		}
	    } catch (final IOException e) {
		e.printStackTrace();
	    }
	}
	answerMessage.setText(commentedText.toString());

	return answerMessage;
    }

    @Override
    public String createMessageText(final String templateName, final Locale locale, final Map<String, Object> model) {
	// text
	String text = null;
	try {
	    final String resourcePath = ResourceBundleUtil.getResourceClassPath(templateName, ".vm", locale);
	    text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, resourcePath, CharEncoding.UTF_8, model);
	} catch (final VelocityException e) {
	    e.printStackTrace();
	}
	return text;
    }

    @Override
    public boolean delete(final Message message, final User user) {
	// set message state to deleted for the given user
	if (message.getReceiver() == user) {
	    message.setMessageStateReceiver(MessageState.DELETED);
	} else {
	    message.setMessageStateSender(MessageState.DELETED);
	}
	// cleanup job (optional): if sender and receiver have set state
	// deleted, delete message from DB
	if (message.getMessageStateReceiver() == MessageState.DELETED
		&& message.getMessageStateSender() == MessageState.DELETED) {
	    return super.delete(message);
	} else {
	    return save(message);
	}
    }

    @Override
    public Search getInboxSearchCriteria(final User user) {
	final Search search = new Search(Message.class);

	// receiver is given user
	search.addFilterEqual("receiver.id", user.getId());

	// sort date decreasing
	search.addSortDesc("timeStamp");

	return search;
    }

    @Override
    public MessageState getMessageState(final Message message, final User user) {
	if (message.getReceiver() == user) {
	    // user is receiver
	    return message.getMessageStateReceiver();
	} else {
	    // user is sender
	    return message.getMessageStateSender();
	}
    }

    @Override
    public Search getOutboxSearchCriteria(final User user) {
	final Search search = new Search(Message.class);

	// sender is given user
	search.addFilterEqual("sender.id", user.getId());

	// sort date decreasing
	search.addSortDesc("timeStamp");

	return search;
    }

    @Override
    public void send(final Message message) {
	// 0. set attributes
	if (message.getTimeStamp() == null) {
	    message.setTimeStamp(new Date());
	}

	// 1. save message
	final boolean saved = save(message);

	// 2. notify receiver about new message
	// TODO implement notification
    }

    @Override
    public Message createMessage(final User receiver, final User sender, final Locale locale, final String subject,
	    final String templateName, final Map<String, Object> model) {
	final Message msg = new Message();
	// recipient
	msg.setReceiver(receiver);

	// sender
	msg.setSender(sender);

	// subject
	msg.setSubject(subject);
	final String text = createMessageText(templateName, locale, model);
	msg.setText(text);
	return msg;
    }
}
