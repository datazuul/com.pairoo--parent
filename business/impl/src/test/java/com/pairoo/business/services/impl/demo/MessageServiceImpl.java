package com.pairoo.business.services.impl.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.Search;
import com.pairoo.business.api.MessageService;
import com.pairoo.business.services.impl.DataRepository;
import com.pairoo.domain.Message;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.MessageState;
import com.pairoo.domain.enums.MessageType;

public class MessageServiceImpl implements MessageService {
	private final DataRepository repo;

	public MessageServiceImpl(final DataRepository repo) {
		super();
		this.repo = repo;
	}

	@Override
	public void changeState(final Message message, final User user, final MessageState state) {
		if (message.getReceiver() == user) {
			// user is receiver
			if (message.getMessageStateReceiver() != state) {
				message.setMessageStateReceiver(state);
			}
		} else {
			// user is sender
			if (message.getMessageStateSender() != state) {
				message.setMessageStateSender(state);
			}
		}
	}

	@Override
	public long count(final ISearch search) {
		return repo.getMessagesList().size();
	}

	@Override
	public Long countAll() {
		return new Long(repo.getMessagesList().size());
	}

	@Override
	public long countUnreadInboxMessages(final User user) {
		return repo.getMessagesList().size();
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
		answerMessage.setSubject("Re: " + message.getSubject());

		// add comment and header to message text
		final String messageText = message.getText();
		final StringBuffer commentedText = new StringBuffer();
		final BufferedReader bufferedReader = new BufferedReader(new StringReader(messageText));
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				commentedText.append("> " + line).append("\n");
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		answerMessage.setText(commentedText.toString());

		return answerMessage;
	}

	@Override
	public boolean delete(final Message message) {
		return false;
	}

	@Override
	public boolean delete(final Message message, final User user) {
		repo.getMessagesList().remove(message);
		return true;
	}

	@Override
	public List<Message> findAll(final int offset, final int max) {
		return repo.getMessagesList();
	}

	@Override
	public Message get(final Long id) {
		return repo.getMessagesList().get(id.intValue());
	}

	@Override
	public Search getInboxSearchCriteria(final User user) {
		return new Search();
	}

	@Override
	public MessageState getMessageState(final Message message, final User user) {
		return message.getMessageStateReceiver();
	}

	@Override
	public Search getOutboxSearchCriteria(final User user) {
		return new Search();
	}

	@Override
	public boolean save(final Message object) {
		return true;
	}

	@Override
	public List<Message> search(final ISearch search) {
		return repo.getMessagesList();
	}

	@Override
	public void send(final Message message) {
	}

	@Override
	public void update(final Message object) {
	}

	@Override
	public Message createMessage(final User receiver, final User sender, final Locale locale, final String subjectKey,
			final String templateName, final Map<String, Object> model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createMessageText(String templateName, Locale locale, Map<String, Object> model) {
		return "";
	}
}
