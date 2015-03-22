package com.pairoo.frontend.webapp.wicket.pages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.pairoo.frontend.webapp.wicket.components.ContextImageConstants;

/**
 * @author Ralf Eichinger
 */
public abstract class ImprintPage extends BaseInterceptionPage {

    private static final long serialVersionUID = 1L;

    public ImprintPage() {
        super();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(emailSupportImage("emailSupport2"));
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(ImprintPage.class);
    }

    private Component emailSupportImage(String id) {
        IModel<String> pathModel = new Model<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getObject() {
                String contextRelativePath = ContextImageConstants.TEXT_IMAGES_PATH + "/" + getLocale().getLanguage()
                        + "/" + ContextImageConstants.EMAIL_SUPPORT;
                return contextRelativePath;
            }
        };
        Component c = new ContextImage(id, pathModel);
        return c;
    }
}
