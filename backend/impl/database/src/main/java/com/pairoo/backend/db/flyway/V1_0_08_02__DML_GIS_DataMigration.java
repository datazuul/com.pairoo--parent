package com.pairoo.backend.db.flyway;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.datazuul.framework.domain.geo.Continent;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.GeoLocation;
import com.datazuul.framework.domain.geo.Subdivision;
import com.googlecode.flyway.core.api.migration.spring.SpringJdbcMigration;
import com.pairoo.domain.geo.GeoArea;

/**
 * In the old schema UserProfile and SearchProfile share the table GEOLOCATION
 * to save the living location of the UserProfile or the search location of the
 * SearchProfile. This has now changed to:
 * <ul>
 * <li>UserProfile: continues to use GEOLOCATION but now foreign key id moves
 * from GEOLOCATION column to GEOLOCATION_ID column.<br/>
 * UserProfile has to link to the prefilled GeoLocation that has the zipcode the
 * old GeoLocation had before. So GeoLocation-ID has to be updated to ID of
 * prefilled data. To ensure that the UserProfile links to a geolocation with
 * same zipcode, the old zipcode and country have to be cached before data in
 * geolocation is deleted.</li>
 * <li>SearchProfile: now uses GEOAREA referenced by GEOAREA_ID.<br/>
 * Migration is easier than for UserProfile: Insert new GeoArea with current
 * zipcode to zipcodeStartsWith and country to country. Update GEOAREA_ID in
 * SEARCHPROFILE. After GeoLocation data is filled, try to lookup GeoLocation
 * for GeoAreas with zipcodeStartsWith-length == 5 and change accordingly.</li>
 * </ul>
 * 
 * <p>
 * This leads to the following migration steps:
 * <ol>
 * <li>copy GEOLOCATION data for SEARCHPROFILEs to GEOAREA</li>
 * <li>cache USERPROFILE's zipcode and country for later lookup</li>
 * <li>delete all data in GEOLOCATION</li>
 * <li>fill data from file into GEOLOCATION and SUBDIVISION</li>
 * <li>look up cached zipcode/country of USERPROFILE in new data and update
 * GEOLOCATION_ID in USERPROFILE accordingly (if not found use a default
 * GEOLOCATION (81245 Germany).</li>
 * <li>look up stored ZIPCODE_START of GEOAREA in new GEOLOCATIONs. If found,
 * update GEOAREA's GEOLOCATION_ID value and delete ZIPCODE_START.</li>
 * </ol>
 * 
 * Prerequisites: before this migration all constraints are dropped, new tables
 * and columns are created. GeoLocation-data has to be prefilled.<br/>
 * Postrequisites: tables and columns are dropped after this migration and
 * constraints are added afterwards.
 * 
 * @author Ralf Eichinger
 */
public class V1_0_08_02__DML_GIS_DataMigration implements SpringJdbcMigration {
    private static final Logger LOGGER = LoggerFactory.getLogger(V1_0_08_02__DML_GIS_DataMigration.class);

    private String dataFilename;
    private JdbcTemplate jdbcTemplate;
    private final HashMap<String, Country> countryCodeToCountry = new HashMap<String, Country>();
    private final HashMap<Country, Continent> countryToContinent = new HashMap<Country, Continent>();
    private long geoAreaCounter = 1;
    private long geoLocationCounter = 1;
    private long subdivisionCounter = 1;

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {
	this.jdbcTemplate = jdbcTemplate;

	prepareUtils();

	migrateSearchProfile();
	final HashMap<Long, GeoLocation> userProfileGeoLocationCache = cacheUserProfilesZipcodeAndCountry();
	deleteGeolocationData();
	deleteSubdivisionData();
	fillGeoLocation();
	migrateUserProfile(userProfileGeoLocationCache);
	updateGeoArea();
    }

    private void deleteSubdivisionData() {
	LOGGER.info("delete from SUBDIVISION");
	final int rows = this.jdbcTemplate.update("delete from SUBDIVISION");
	LOGGER.info("deleted {} rows from SUBDIVISION", rows);
    }

