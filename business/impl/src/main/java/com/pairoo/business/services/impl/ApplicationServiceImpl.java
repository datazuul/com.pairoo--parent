package com.pairoo.business.services.impl;

import java.util.List;
import java.util.Locale;

import com.datazuul.framework.domain.Language;
import com.pairoo.business.api.ApplicationService;
import java.util.Arrays;

/**
 * @author Ralf Eichinger
 */
public class ApplicationServiceImpl implements ApplicationService {
    private final static Language[] AVAILABLE_TRANSLATION_LANGUAGES = new Language[] { Language.ENGLISH,
	    Language.FRENCH, Language.GERMAN, Language.ITALIAN, Language.SPANISH };

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.pairoo.business.api.ApplicationService#getAvailableTranslationLanguages
     * ()
     */
    @Override
    public List<Language> getAvailableTranslationLanguages() {
        List<Language> languages = Arrays.asList(AVAILABLE_TRANSLATION_LANGUAGES);
	return languages;
    }

    @Override
    public Language getLanguage(final Locale locale) {
	Language result = Language.getLanguage(locale);
	if (result == null) {
	    result = Language.ENGLISH;
	}
	return result;
    }

    @Override
    public Locale getLocaleForTLD(String topLevelDomain) {
	Locale defaultLocale = Locale.ENGLISH;
	
	if (topLevelDomain == null) {
	    return defaultLocale;
	} else {
	    topLevelDomain = topLevelDomain.toLowerCase();
	}
	Locale result = null;
	for (Language language : AVAILABLE_TRANSLATION_LANGUAGES) {
	    if (language.getLocale().getLanguage().equalsIgnoreCase(topLevelDomain)) {
		result = language.getLocale();
	    }
	}
	if (result == null) {
	    // some TLDs have different locale:
	    if ("ch".equals(topLevelDomain) || "at".equals(topLevelDomain)) {
		result = Locale.GERMAN;
	    }
	}
	if (result == null) {
	    result = defaultLocale; // default
	}
	return result;
    }

    @Override
    public Locale getLocaleForLanguage(String preferredLanguage) {
        Locale defaultLocale = Locale.ENGLISH;
        if (preferredLanguage == null) {
	    return defaultLocale;
	} else {
	    preferredLanguage = preferredLanguage.toLowerCase();
	}
        Locale result = null;
	for (Language language : AVAILABLE_TRANSLATION_LANGUAGES) {
	    if (language.getLocale().getLanguage().equalsIgnoreCase(preferredLanguage)) {
		result = language.getLocale();
	    }
	}
        if (result == null) {
	    result = defaultLocale; // default
	}
	return result;
    }
}
