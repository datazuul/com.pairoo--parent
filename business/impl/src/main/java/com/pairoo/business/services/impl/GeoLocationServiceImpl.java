package com.pairoo.business.services.impl;

import java.util.List;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.GeoLocation;
import com.datazuul.framework.domain.geo.Subdivision;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.pairoo.backend.dao.GeoLocationDao;
import com.pairoo.business.api.GeoLocationService;
import com.pairoo.business.api.SubdivisionService;
import javax.measure.quantity.Length;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import org.jscience.physics.amount.Amount;

/**
 * @author ralf
 */
public class GeoLocationServiceImpl extends AbstractDomainObjectServiceImpl<Long, GeoLocation> implements
        GeoLocationService {

    private static double EARTH_RADIUS_KM = 6371.009; // [km] 6378.137
    private final SubdivisionService subdivisionService;

    /**
     * @param dao the data access interface
     */
    public GeoLocationServiceImpl(final GeoLocationDao dao, final SubdivisionService subdivisionService) {
        super(dao);
        this.subdivisionService = subdivisionService;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.pairoo.business.services.GeoLocationService#distance(double,
     * double, double, double, java.lang.String)
     */
    @Override
    public double distance(final double lat1, final double lon1, final double lat2, final double lon2, final String unit) {
        final double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.pairoo.business.api.GeoLocationService#distance(double, double,
     * double, double)
     */
    @Override
    public double distance(final double latitude1, final double longitude1, final double latitude2,
            final double longitude2) {
        final double latitudeSin = Math.sin(Math.toRadians(latitude2 - latitude1) / 2);
        final double longitudeSin = Math.sin(Math.toRadians(longitude2 - longitude1) / 2);
        final double a = latitudeSin * latitudeSin + Math.cos(Math.toRadians(latitude1))
                * Math.cos(Math.toRadians(latitude2)) * longitudeSin * longitudeSin;
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6378137 * c;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.pairoo.business.services.GeoLocationService#deg2rad(double)
     */
    @Override
    public double deg2rad(final double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.pairoo.business.services.GeoLocationService#rad2deg(double)
     */
    @Override
    public double rad2deg(final double rad) {
        return (rad * 180 / Math.PI);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.pairoo.business.api.GeoLocationService#deg2dec(int, int, int)
     */
    @Override
    public double deg2dec(final int degree, final int minutes, final int seconds) {
        final double dec = degree + ((double) minutes / 60) + ((double) seconds / (60 * 60));
        return dec;
    }

    /**
     * see http://xebee.xebia.in/2010/10/28/working-with-geolocations/
     *
     * (non-Javadoc)
     *
     * @see
     * com.pairoo.business.api.GeoLocationService#getExtremePointsFrom(com.datazuul.framework.domain.geo.GeoLocation,
     * java.lang.Double)
     */
    @Override
    public GeoLocation[] getExtremePointsFrom(final GeoLocation centerLocation, final Double distance) {
        final double longDiff = getExtremeLongitudesDiffForPoint(centerLocation, distance);
        final double latDiff = getExtremeLatitudesDiffForPoint(centerLocation, distance);
        GeoLocation p1 = new GeoLocation(centerLocation.getLatitude() - latDiff, centerLocation.getLongitude()
                - longDiff);
        p1 = validatePoint(p1);
        GeoLocation p2 = new GeoLocation(centerLocation.getLatitude() + latDiff, centerLocation.getLongitude()
                + longDiff);
        p2 = validatePoint(p2);

        return new GeoLocation[]{p1, p2};
    }

    /**
     * Returns the difference in degrees of longitude corresponding to the
     * distance from the center point. This distance can be used to find the
     * extreme points.
     *
     * @param p1 geo point
     * @param distance distance in km
     * @return extreme longitudes diff in km
     */
    private double getExtremeLongitudesDiffForPoint(final GeoLocation p1, final double distance) {
        double lat1 = p1.getLatitude();
        lat1 = deg2rad(lat1);
        final double longitudeRadius = Math.cos(lat1) * EARTH_RADIUS_KM;
        double diffLong = (distance / longitudeRadius);
        diffLong = rad2deg(diffLong);
        return diffLong;
    }

    /**
     * Returns the difference in degrees of latitude corresponding to the
     * distance from the center point. This distance can be used to find the
     * extreme points.
     *
     * @param p1 geo point
     * @param distance distanc in km
     * @return extreme latitudes diff in km
     */
    private double getExtremeLatitudesDiffForPoint(final GeoLocation p1, final double distance) {
        final double latitudeRadians = distance / EARTH_RADIUS_KM;
        final double diffLat = rad2deg(latitudeRadians);
        return diffLat;
    }

    /**
     * Validates if the point passed has valid values in degrees i.e. latitude
     * lies between -90 and +90 and the longitude
     *
     * @param point geo location
     * @return corrected geolocation
     */
    private GeoLocation validatePoint(final GeoLocation point) {
        if (point.getLatitude() > 90) {
            point.setLatitude(90 - (point.getLatitude() - 90));
        }
        if (point.getLatitude() < -90) {
            point.setLatitude(-90 - (point.getLatitude() + 90));
        }
        if (point.getLongitude() > 180) {
            point.setLongitude(-180 + (point.getLongitude() - 180));
        }
        if (point.getLongitude() < -180) {
            point.setLongitude(180 + (point.getLongitude() + 180));
        }
        return point;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.pairoo.business.api.GeoLocationService#getBySubdivision(com.pairoo
     * .domain.geo.Subdivision)
     */
    @Override
    public List<GeoLocation> getBySubdivision(final Subdivision subdivision) {
        final Search search = new Search(GeoLocation.class);
        if (subdivision.getParent() == null) {
            search.addFilterEqual("subdivision_biggest", subdivision);
        } else {
            final List<Subdivision> subdivisions = subdivisionService.getAllSmallestRecursively(subdivision);
            search.addFilterIn("subdivision_smallest", subdivisions);
        }
        return dao.search(search);
    }

    @Override
    public GeoLocation getByCountryAndZipcode(final Country country, final String zipcode) {
        if (country == null || Country.ANY.equals(country) || zipcode == null || zipcode.length() == 0) {
            return null;
        }
        final Search search = new Search(GeoLocation.class);
        search.addFilter(Filter.and(Filter.isNotNull("country"), Filter.equal("country", country),
                Filter.isNotNull("zipcode"), Filter.equal("zipcode", zipcode)));
        final List<GeoLocation> list = dao.search(search);
        // FIXME as zipcode may not be unique, workaround is to return first of
        // list... (e.g. "4126" in Argentina)
        // correct solution: provide user a selection of found locations (e.g.
        // with AJAX autocompletion) to
        // choose one from
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<GeoLocation> getByCountryAndZipcodeStart(final Country country, final String zipcodeStart) {
        if (country == null || Country.ANY.equals(country) || zipcodeStart == null || zipcodeStart.length() == 0) {
            return null;
        }
        final String zipcode = zipcodeStart + '%';
        final Search search = new Search(GeoLocation.class);
        search.addFilterEqual("country", country);
        search.addFilterILike("zipcode", zipcode);
        return dao.search(search);
    }

    @Override
    public String distanceInPreferredUnit(GeoLocation geoLocation1, GeoLocation geoLocation2, Unit<Length> preferredUnit) {
        final double distanceInMeter = distance(geoLocation1.getLatitude(),
                geoLocation1.getLongitude(), geoLocation2.getLatitude(), geoLocation2.getLongitude());
        final long distanceKm = Math.round(new Double(distanceInMeter / 1000));
        Amount<Length> distanceInKm = Amount.valueOf(distanceKm, SI.KILOMETER);
        long distanceInPreferredUnit = Math.round(distanceInKm.doubleValue(preferredUnit));
        String distance = String.valueOf(distanceInPreferredUnit) + " " + preferredUnit.toString();
        return distance;
    }
}
