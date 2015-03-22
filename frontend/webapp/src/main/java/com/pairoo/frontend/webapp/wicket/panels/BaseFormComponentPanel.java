package com.pairoo.frontend.webapp.wicket.panels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.util.EnumUtils;
import com.datazuul.framework.webapp.wicket.components.locale.SortableLanguage;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;

/**
 * @author Ralf Eichinger
 * 
 * @param <T>
 *            model object
 */
public class BaseFormComponentPanel<T extends Object> extends FormComponentPanel<T> {
    private static final long serialVersionUID = 1L;
    static final Logger LOGGER = LoggerFactory.getLogger(BaseFormComponentPanel.class);

    public BaseFormComponentPanel(final String id) {
	super(id);
    }

    public BaseFormComponentPanel(final String id, final IModel<T> model) {
	super(id, model);
    }

    protected FormComponentLabel createFormComponentLabel(final String id, final FormComponent<?> formComponent) {
	return new SimpleFormComponentLabel(id, formComponent);
    }

    protected List<Country> sortCountries(final Locale currentLocale, final List<Country> countries) {
	final Map<String, Country> unsortedMap = new HashMap<String, Country>();
	// put keys and values........
	for (final Country country : countries) {
	    unsortedMap.put(getString(EnumUtils.getEnumKey(country)), country);
	}
	final Map<String, Country> sortedMap = new TreeMap<String, Country>(unsortedMap);

	final Collection<Country> sortedCountries = sortedMap.values();
	return new ArrayList<Country>(sortedCountries);
    }

    public List<Language> sortLanguages(ArrayList<Language> languages) {
	return sortLanguages(languages.toArray(new Language[] {}));
    }

    private List<Language> sortLanguages(final Language[] languages) {
	final ArrayList<SortableLanguage> sortableChoicesList = new ArrayList<SortableLanguage>();
	for (int i = 0; i < languages.length; i++) {
	    final Language language = languages[i];
	    String label = getLocalizer().getString(EnumUtils.getEnumKey(language), this, null, language.getLocale(),
		    null, null);

	    final SortableLanguage sLanguage = new SortableLanguage(language, label);
	    sortableChoicesList.add(sLanguage);
	}
	Collections.sort(sortableChoicesList);
	Collections.reverse(sortableChoicesList);

	final List<Language> sortedChoicesList = new ArrayList<Language>();
	for (final SortableLanguage sLanguage : sortableChoicesList) {
	    sortedChoicesList.add(0, sLanguage.getLanguage());
	}
	return sortedChoicesList;
    }

    public Language getPreferredLanguage() {
	final WicketWebSession session = (WicketWebSession) getSession();
	return session.getSessionLanguage();
    }

    public User getUser() {
	final WicketWebSession session = (WicketWebSession) getSession();
	return session.getUser();
    }

    /**
     * Hint: never use this.getClass() when calling this method. It will not
     * work.
     * 
     * @param panelClass
     *            class of page
     */
    protected void logEnter(final Class<? extends BaseFormComponentPanel<T>> panelClass) {
	logEnter(panelClass.getSimpleName());
    }

    private void logEnter(final String className) {
	if (className != null && !className.isEmpty()) {
	    final WicketWebSession session = (WicketWebSession) getSession();
	    String username = "(not logged in)";
	    if (session.isAuthenticated()) {
		username = session.getUser().getUserAccount().getUsername();
	    }
	    LOGGER.info("'{}'/'{}' ENTERING panel '{}'", new Object[] { session.getId(), username, className });
	} else {
	    LOGGER.info("ENTERING unknown panel");
	}
    }
}
