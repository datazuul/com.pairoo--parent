package com.pairoo.frontend.webapp.wicket.panels;

import org.apache.wicket.markup.html.link.Link;

import com.pairoo.frontend.webapp.wicket.pages.GuidePage;
import com.pairoo.frontend.webapp.wicket.pages.ImprintPage;
import com.pairoo.frontend.webapp.wicket.pages.PartnerProgramPage;
import com.pairoo.frontend.webapp.wicket.pages.PrivacyStatementPage;
import com.pairoo.frontend.webapp.wicket.pages.TermsOfUsePage;

/**
 * @author Ralf Eichinger
 */
public class FooterNaviPanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    public FooterNaviPanel(final String id) {
	super(id);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	// add(createCopyrightPageLink());
	add(linkToTermsOfUsePage("lnkTermsOfUse"));
	add(linkToPrivacyStatementPage("lnkPrivacyStatement"));
	add(linkToGuidePage("lnkGuide"));
	// add(createPartnerProgramPageLink());
	add(linkToImprintPage("lnkImprint"));

	// add(new BookmarkablePageLink("lnkAboutUs", AboutUsPage.class));
	// add(new BookmarkablePageLink("lnkContactUs", ContactUsPage.class));
	// add(new BookmarkablePageLink("lnkSuccessStories",
	// TestimonialsPage.class));
	// add(new BookmarkablePageLink("lnkDatingSafety",
	// DatingSafetyPage.class));
    }

    private Link<Void> linkToImprintPage(String id) {
	return new Link<Void>(id) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClick() {
		setResponsePage(new ImprintPage() {
		    private static final long serialVersionUID = 1L;

		    @Override
		    protected void onGoBack() {
			setResponsePage(FooterNaviPanel.this.getPage());
		    }
		});
	    }
	};
    }

    private Link<Void> createPartnerProgramPageLink() {
	return new Link<Void>("lnkPartnerProgram") {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClick() {
		setResponsePage(new PartnerProgramPage() {
		    private static final long serialVersionUID = 1L;

		    @Override
		    protected void onGoBack() {
			setResponsePage(FooterNaviPanel.this.getPage());
		    }
		});
	    }
	};
    }

    private Link<Void> linkToGuidePage(String id) {
	return new Link<Void>(id) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClick() {
		setResponsePage(new GuidePage() {
		    private static final long serialVersionUID = 1L;

		    @Override
		    protected void onGoBack() {
			setResponsePage(FooterNaviPanel.this.getPage());
		    }
		});
	    }
	};
    }

    private Link<Void> linkToPrivacyStatementPage(String id) {
	return new Link<Void>(id) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClick() {
		setResponsePage(new PrivacyStatementPage() {
		    private static final long serialVersionUID = 1L;

		    @Override
		    protected void onGoBack() {
			setResponsePage(FooterNaviPanel.this.getPage());
		    }
		});
	    }
	};
    }

    private Link<Void> linkToTermsOfUsePage(String id) {
	return new Link<Void>(id) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClick() {
		setResponsePage(new TermsOfUsePage() {
		    private static final long serialVersionUID = 1L;

		    @Override
		    protected void onGoBack() {
			setResponsePage(FooterNaviPanel.this.getPage());
		    }
		});
	    }
	};
    }
}
