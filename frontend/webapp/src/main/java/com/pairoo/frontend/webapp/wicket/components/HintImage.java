package com.pairoo.frontend.webapp.wicket.components;

import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

//import com.visural.wicket.behavior.beautytips.BeautyTipBehavior;

/**
 * @author Ralf Eichinger
 */
public class HintImage extends ContextImage {
    private static final long serialVersionUID = 1L;

    /**
     * @param id
     * @param contextRelativePath
     */
    public HintImage(final String id, final StringResourceModel hintText) {
	super(id, ContextImageConstants.HINT);
//	add(new BeautyTipBehavior(hintText));
    }

    public HintImage(final String id, final ResourceModel hintText) {
	super(id, ContextImageConstants.HINT);
//	add(new BeautyTipBehavior(hintText));
    }
}