    /**
     * look up stored ZIPCODE_START of GEOAREA in new GEOLOCATIONs. If found,
     * update GEOAREA's GEOLOCATION_ID value and delete ZIPCODE_START.
     * 
     * create table GEOAREA (ID bigint not null, CONTINENT varchar(255), COUNTRY
     * varchar(255), GEOLOCATION_ID bigint, MAXDISTANCE varchar(255),
     * SUBDIVISION_ID bigint, ZIPCODE_START varchar(255), primary key (ID));
     */
    private void updateGeoArea() {
	LOGGER.info("select ID, COUNTRY, ZIPCODE_START from GEOAREA");
	final List<GeoArea> geoAreas = this.jdbcTemplate.query("select ID, COUNTRY, ZIPCODE_START from GEOAREA",
		new RowMapper() {
		    @Override
		    public Object mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			final Long id = rs.getLong("ID");
			final String countryName = rs.getString("COUNTRY");
			final Country country = Enum.valueOf(Country.class, countryName);
			final String zipcodeStart = rs.getString("ZIPCODE_START");
			// copy data to a GeoLocation object
			final GeoArea geoArea = new GeoArea();
			geoArea.setId(id);
			geoArea.setZipcodeStartsWith(zipcodeStart);
			geoArea.setCountry(country);
			return geoArea;
		    }
		});
	for (final GeoArea geoArea : geoAreas) {
	    if (geoArea.getZipcodeStartsWith() != null && !geoArea.getZipcodeStartsWith().isEmpty()) {
		Long geoLocationId = null;
		final String countryName = geoArea.getCountry().name();
		final String zipcodeStartsWith = geoArea.getZipcodeStartsWith();
		final Long geoAreaId = geoArea.getId();
		try {
		    final Object[] params = new Object[] { countryName, zipcodeStartsWith };
		    LOGGER.info("select ID from GEOLOCATION where COUNTRY={} and ZIPCODE={}", params);
		    geoLocationId = this.jdbcTemplate.queryForLong(
			    "select ID from GEOLOCATION where COUNTRY=? and ZIPCODE=?", params);
		} catch (final Exception e) {

		}
		if (geoLocationId != null) {
		    final Object[] params = new Object[] { geoLocationId, geoAreaId };
		    LOGGER.info("update GEOAREA set COUNTRY=NULL, GEOLOCATION_ID={} where ID={}", params);
		    // it is a dedicated geolocation (with maxdistance)
		    this.jdbcTemplate.update("update GEOAREA set COUNTRY=NULL, GEOLOCATION_ID=? where ID=?", params);
		} else {
		    // it is a zipcode range where max distance makes no sense
		    // if (!zipcodeStartsWith.contains("*")) {
		    // zipcodeStartsWith = zipcodeStartsWith + "*";
		    // }
		    final Object[] params = new Object[] { zipcodeStartsWith, geoAreaId };
		    LOGGER.info("update GEOAREA set MAXDISTANCE=NULL, ZIPCODE_START={} where ID={}", params);
		    this.jdbcTemplate.update("update GEOAREA set MAXDISTANCE=NULL, ZIPCODE_START=? where ID=?", params);
		}
	    }
	}

    }

    /**
     * look up cached zipcode/country of USERPROFILE in new data and update
     * GEOLOCATION_ID in USERPROFILE accordingly (if not found delete
     * geolocation id).</li>
     * 
     * @param userProfileGeoLocationCache
     *            cached geolocations of userprofiles
     */
    private void migrateUserProfile(final HashMap<Long, GeoLocation> userProfileGeoLocationCache) {
	final Set<Long> keySet = userProfileGeoLocationCache.keySet();
	for (final Long key : keySet) {
	    final GeoLocation geoLocation = userProfileGeoLocationCache.get(key);
	    if (geoLocation.getZipcode() != null && !geoLocation.getZipcode().isEmpty()) {
		Long geoLocationId = null;
		try {
		    final Object[] params = new Object[] { geoLocation.getCountry().name(), geoLocation.getZipcode() };
		    LOGGER.info("select ID from GEOLOCATION where COUNTRY={} and ZIPCODE={}", params);
		    geoLocationId = this.jdbcTemplate.queryForLong(
			    "select ID from GEOLOCATION where COUNTRY=? and ZIPCODE=?", params);
		} catch (final Exception e) {
		    // nothing found
		}
		if (geoLocationId != null) {
		    final Object[] params = new Object[] { geoLocationId, key };
		    LOGGER.info("update USERPROFILE set GEOLOCATION_ID={} where ID={}", params);
		    this.jdbcTemplate.update("update USERPROFILE set GEOLOCATION_ID=? where ID=?", params);
		} else {
		    final Object[] params = new Object[] { key };
		    LOGGER.info("update USERPROFILE set GEOLOCATION_ID=NULL where ID={}", params);
		    this.jdbcTemplate.update("update USERPROFILE set GEOLOCATION_ID=NULL where ID=?", params);
		}
	    }
	}
    }

    private void prepareUtils() {
	// fill countryCodeToCountry
	final Country[] countries = Country.values();
	for (final Country country : countries) {
	    if (country != Country.ANY) {
		countryCodeToCountry.put(country.getCountryCode(), country);
	    }
	}
	// fill countryToContinent
	final Continent[] continents = Continent.values();
	for (final Continent continent : continents) {
	    if (continent != Continent.ANY) {
		final Country[] countries2 = continent.getCountries();
		for (final Country country : countries2) {
		    countryToContinent.put(country, continent);
		}
	    }
	}
    }

    private void fillGeoLocation() throws Exception {
	LOGGER.info("starting to insert data from file {}", "classpath:com/pairoo/backend/db/flyway/"
		+ getDataFilename());
	final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	final Resource resource = resolver.getResource("classpath:com/pairoo/backend/db/flyway/" + getDataFilename());
	final InputStream inputStream = resource.getInputStream();
	final BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

	// use tab as data delimiter in line
	final Pattern pattern = Pattern.compile("\t");
	long l = 0;
	while (bufReader.ready()) {
	    l++;
	    final String aLine = bufReader.readLine();
	    LOGGER.info("Line {}: {}", l, aLine);

	    processLine(aLine, pattern);
	}

    }

    /**
     * <p>
     * The data format is tab-delimited text in utf8 encoding, with the
     * following fields :
     * <p>
     * 
     * <pre>
     * [0] country code : iso country code, 2 
     * [1] characters postal code : varchar(20)
     * [2] place name : varchar(180)
     * [3] admin name1 : 1. order subdivision (state) varchar(100)
     * [4] admin code1 : 1. order subdivision (state) varchar(20)
     * [5] admin name2 : 2. order subdivision (county/province) varchar(100)
     * [6] admin code2 : 2. order subdivision (county/province) varchar(20)
     * [7] admin name3 : 3. order subdivision (community) varchar(100)
     * [8] admin code3 : 3. order subdivision (community) varchar(20)
     * [9] latitude : estimated latitude (wgs84)
     * [10] longitude : estimated longitude (wgs84)
     * [11] accuracy : accuracy of lat/lng from 1=estimated to 6=centroid
     * </pre>
     * 
     * GeoLocation object:
     * 
     * <pre>
     * private String name;
     * private String zipcode;
     * private Double latitude; // North/South
     * private Double longitude; // West/East
     * private Integer population; // of 2010
     * private Country country;
     * private Continent continent;
     * private Subdivision subdivision_biggest;
     * private Subdivision subdivision_smallest;
     * </pre>
     * 
     * @param aLine
     *            data line
     * @throws Exception
     *             exception happened
     */
    private void processLine(final String aLine, final Pattern pattern) throws Exception {
	try {
	    final String[] segs = StringUtils.splitByWholeSeparatorPreserveAllTokens(aLine, "\t");
	    // final String[] segs = pattern.split(aLine);

	    // temporary data container
	    final GeoLocation geoLocation = new GeoLocation();

	    // country
	    // [0] country code : iso country code, 2
	    final String countryCode = segs[0];
	    Country country = null;
	    if (countryCode != null) {
		country = countryCodeToCountry.get(countryCode);
		geoLocation.setCountry(country);
	    }
	    if (country == null) {
		throw new Exception("LINE: " + aLine + " no Country found for code " + countryCode);
	    }

	    // continent
	    final Continent continent = countryToContinent.get(country);
	    geoLocation.setContinent(continent);
	    if (continent == null) {
		throw new Exception("LINE: " + aLine + " no Continent set for country " + country.name());
	    }

	    // postal code / zipcode
	    // [1] characters postal code : varchar(20)
	    final String postalCode = segs[1];
	    geoLocation.setZipcode(postalCode);

	    // place name
	    // [2] place name : varchar(180)
	    final String name = segs[2];
	    geoLocation.setName(name);

	    // subdivisions
	    Subdivision subdivision1, subdivision2, subdivision3;

	    // biggest subdivision
	    // [3] admin name1 : 1. order subdivision (state) varchar(100)
	    final String admin1Name = segs[3];
	    if (admin1Name != null && !admin1Name.isEmpty()) {
		subdivision1 = new Subdivision();
		subdivision1.setCountry(country);
		subdivision1.setName(admin1Name);

		// * [4] admin code1 : 1. order subdivision (state) varchar(20)
		final String admin1Code = segs[4];
		subdivision1.setCode(admin1Code);

		geoLocation.setSubdivision_biggest(subdivision1);
		final Long idOfSubdivision1 = insertSubdivision(subdivision1, null);
		subdivision1.setId(idOfSubdivision1);

		// * [5] admin name2 : 2. order subdivision (county/province)
		// varchar(100)
		final String admin2Name = segs[5];
		if (admin2Name != null && !admin2Name.isEmpty()) {
		    subdivision2 = new Subdivision();
		    subdivision2.setCountry(country);
		    subdivision2.setName(admin2Name);

		    // * [6] admin code2 : 2. order subdivision
		    // (county/province) varchar(20)
		    final String admin2Code = segs[6];
		    subdivision2.setCode(admin2Code);

		    geoLocation.setSubdivision_smallest(subdivision2);
		    final Long idOfSubdivision2 = insertSubdivision(subdivision2, idOfSubdivision1);
		    subdivision2.setId(idOfSubdivision2);

		    // * [7] admin name3 : 3. order subdivision (community)
		    // varchar(100)
		    final String admin3Name = segs[7];
		    if (admin3Name != null && !admin3Name.isEmpty()) {
			subdivision3 = new Subdivision();
			subdivision3.setCountry(country);
			subdivision3.setName(admin3Name);
			// * [8] admin code3 : 3. order subdivision (community)
			// varchar(20)
			final String admin3Code = segs[8];
			subdivision3.setCode(admin3Code);

			geoLocation.setSubdivision_smallest(subdivision3);
			final Long idOfSubdivision3 = insertSubdivision(subdivision3, idOfSubdivision2);
			subdivision3.setId(idOfSubdivision3);
		    }

		}

	    }

	    // * [9] latitude : estimated latitude (wgs84)
	    final String latitude = segs[9];
	    if (latitude != null && !latitude.isEmpty()) {
		geoLocation.setLatitude(Double.parseDouble(latitude));
	    }

	    // * [10] longitude : estimated longitude (wgs84)
	    final String longitude = segs[10];
	    if (longitude != null && !longitude.isEmpty()) {
		geoLocation.setLongitude(Double.parseDouble(longitude));
	    }

	    // * [11] accuracy : accuracy of lat/lng from 1=estimated to
	    // 6=centroid
	    // create table GEOLOCATION (ID bigint not null, NAME varchar(255),
	    // ZIPCODE varchar(255), LATITUDE double, LONGITUDE double,
	    // POPULATION integer, COUNTRY varchar(255), SUBDIVISION_BIGGEST_ID
	    // bigint, SUBDIVISION_SMALLEST_ID bigint, CONTINENT varchar(255),
	    // primary key (ID));
	    Long subdivisionBiggestId = null;
	    if (geoLocation.getSubdivision_biggest() != null) {
		subdivisionBiggestId = geoLocation.getSubdivision_biggest().getId();
	    }
	    Long subdivisionSmallestId = null;
	    if (geoLocation.getSubdivision_smallest() != null) {
		subdivisionSmallestId = geoLocation.getSubdivision_smallest().getId();
	    }
	    final Object[] params = new Object[] { geoLocationCounter, geoLocation.getName(), geoLocation.getZipcode(),
		    geoLocation.getLatitude(), geoLocation.getLongitude(), geoLocation.getCountry().name(),
		    subdivisionBiggestId, subdivisionSmallestId, geoLocation.getContinent().name() };
	    LOGGER.info(
		    "insert into GEOLOCATION(ID, NAME, ZIPCODE, LATITUDE, LONGITUDE, COUNTRY, SUBDIVISION_BIGGEST_ID, SUBDIVISION_SMALLEST_ID, CONTINENT) values({},{},{},{},{},{},{},{},{})",
		    params);
	    this.jdbcTemplate
		    .update("insert into GEOLOCATION(ID, NAME, ZIPCODE, LATITUDE, LONGITUDE, COUNTRY, SUBDIVISION_BIGGEST_ID, SUBDIVISION_SMALLEST_ID, CONTINENT) values(?,?,?,?,?,?,?,?,?)",
			    params);
	    geoLocationCounter++;
	} catch (final Exception e) {
	    e.printStackTrace();
	    throw e;
	}
    }

    private Long insertSubdivision(final Subdivision subdivision, final Long idOfParentSubdivision) throws Exception {
	final String subdivisionCode = subdivision.getCode();
	final String subdivisionName = subdivision.getName();
	final String country = subdivision.getCountry().name();
	final Object[] params = new Object[] { subdivisionCode, subdivisionName, country };
	LOGGER.info("select ID from SUBDIVISION where CODE={} and NAME={} and COUNTRY={}", params);
	// check if subdivision already exists (sql results in a
	// preparedstatement)
	final List<Long> foundSubdivisionIds = this.jdbcTemplate.query(
		"select ID from SUBDIVISION where CODE=? and NAME=? and COUNTRY=?", params, new RowMapper() {
		    @Override
		    public Object mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			final Long id = rs.getLong("ID");
			return id;
		    }
		});
	if (foundSubdivisionIds == null || foundSubdivisionIds.isEmpty()) {
	    // insert subdivision into database
	    if (idOfParentSubdivision == null) {
		final Object[] params2 = new Object[] { subdivisionCounter, subdivisionCode, subdivisionName, country };
		LOGGER.info("insert into SUBDIVISION(ID, CODE, NAME, COUNTRY) values({},{},{},{})", params2);
		this.jdbcTemplate.update("insert into SUBDIVISION(ID, CODE, NAME, COUNTRY) values(?,?,?,?)", params2);
	    } else {
		final Object[] params2 = new Object[] { subdivisionCounter, subdivisionCode, subdivisionName, country,
			idOfParentSubdivision };
		LOGGER.info("insert into SUBDIVISION(ID, CODE, NAME, COUNTRY, PARENT_ID) values({},{},{},{},{})",
			params2);
		this.jdbcTemplate.update(
			"insert into SUBDIVISION(ID, CODE, NAME, COUNTRY, PARENT_ID) values(?,?,?,?,?)", params2);
	    }
	    subdivisionCounter++;
	    return subdivisionCounter - 1;
	} else {
	    if (foundSubdivisionIds.size() > 1) {
		LOGGER.error(foundSubdivisionIds.toString());
		throw new Exception("found more than one identical subdivision");
	    }
	    final Long foundSubdivisionId = foundSubdivisionIds.get(0);
	    // should only be one
	    return foundSubdivisionId;
	}
    }

    private void deleteGeolocationData() {
	LOGGER.info("delete from GEOLOCATION");
	final int rows = this.jdbcTemplate.update("delete from GEOLOCATION");

	LOGGER.info("deleted {} rows from GEOLOCATION", rows);
    }

    private HashMap<Long, GeoLocation> cacheUserProfilesZipcodeAndCountry() {
	final HashMap<Long, GeoLocation> userprofileAndGeolocationCache = new HashMap<Long, GeoLocation>();

	// get all userprofiles (to get GEOLOCATION ids identifying
	// USERPROFILE-geolocations)
	final HashMap<Long, Long> userprofileAndGeolocationMap = new HashMap<Long, Long>();
	LOGGER.info("select ID, GEOLOCATION from USERPROFILE");
	this.jdbcTemplate.query("select ID, GEOLOCATION from USERPROFILE", new RowMapper() {
	    @Override
	    public Object mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final Long id = rs.getLong("ID");
		final Long geoLocationId = rs.getLong("GEOLOCATION");
		userprofileAndGeolocationMap.put(id, geoLocationId);
		return null;
	    }
	});

	// get ZIPCODE and COUNTRY from GEOLOCATION for each USERPROFILE and
	// cache it
	final Set<Long> keySet = userprofileAndGeolocationMap.keySet();
	for (final Long key : keySet) {
	    // get ZIPCODE and COUNTRY from GEOLOCATION
	    final Long geoLocationId = userprofileAndGeolocationMap.get(key);
	    final Object[] params = new Object[] { geoLocationId };
	    LOGGER.info("select ZIPCODE, COUNTRY from GEOLOCATION where ID={}", params);
	    final List<GeoLocation> geoLocations = this.jdbcTemplate.query(
		    "select ZIPCODE, COUNTRY from GEOLOCATION where ID=?", params, new RowMapper() {
			@Override
			public Object mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			    String zipcode = rs.getString("ZIPCODE");
			    String countryName = rs.getString("COUNTRY");
			    if (countryName == null || zipcode == null) {
				countryName = Country.GERMANY.name();
				zipcode = "81245";
			    }
			    final Country country = Enum.valueOf(Country.class, countryName);

			    // copy data to a GeoLocation object
			    final GeoLocation geoLocation = new GeoLocation();
			    geoLocation.setZipcode(zipcode);
			    geoLocation.setCountry(country);
			    return geoLocation;
			}
		    });
	    final GeoLocation geoLocation = geoLocations.get(0); // should be
								 // only one ;-)
	    // cache it
	    userprofileAndGeolocationCache.put(key, geoLocation);
	}
	return userprofileAndGeolocationCache;
    }

    private void migrateSearchProfile() {
	// get all searchprofiles (to get GEOLOCATION ids identifying
	// SEARCHPROFILE-geolocations)
	final HashMap<Long, Long> searchprofileAndGeolocationMap = new HashMap<Long, Long>();
	LOGGER.info("select ID, GEOLOCATION from SEARCHPROFILE");
	this.jdbcTemplate.query("select ID, GEOLOCATION from SEARCHPROFILE", new RowMapper() {
	    @Override
	    public Object mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final Long id = rs.getLong("ID");
		final Long geoLocationId = rs.getLong("GEOLOCATION");
		if (geoLocationId != null) {
		    searchprofileAndGeolocationMap.put(id, geoLocationId);
		}
		return null;
	    }
	});

	final Set<Long> keySet = searchprofileAndGeolocationMap.keySet();
	for (final Long key : keySet) {
	    // copy searchprofiles GEOLOCATION
	    final Long geoLocationId = searchprofileAndGeolocationMap.get(key);
	    final Object[] params = new Object[] { geoLocationId };
	    LOGGER.info("select ZIPCODE, COUNTRY from GEOLOCATION where ID={}", params);
	    final List<Long> geoAreaIds = this.jdbcTemplate.query(
		    "select ZIPCODE, COUNTRY from GEOLOCATION where ID=?", params, new RowMapper() {
			@Override
			public Object mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			    final String zipcode = rs.getString("ZIPCODE");
			    final String country = rs.getString("COUNTRY");

			    // copy data to GEOAREA
			    final KeyHolder keyHolder = new GeneratedKeyHolder();
			    final Object[] params = new Object[] { geoAreaCounter, country, zipcode };
			    LOGGER.info("insert into GEOAREA(ID, COUNTRY, ZIPCODE_START) values({},{},{})", params);

			    final PreparedStatementCreatorFactory preparedStatementCreatorFactory = new PreparedStatementCreatorFactory(
				    "insert into GEOAREA(ID, COUNTRY, ZIPCODE_START) values(?,?,?)", new int[] {
					    Types.BIGINT, Types.VARCHAR, Types.VARCHAR });
			    preparedStatementCreatorFactory.setReturnGeneratedKeys(true);
			    final PreparedStatementCreator preparedStatementCreator = preparedStatementCreatorFactory
				    .newPreparedStatementCreator(params);
			    jdbcTemplate.update(preparedStatementCreator, keyHolder);
			    final Map<String, Object> keys = keyHolder.getKeys();
			    final Long geoAreaId = (Long) keys.get("id");
			    geoAreaCounter++;
			    return geoAreaId;
			}
		    });
	    final Long geoAreaId = geoAreaIds.get(0); // should be only one ;-)

	    // update GEOAREA_ID in SEARCHPROFILE to just created GEOAREA
	    final Object[] params2 = new Object[] { geoAreaId, key };
	    LOGGER.info("update SEARCHPROFILE set GEOAREA_ID={} where ID={}", params2);
	    this.jdbcTemplate.update("update SEARCHPROFILE set GEOAREA_ID=? where ID=?", params2);

	    // copy searchprofile's MAXDISTANCE into just created GEOAREA
	    final Object[] params3 = new Object[] { key };
	    LOGGER.info("select MAXDISTANCE from SEARCHPROFILE where ID={}", params3);
	    final String maxDistance = this.jdbcTemplate.queryForObject(
		    "select MAXDISTANCE from SEARCHPROFILE where ID=?", params3, String.class);

	    final Object[] params4 = new Object[] { maxDistance, geoAreaId };
	    LOGGER.info("update GEOAREA set MAXDISTANCE={} where ID={}", params4);
	    this.jdbcTemplate.update("update GEOAREA set MAXDISTANCE=? where ID=?", params4);
	}
    }

    public String getDataFilename() {
	return dataFilename;
    }

    public void setDataFilename(final String dataFilename) {
	this.dataFilename = dataFilename;
    }
}
