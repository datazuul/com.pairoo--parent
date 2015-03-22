package com.pairoo.backend.db.hibernate;

import com.datazuul.framework.domain.geo.GeoLocation;
import com.pairoo.backend.dao.search.DaoUserSearchParams;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.search.UserSearchResult;
import java.util.List;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ralf
 */
public class UserDaoImplITTest extends AbstractITCase {

    @Autowired
    protected GeoLocationDaoImpl daoGeoLocation;
    @Autowired
    protected UserDaoImpl dao;

    public UserDaoImplITTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of search method, of class UserDaoImpl.
     */
    @Test
    @Ignore
    public void testSearch() {
        final GeoLocation geolocation = getBean("GeoLocation_unsaved", GeoLocation.class);
        daoGeoLocation.save(geolocation);

        for (int i = 0; i < 10; i++) {
            final User domainObj = getBean("User_unsaved", User.class);
            // make unique fields unique:
            domainObj.setEmail(i + domainObj.getEmail());
            domainObj.getUserProfile().setGeoLocation(geolocation);
            dao.save(domainObj);

            // get the saved domain object
            final User result = dao.find(domainObj.getId());

            Assert.assertNotNull(result);
            Assert.assertEquals(domainObj, result);
        }

        DaoUserSearchParams searchParams = new DaoUserSearchParams();
        long first = 0L;
        long count = 10L;

	// FIXME: Why 0 are returned and not 10?
        final List<UserSearchResult> result = dao.search(searchParams, first, count);

        Assert.assertEquals(10, result.size());
    }

    /**
     * Test of count method, of class UserDaoImpl.
     */
    @Test
    @Ignore
    public void testCount() {
        System.out.println("count");
        DaoUserSearchParams searchParams = null;
        UserDaoImpl instance = new UserDaoImpl();
        long expResult = 0L;
        long result = instance.count(searchParams);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
