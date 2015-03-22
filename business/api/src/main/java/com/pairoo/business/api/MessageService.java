package com.pairoo.business.api;

import java.util.Locale;
import java.util.Map;

import com.datazuul.framework.business.services.DomainObjectService;
import com.googlecode.genericdao.search.Search;
import com.pairoo.domain.Message;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.MessageState;

/**
 * @author Ralf Eichinger
 */
public interface MessageService extends DomainObjectService<Long, Message> {
	public static final String BEAN_ID = "messageService";

	/**
	 * Change the state of a message.
	 * 
	 * @param message
	 *            message to change
	 * @param user
	 *            user for whom to change the state
	 * @param state
	 *            new message state
	 */
	public void changeState(Message message, User user, MessageState state);

	/**
	 * Count unread messages in inbox of the given user as receiver.
	 * 
	 * @param user
	 *            receiver
	 * @return number of unread messages
	 */
	public long countUnreadInboxMessages(User user);

	/**
	 * Create/Prepare an answer message to the given message.
	 * 
	 * @param message
	 *            message to be answered
	 * @return answer message
	 */
	public Message createAnswer(Message message);

	/**
	 * Set message state to deleted for the given user or delete message
	 * completely if receiver and sender set state to deleted.
	 * 
	 * @param message
	 *            message to be deleted/state changed
	 * @param user
	 *            user for whom to change state
	 * @return
	 */
	public boolean delete(Message message, User user);

	/**
	 * Create a Search for getting the inbox messages of the given user
	 * 
	 * @param user
	 *            given user
	 * @return inbox messages criteria
	 */
	public Search getInboxSearchCriteria(User user);

	/**
	 * Get message state for a given user.
	 * 
	 * @param message
	 *            message to get state for
	 * @param user
	 *            user who wants to know his message state
	 * @return message state
	 */
	public MessageState getMessageState(Message message, User user);

	/**
	 * Create a Search for getting the outbox messages of the given user
	 * 
	 * @param user
	 *            given user
	 * @return outbox search criteria
	 */
	public Search getOutboxSearchCriteria(User user);

	/**
	 * Send the given message from sender to receiver (included in message).
	 * 
	 * @param message
	 *            the message to be sent
	 */
	public void send(Message message);

	/**
	 * Convenient method for creating a message.
	 * 
	 * @param receiver
	 *            receiving user
	 * @param sender
	 *            sending user (email property)
	 * @param locale
	 *            preferred text locale
	 * @param subjectKey
	 *            key to lookup subject text
	 * @param templateName
	 *            template path
	 * @param model
	 *            model containing variables for template
	 * @return message
	 */
	public Message createMessage(User receiver, User sender, Locale locale, String subjectKey, String templateName,
			Map<String, Object> model);

	/**
	 * Merge given params into a localized template.
	 * 
	 * @param templateName
	 *            name of template
	 * @param locale
	 *            locale of language
	 * @param model
	 *            params
	 * @return merged text
	 */
	public String createMessageText(final String templateName, final Locale locale, final Map<String, Object> model);
}
