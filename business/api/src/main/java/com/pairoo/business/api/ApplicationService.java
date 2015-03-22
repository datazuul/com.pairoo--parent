package com.pairoo.business.api;

import java.util.List;
import java.util.Locale;

import com.datazuul.framework.domain.Language;

/**
 * Service providing application specific functions, e.g. business driven global
 * settings like supported translation languages.
 * 
 * @author Ralf Eichinger
 */
public interface ApplicationService {
    public static final String BEAN_ID = "applicationService";

    public List<Language> getAvailableTranslationLanguages();

    public Language getLanguage(Locale locale);

    public Locale getLocaleForTLD(String topLevelDomain);

    public Locale getLocaleForLanguage(String preferredLanguage);
}
