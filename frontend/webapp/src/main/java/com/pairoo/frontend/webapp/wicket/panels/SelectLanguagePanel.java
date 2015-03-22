package com.pairoo.frontend.webapp.wicket.panels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.util.EnumUtils;
import com.datazuul.framework.webapp.wicket.components.locale.SortableLocale;

/**
 * @author Ralf Eichinger
 */
public class SelectLanguagePanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectLanguagePanel.class);

    public SelectLanguagePanel(final String id, final List<Language> languages) {
	super(id);

	final PropertyModel<Locale> pm = new PropertyModel<Locale>(SelectLanguagePanel.this, "locale");
	// set the model that gets the current locale, and that is used for
	// updating the current locale to property 'locale' of FormInput
	setDefaultModel(pm);

	setOutputMarkupId(true);

	final List<Locale> sortedChoicesList = sortLocales(getLocale(), languages);
	final LoadableDetachableModel<List<Locale>> localeList = new LoadableDetachableModel<List<Locale>>() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected List<Locale> load() {
		return getLocales(languages);
	    }

	    private List<Locale> getLocales(final List<Language> availableTranslationLanguages) {
		final List<Locale> locales = new ArrayList<Locale>();
		final Locale currentLocale = getLocale();
		for (final Language language : availableTranslationLanguages) {
		    final Locale languageLocale = language.getLocale();
		    // do not filter current locale from list
		    locales.add(languageLocale);
		}
		return locales;
		// return sortLocales(currentLocale, locales);
	    }

	    private List<Locale> sortLocales(final Locale currentLocale, final List<Locale> locales) {
		final List<Locale> sortedLocales = new ArrayList<Locale>();

		final ArrayList<SortableLocale> sortableChoicesList = new ArrayList<SortableLocale>();
		for (final Locale locale : locales) {
		    // if (currentLocale != locale) {
		    Language language = Language.getLanguage(locale);
		    String label = getLocalizer().getString(EnumUtils.getEnumKey(language), SelectLanguagePanel.this,
			    null, locale, null, null);
		    final SortableLocale sLocale = new SortableLocale(locale, label);
		    sortableChoicesList.add(sLocale);
		}
		Collections.sort(sortableChoicesList);

		for (final SortableLocale sortableLocale : sortableChoicesList) {
		    sortedLocales.add(sortableLocale.getLocale());
		}
		return sortedLocales;
	    }
	};
	final ListView<Locale> lv = new ListView<Locale>("lv", localeList) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(final ListItem<Locale> item) {
		final Locale itemLocale = item.getModelObject();
		String country = itemLocale.getCountry();
		if (country == null || country.length() == 0) {
		    country = itemLocale.getLanguage();
		}
		final String display = itemLocale.getDisplayName(itemLocale);

		final Link<Void> link = new Link<Void>("link") {
		    private static final long serialVersionUID = 1L;

		    @Override
		    public void onClick() {
			setLocale(itemLocale);
			setResponsePage(getApplication().getHomePage());
		    }
		};
		link.add(new AttributeModifier("title", display));
		item.add(link);

		final Label label = new Label("label", display);
		label.setRenderBodyOnly(true);
		link.add(label);
	    }
	};
	lv.setOutputMarkupId(true);
	add(lv);

	// add(new LocaleDropDownChoice("localeSelect", availableLocales));
    }

    private List<Locale> sortLocales(final Locale currentLocale, final List<Language> languages) {
	final List<Locale> sortedLocales = new ArrayList<Locale>();

	final ArrayList<SortableLocale> sortableChoicesList = new ArrayList<SortableLocale>();
	for (final Language language : languages) {
	    final Locale locale = language.getLocale();
	    // if (currentLocale != locale) {
	    String label = getLocalizer().getString(EnumUtils.getEnumKey(language), this, null, locale, null, null);
	    final SortableLocale sLocale = new SortableLocale(locale, label);
	    sortableChoicesList.add(sLocale);
	    // }
	}
	Collections.sort(sortableChoicesList);

	for (final SortableLocale sortableLocale : sortableChoicesList) {
	    sortedLocales.add(sortableLocale.getLocale());
	}
	return sortedLocales;
    }

    /**
     * @param locale
     *            the locale to set
     */
    public void setLocale(final Locale locale) {
	getSession().setLocale(locale);
	LOGGER.info("Locale changed to: {}", getLocale());
    }
}
