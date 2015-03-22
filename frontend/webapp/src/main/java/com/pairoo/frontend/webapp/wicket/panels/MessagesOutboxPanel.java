package com.pairoo.frontend.webapp.wicket.panels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.datazuul.framework.webapp.wicket.dataprovider.DomainObjectDataProvider;
import com.googlecode.genericdao.search.Search;
import com.pairoo.business.api.MessageService;
import com.pairoo.domain.Message;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.MessageViewMode;

/**
 * @author Ralf Eichinger
 */
public class MessagesOutboxPanel extends BasePanel {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = MessageService.BEAN_ID)
    private MessageService messageService;

    public MessagesOutboxPanel(final String id, final IModel<User> model) {
        super(id, model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(MessagesOutboxPanel.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // data provider
        final DomainObjectDataProvider<Long, Message, String> messageDataProvider = createDataProvider();

        // data view
        final DataView<Message> dataView = createDataViewComponent("messages", messageDataProvider);
        add(dataView);

        // paging
        add(createPagingComponent("navigator", dataView));
    }

    private PagingNavigator createPagingComponent(String id, final DataView<Message> dataView) {
        return new PagingNavigator(id, dataView) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return (getPageable().getPageCount() > 1 && dataView.getItemCount() > 0);
            }
        };
    }

    private DataView<Message> createDataViewComponent(String id,
            final DomainObjectDataProvider<Long, Message, String> messageDataProvider) {
        // http://efreedom.com/Question/1-3822324/Insert-Sub-Rows-Wicket-DataTable
        // http://karthikg.wordpress.com/2008/01/24/developing-a-custom-apache-wicket-component/
        // http://apache-wicket.1842946.n4.nabble.com/Visibility-setting-with-Ajax-td3148582.html

        // final Accordion accordion = new Accordion("accordion");
        final DataView<Message> dataView = new DataView<Message>(id, messageDataProvider, 20) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final Item<Message> item) {
                // final ViewMessagePanel viewMessagePanel = new
                // ViewMessagePanel("messagePanel", new Model<Message>(
                // message));
                // viewMessagePanel.setRenderBodyOnly(true);
                // item.add(viewMessagePanel);
                // item.add(new AjaxEventBehavior("onclick") {

                final MessageHeaderPanel messageHeaderPanel = new MessageHeaderPanel("messageHeader", item.getModel(),
                        MessageViewMode.OUTBOX);
                messageHeaderPanel.setOutputMarkupId(true);
                messageHeaderPanel.add(new AjaxEventBehavior("onclick") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onEvent(final AjaxRequestTarget target) {
                        messageHeaderPanel.setExpanded(!messageHeaderPanel.isExpanded());
                        Component newContent = null;
                        if (messageHeaderPanel.isExpanded()) {
                            final IModel<Message> model = (IModel<Message>) getComponent().getDefaultModel();
                            // IModel<Message> model = new
                            // Model<Message>((Message)
                            // messageHeaderPanel.getDefaultModelObject());
                            newContent = new MessageContentPanel("messageContent", model, MessageViewMode.OUTBOX) {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void changeState() {
                                    // do not change, just view state of
                                    // receiver
                                }

                                @Override
                                public void onAnswerMessage(final AjaxRequestTarget target) {
                                    // TODO Auto-generated method stub
                                }

                                @Override
                                public void onDeleteMessage() {
                                    setResponsePage(getPage());
                                }
                            };
                        } else {
                            newContent = new WebMarkupContainer("messageContent").add(new AttributeModifier("style",
                                    "display:none;"));
                        }
                        newContent.setOutputMarkupId(true);
                        Component contentPanel = item.get("messageContent");
                        contentPanel.replaceWith(newContent);
                        contentPanel = newContent;
                        target.add(messageHeaderPanel);
                        target.add(contentPanel);
                    }
                });
                item.add(messageHeaderPanel);

                final Component contentPanel = new WebMarkupContainer("messageContent").add(new AttributeModifier(
                        "style", "display:none;"));
                contentPanel.setOutputMarkupId(true);
                // lazyContentPanel.setVisible(false);
                item.add(contentPanel);
            }
        };
        dataView.setRenderBodyOnly(true);
        // accordion.activate(0);
        // accordion.setNavigation(true);
        // accordion.add(dataView);
        // add(accordion);
        return dataView;
    }

    private DomainObjectDataProvider<Long, Message, String> createDataProvider() {
        final User user = (User) getDefaultModelObject();

        final Search outboxCriteria = messageService.getOutboxSearchCriteria(user);

        // set up data provider
        final DomainObjectDataProvider<Long, Message, String> messageDataProvider = new DomainObjectDataProvider<Long, Message, String>(
                messageService, outboxCriteria);
        return messageDataProvider;
    }
}
