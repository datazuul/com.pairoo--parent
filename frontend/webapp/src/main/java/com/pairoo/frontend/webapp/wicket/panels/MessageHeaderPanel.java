package com.pairoo.frontend.webapp.wicket.panels;

import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import com.datazuul.framework.util.EnumUtils;
import com.pairoo.domain.Message;
import com.pairoo.domain.enums.MessageState;
import com.pairoo.domain.enums.MessageType;
import com.pairoo.domain.enums.MessageViewMode;

/**
 * @author Ralf Eichinger
 */
public class MessageHeaderPanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    private boolean expanded = false;

    private final MessageViewMode viewMode;

    public MessageHeaderPanel(final String id, final IModel<Message> model, final MessageViewMode viewMode) {
	super(id, new CompoundPropertyModel<Message>(model));
	this.viewMode = viewMode;
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	final Message message = (Message) getDefaultModelObject();

	// from
	add(fromLabel("username", viewMode, message));

	// subject
	add(subjectLabel("subject", (IModel<Message>) getDefaultModel()));

	// date
	add(timestampLabel("timeStamp"));

	// open / close image
	add(toggleImage("toggleImage"));
    }

    private ContextImage toggleImage(String id) {
	final ContextImage toggleImage = new ContextImage(id, new IModel<String>() {
	    private static final long serialVersionUID = 1L;

	    String iconPath = null;

	    @Override
	    public void detach() {
		iconPath = null;
	    }

	    @Override
	    public String getObject() {
		if (!isExpanded()) {
		    iconPath = "images/icons/open.png";
		} else {
		    iconPath = "images/icons/close.png";
		}
		return iconPath;
	    }

	    @Override
	    public void setObject(final String object) {
		iconPath = object;

	    }
	});
	return toggleImage;
    }

    private DateLabel timestampLabel(String id) {
	final DateLabel date = DateLabel.forDatePattern(id, "dd.MM.yy HH:mm");
	return date;
    }

    private Label subjectLabel(String id, IModel<Message> model) {
	Label subject = new Label(id);

	Message message = model.getObject();
	if (MessageType.WINK.equals(message.getMessageType())) {
	    subject.setDefaultModel(new ResourceModel(EnumUtils.getEnumKey(message.getMessageType())));
	}

	return subject;
    }

    private Label fromLabel(String id, final MessageViewMode viewMode, final Message message) {
	Label from = null;
	if (MessageViewMode.INBOX.equals(viewMode)) {
	    from = new Label(id, message.getSender().getUserAccount().getUsername());
	} else if (MessageViewMode.OUTBOX.equals(viewMode)) {
	    from = new Label(id, message.getReceiver().getUserAccount().getUsername());
	}
	return from;
    }

    private MessageState getMessageState() {
	final Message message = (Message) getDefaultModelObject();
	if (MessageViewMode.INBOX.equals(viewMode)) {
	    return message.getMessageStateReceiver();
	    // return messageService.getMessageState(message,
	    // message.getReceiver());
	} else if (MessageViewMode.OUTBOX.equals(viewMode)) {
	    return message.getMessageStateSender();
	} else {
	    return null;
	}
    }

    public boolean isExpanded() {
	return expanded;
    }

    @Override
    protected void onComponentTag(final ComponentTag tag) {
	super.onComponentTag(tag);
	String cssClass = (String) tag.getAttributes().get("class");
        if (cssClass == null) {
            cssClass = "";
        }

	final MessageState state = getMessageState();

	if (state == MessageState.UNREAD && !cssClass.contains(state.name().toLowerCase())) {
	    cssClass = cssClass + " " + MessageState.UNREAD.name().toLowerCase();
	} else if (state == MessageState.READ && cssClass.contains(MessageState.UNREAD.name().toLowerCase())) {
	    cssClass = cssClass.replaceAll(MessageState.UNREAD.name().toLowerCase(), state.name().toLowerCase());
	}
	tag.getAttributes().put("class", cssClass);
    }

    public void setExpanded(final boolean expanded) {
	this.expanded = expanded;
    }
}
