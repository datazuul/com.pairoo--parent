package com.pairoo.business.services.impl;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.pairoo.business.api.PersonProfileService;
import com.pairoo.domain.PersonProfile;
import com.pairoo.domain.enums.Starsign;

/**
 * @author Ralf Eichinger
 */
public class PersonProfileServiceImplTest {

    private PersonProfileService personProfileService;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        personProfileService = new PersonProfileServiceImpl();
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testGetStarsign() {
        // TODO more tests and replace Date with something not deprecated...
        Starsign starsign = PersonProfile.getStarsign(new Date(70, 12 - 1, 8));
        Assert.assertEquals(Starsign.SAGITTARIUS, starsign);
    }
}
