package com.pairoo.domain;

import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.GeoLocation;

public class MockUser extends User {
    private static final long serialVersionUID = 1L;

    public MockUser() {
	super();

	// ---------------- default values
	final UserProfile userProfile = new UserProfile();
	// userProfile.setPartnerType(PartnerType.FEMALE);
	// userProfile.setHouseholdType(HouseholdType.DONT_SAY);

	// final LifeStyle lifeStyle = new LifeStyle();
	// lifeStyle.setSmokeType(SmokeType.DONT_SAY);
	// userProfile.setLifeStyle(lifeStyle);

	final GeoLocation geoLocation = new GeoLocation();
	geoLocation.setCountry(Country.GERMANY);
	userProfile.setGeoLocation(geoLocation);
	setUserProfile(userProfile);

	final SearchProfile searchProfile = new SearchProfile();
	// searchProfile.setPartnerType(PartnerType.MALE);
	// searchProfile.getEthnicities().add(Ethnicity.ANY);
	searchProfile.setMinAge(32);
	searchProfile.setMaxAge(38);
	setSearchProfile(searchProfile);
    }
}
