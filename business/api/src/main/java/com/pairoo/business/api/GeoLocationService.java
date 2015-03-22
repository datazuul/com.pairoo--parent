package com.pairoo.business.api;

import java.util.List;

import com.datazuul.framework.business.services.DomainObjectService;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.GeoLocation;
import com.datazuul.framework.domain.geo.Subdivision;
import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

public interface GeoLocationService extends DomainObjectService<Long, GeoLocation> {

    public static final String BEAN_ID = "geoLocationService";

    /**
     * Calculates distance between to points.
     *
     * latitude: South is negative longitude: West is negative
     *
     * @param lat1 latitude of point 1 (in decimal degree)
     * @param lon1 longitude of point 1 ( " )
     * @param lat2 latitude of point 2 (in decimal degree)
     * @param lon2 longitude of point 2 ( " )
     * @param unit distance unit ('M'=statute miles, 'K'=kilometers (default),
     * 'N'=nautical miles)
     * @return distance between point 1 and point 2 in given unit
     */
    public double distance(double lat1, double lon1, double lat2, double lon2, String unit);

    public String distanceInPreferredUnit(GeoLocation geoLocation1, GeoLocation geoLocation2, Unit<Length> preferredUnit);

    /**
     * Calculates the great circle distance between two points on the Earth.
     * Uses the Haversine Formula.
     *
     * @param latitude1 Latitude of first location in decimal degrees.
     * @param longitude1 Longitude of first location in decimal degrees.
     * @param latitude2 Latitude of second location in decimal degrees.
     * @param longitude2 Longitude of second location in decimal degrees.
     * @return Distance in meter.
     */
    public double distance(final double latitude1, final double longitude1, final double latitude2,
            final double longitude2);

    /**
     * Returns an array of two extreme points corresponding to center point and
     * the distance from the center point. These extreme points are the points
     * with min latitude and longitude and max latitude and longitude.
     *
     * @param centerLocation location in latitude/longitude
     * @param distance distance radius [km]
     * @return minimum (first) and maximum (second) points
     */
    public GeoLocation[] getExtremePointsFrom(final GeoLocation centerLocation, final Double distance);

    /**
     * Converts decimal degrees to radians
     *
     * @param deg decimal degrees
     * @return radians
     */
    public double deg2rad(double deg);

    /**
     * Converts radians to decimal degrees
     *
     * @param rad radians
     * @return decimal degrees
     */
    public double rad2deg(double rad);

    /**
     * Converts from degrees to decimal degrees
     *
     * @param degree degrees
     * @param minutes minutes
     * @param seconds seconds
     * @return decimal degrees
     */
    public double deg2dec(int degree, int minutes, int seconds);

    /**
     * Find all GeoLocations within the given Subdivision
     *
     * @param subdivision given subdivision
     * @return list of geo locations
     */
    public List<GeoLocation> getBySubdivision(Subdivision subdivision);

    /**
     * Find unique GeoLocation specified by country and (complete) zipcode
     *
     * @param country country
     * @param zipcode complete zipcode
     */
    public GeoLocation getByCountryAndZipcode(Country country, String zipcode);

    /**
     * Find all GeoLocations in country and whose zipcode starts with the given
     * fragment.
     *
     * @param country country
     * @param zipcode zipcode fragment (containing wildcard "*")
     * @return list of found GeoLocations
     */
    public List<GeoLocation> getByCountryAndZipcodeStart(Country country, String zipcodeStart);
}
