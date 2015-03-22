/**
 * 
 */
package com.pairoo.business.services.impl;

import junit.framework.TestCase;

import org.junit.Assert;

/**
 * @author ralf
 * 
 */
public class TestGeoLocationServiceImplTest extends TestCase {

    public void testDistance() throws Exception {
	final GeoLocationServiceImpl impl = new GeoLocationServiceImpl(null, null);

	// 1. check a long distance
	// Distance from Munich to Tokyo is 9393 kilometers or 5837 miles or
	// 5072 nautical miles
	// see http://www.timeanddate.com/worldclock/distance.html
	// Munich: Latitude: 48° 08' North, Longitude: 11° 35' East
	// Tokyo: Latitude: 35° 41' North, Longitude: 139° 42' East
	final double munichLatitude = impl.deg2dec(48, 8, 0);
	final double munichLongitude = impl.deg2dec(11, 35, 0);
	final double tokyoLatitude = impl.deg2dec(35, 41, 0);
	final double tokyoLongitude = impl.deg2dec(139, 42, 0);

	// final double distance = impl.distance(munichLatitude,
	// munichLongitude, tokyoLatitude, tokyoLongitude, "K");
	// distance = 9370.40122898053
	double distance2 = impl.distance(munichLatitude, munichLongitude, tokyoLatitude, tokyoLongitude) / 1000;
	// distance2 = 9381.349600008201 (seems to be better, so continue using
	// this)

	double tolerance = distance2 / 100;
	Assert.assertEquals(9393, distance2, tolerance);

	// 2. check a short distance
	// Distance is 106 kilometers or 66 miles or 57 nautical miles
	// Munich: Latitude: 48° 08' North, Longitude: 11° 35' East
	// Regensburg: Latitude: 49° 01' North, Longitude: 12° 07' East
	final double regensburgLatitude = impl.deg2dec(49, 1, 0);
	final double regensburgLongitude = impl.deg2dec(12, 7, 0);
	distance2 = impl.distance(munichLatitude, munichLongitude, regensburgLatitude, regensburgLongitude) / 1000;
	tolerance = distance2 / 100;
	Assert.assertEquals(106, distance2, tolerance);
    }

}
