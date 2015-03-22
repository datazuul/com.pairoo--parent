package com.pairoo.backend;

import java.util.Locale;

public class LocalizedStrings extends com.datazuul.framework.i18n.LocalizedStrings {
    private static final String BASENAME = LocalizedStrings.class.getName();

    // translation keys

    public static String get(final String key, final Locale locale) {
	return LocalizedStrings.get(key, BASENAME, locale);
    }
}