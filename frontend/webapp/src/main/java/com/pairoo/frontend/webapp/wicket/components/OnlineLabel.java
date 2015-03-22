package com.pairoo.frontend.webapp.wicket.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.pairoo.domain.UserAccount;
import org.apache.wicket.behavior.AttributeAppender;

/**
 * @author Ralf Eichinger
 */
public class OnlineLabel extends Label {

    private static final long serialVersionUID = 1L;

    public OnlineLabel(final String id, final IModel<UserAccount> model) {
        super(id);

        final UserAccount userAccount = model.getObject();
        boolean online = userAccount.isOnline();
        init(online);
    }

    public OnlineLabel(final String id, boolean online) {
        super(id);
        init(online);
    }

    private void init(boolean online) {
        // online status
        String onlineText = null;
        if (online) {
            onlineText = "online";
        } else {
            onlineText = "offline";
        }

        setDefaultModel(new Model<>(onlineText));
        add(new AttributeAppender("class", Model.of(onlineText), " "));
    }
}
