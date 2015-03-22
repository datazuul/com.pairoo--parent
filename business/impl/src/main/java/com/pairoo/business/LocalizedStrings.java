package com.pairoo.business;

import java.util.Locale;

public class LocalizedStrings extends com.datazuul.framework.i18n.LocalizedStrings {
    private static final String BASENAME = LocalizedStrings.class.getName();

    // translation keys
    public static final String EMAIL_ADDRESS_PAIROO = "email_address_pairoo";
    public static final String EMAIL_ADDRESS_SUPPORT = "email_address_support";
    public static final String EMAIL_USERPROFILE_VISITED_SUBJECT = "email_userprofile_visited_subject";
    public static final String REGISTRATION_CONFIRMATION_SUBJECT = "registration_confirmation_subject";
    public static final String RESET_PASSWORD_SUBJECT = "reset_password_subject";
    public static final String SUBJECT_REPORT_PROFILE = "subject_report_profile";
    public static final String SUBSCRIPTION_FUNCTIONALITY_ACTIVE = "subscription.functionality.active";
    public static final String SUBSCRIPTION_FUNCTIONALITY_INACTIVE = "subscription.functionality.inactive";
    public static final String TRANSACTION_NARRATIVE_TEXT = "transaction_narrative_text";

    public static String get(final String key, final Locale locale) {
	return LocalizedStrings.get(key, BASENAME, locale);
    }
}
