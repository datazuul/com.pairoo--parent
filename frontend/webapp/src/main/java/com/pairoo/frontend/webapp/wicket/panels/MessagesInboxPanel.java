package com.pairoo.frontend.webapp.wicket.panels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Localizer;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.datazuul.framework.webapp.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import com.datazuul.framework.webapp.wicket.dataprovider.DomainObjectDataProvider;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.googlecode.genericdao.search.Search;
import com.pairoo.business.api.MessageService;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.Message;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.MessageState;
import com.pairoo.domain.enums.MessageType;
import com.pairoo.domain.enums.MessageViewMode;
import com.pairoo.domain.enums.Role;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.MembershipPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.MessagesPage;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.AnswerMessagePanel;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.SendWinkPanel;

/**
 * @author Ralf Eichinger
 */
public class MessagesInboxPanel extends BasePanel {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = MessageService.BEAN_ID)
    private MessageService messageService;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;

    public MessagesInboxPanel(final String id, final IModel<User> model) {
	super(id, model);
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(MessagesInboxPanel.class);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	// data provider
	final DomainObjectDataProvider<Long, Message, String> messageDataProvider = createDataProvider();

	// data view (accordion)
	final DataView<Message> dataView = createDataViewComponent("messages", messageDataProvider);
	add(dataView);

	// paging
	add(createPagingComponent("navigator", dataView));
    }

    private PagingNavigator createPagingComponent(final String id, final DataView<Message> dataView) {
	return new PagingNavigator(id, dataView) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public boolean isVisible() {
		return (getPageable().getPageCount() > 1 && dataView.getItemCount() > 0);
	    }
	};
    }

    private DataView<Message> createDataViewComponent(final String id,
	    final DomainObjectDataProvider<Long, Message, String> messageDataProvider) {
	// http://efreedom.com/Question/1-3822324/Insert-Sub-Rows-Wicket-DataTable
	// http://karthikg.wordpress.com/2008/01/24/developing-a-custom-apache-wicket-component/
	// http://apache-wicket.1842946.n4.nabble.com/Visibility-setting-with-Ajax-td3148582.html

	// final Accordion accordion = new Accordion("accordion");
	final DataView<Message> dataView = new DataView<Message>(id, messageDataProvider, 20) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(final Item<Message> item) {
		final Message message = item.getModelObject();

		// final ViewMessagePanel viewMessagePanel = new
		// ViewMessagePanel("messagePanel", new Model<Message>(
		// message));
		// viewMessagePanel.setRenderBodyOnly(true);
		// item.add(viewMessagePanel);
		// item.add(new AjaxEventBehavior("onclick") {

		final WebMarkupContainer contentPanelContainer = new WebMarkupContainer("messageContentContainer");
		contentPanelContainer.add(new AttributeModifier("style", "display:none;"));
		contentPanelContainer.setOutputMarkupId(true);
		// lazyContentPanel.setVisible(false);
		item.add(contentPanelContainer);

		final WebMarkupContainer contentPanel = new WebMarkupContainer("messageContent");
		contentPanel.setOutputMarkupId(true);
		contentPanelContainer.add(contentPanel);

		final MessageHeaderPanel messageHeaderPanel = new MessageHeaderPanel("messageHeader", item.getModel(),
			MessageViewMode.INBOX);
		messageHeaderPanel.setOutputMarkupId(true);
		messageHeaderPanel.add(new AjaxEventBehavior("onclick") {
		    private static final long serialVersionUID = 1L;

		    @Override
		    protected void onEvent(final AjaxRequestTarget target) {
			User user = (User) MessagesInboxPanel.this.getDefaultModelObject();
			// FIXME why does strategy not work in production?
			if (!MessageType.WINK.equals(message.getMessageType())
				&& !user.getUserAccount().hasRole(Role.PREMIUM.getCode())) {
			    getSession().info(Localizer.get().getString("error.restricted_to_premium_members", null));
			    throw new RestartResponseException(new MembershipPage(
				    (IModel<User>) new LoadableDetachableDomainObjectModel<Long>(user, userService)));
			}
			messageHeaderPanel.setExpanded(!messageHeaderPanel.isExpanded());
			if (messageHeaderPanel.isExpanded()) {
			    final IModel<Message> model = (IModel<Message>) getComponent().getDefaultModel();
			    // IModel<Message> model = new
			    // Model<Message>((Message)
			    // messageHeaderPanel.getDefaultModelObject());
			    final Component newContent = new MessageContentPanel("messageContent", model,
				    MessageViewMode.INBOX) {
				private static final long serialVersionUID = 1L;

				@Override
				public void changeState() {
				    messageService.changeState(message, message.getReceiver(), MessageState.READ);
				}

				@Override
				public void onAnswerMessage(final AjaxRequestTarget target) {
				    // create answer message
				    final Message answerMessage = messageService.createAnswer(message);

				    Panel answerMessagePanel = null;
				    if (!MessageType.WINK.equals(message.getMessageType())) {
					// answer to a not-wink message
					answerMessagePanel = new AnswerMessagePanel(getId(), new Model<Message>(
						answerMessage)) {
					    private static final long serialVersionUID = 1L;

					    @Override
					    protected void onGoBack() {
						// TODO Auto-generated method
						// stub
					    }
					};
					answerMessagePanel.setOutputMarkupId(true);
					// authorization
					MetaDataRoleAuthorizationStrategy.authorize(answerMessagePanel, new Action(
						Action.RENDER), Role.PREMIUM.getCode());
				    } else {
					// answer to a wink message
					answerMessage.setMessageType(MessageType.WINK);
					answerMessage.setText(null);

					answerMessagePanel = new SendWinkPanel(getId(), new Model<Message>(
						answerMessage)) {
					    private static final long serialVersionUID = 1L;

					    @Override
					    protected void onGoBack() {
						// TODO Auto-generated method
						// stub
					    }
					};
				    }
				    final Component contentPanel = item.get("messageContentContainer:messageContent");
				    contentPanel.replaceWith(answerMessagePanel);
				    target.add(answerMessagePanel);
				}

				@Override
				public void onDeleteMessage() {
				    User user = (User) MessagesInboxPanel.this.getDefaultModelObject();
				    setResponsePage(new MessagesPage(new LoadableDetachableDomainObjectModel<Long>(
					    user, userService)));
				}
			    };

			    if (!MessageType.WINK.equals(message.getMessageType())) {
				// authorization
				MetaDataRoleAuthorizationStrategy.authorize(newContent, new Action(Action.RENDER),
					Role.PREMIUM.getCode());
			    }

			    newContent.setOutputMarkupId(true);

			    Component contentPanel = item.get("messageContentContainer:messageContent");
			    contentPanel.replaceWith(newContent);
			    contentPanel = newContent;

			    contentPanelContainer.add(new AttributeModifier("style", ""));
			} else {
			    final WebMarkupContainer newContent = new WebMarkupContainer("messageContent");
			    newContent.setOutputMarkupId(true);
			    Component contentPanel = item.get("messageContentContainer:messageContent");
			    contentPanel.replaceWith(newContent);
			    contentPanel = newContent;

			    contentPanelContainer.add(new AttributeModifier("style", "display:none;"));
			}
			target.add(messageHeaderPanel);
			target.add(contentPanelContainer);
		    }
		});
		item.add(messageHeaderPanel);
	    }
	};
	// dataView.setRenderBodyOnly(true);
	dataView.setOutputMarkupId(true);
	// accordion.activate(0);
	// accordion.setNavigation(true);
	// accordion.add(dataView);
	// add(accordion);
	return dataView;
    }

    private DomainObjectDataProvider<Long, Message, String> createDataProvider() {
	final User user = (User) getDefaultModelObject();

	final Search inboxCriteria = messageService.getInboxSearchCriteria(user);

	// set up data provider
	final DomainObjectDataProvider<Long, Message, String> messageDataProvider = new DomainObjectDataProvider<Long, Message, String>(
		messageService, inboxCriteria);
	return messageDataProvider;
    }
}
