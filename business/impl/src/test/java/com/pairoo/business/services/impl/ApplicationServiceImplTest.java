package com.pairoo.business.services.impl;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.datazuul.framework.domain.Language;
import com.pairoo.business.api.ApplicationService;

public class ApplicationServiceImplTest {
    ApplicationService service = null;

    @Before
    public void onBefore() {
	service = new ApplicationServiceImpl();
    }

    @Test
    public void testGetLocaleForTLD() {
	Assert.assertEquals(Locale.GERMAN, service.getLocaleForTLD("at"));
	Assert.assertEquals(Locale.GERMAN, service.getLocaleForTLD("ch"));
	Assert.assertEquals(Locale.ENGLISH, service.getLocaleForTLD("com"));
	Assert.assertEquals(Locale.GERMAN, service.getLocaleForTLD("de"));
	Assert.assertEquals(Language.SPANISH.getLocale(), service.getLocaleForTLD("es"));
	Assert.assertEquals(Locale.FRENCH, service.getLocaleForTLD("fr"));
	Assert.assertEquals(Locale.ITALIAN, service.getLocaleForTLD("it"));
	Assert.assertEquals(Locale.ENGLISH, service.getLocaleForTLD("uk"));
    }
}
