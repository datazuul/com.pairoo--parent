package com.pairoo.backend.dao.search;

import com.datazuul.framework.domain.geo.Country;
import com.pairoo.domain.PersonProfile;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.Ethnicity;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.ProfilePictureType;
import com.pairoo.domain.enums.SmokeType;
import java.util.Date;
import java.util.List;

/**
 * Search params for searching for users.
 *
 * @author ralf
 */
public class DaoUserSearchParams {

    private Date birthdateOldest;
    private Date birthdateYoungest;
    private Country country;
    private List<Ethnicity> ethnicities;
    private Long idOfSearchingUser;
    private Double maxLatitude;
    private Double maxLongitude;
    private Double minLatitude;
    private Double minLongitude;
    private PartnerType partnerType;
    private ProfilePictureType profilePictureType;
    private SmokeType smokeType;
    private String zipcode;
    private String zipcodeStartsWith;

    public DaoUserSearchParams() {
        super();
    }

    public DaoUserSearchParams(User searchingUser, SearchProfile searchProfile) {
        super();

        // exclude own user
        idOfSearchingUser = searchingUser.getId();

        // add partner type filter to search
        final PartnerType pT = searchProfile.getPartnerType();
        if (!PartnerType.ANY.equals(pT)) {
            partnerType = pT;
        }

        // add age range filter
        birthdateYoungest = PersonProfile.getMinBirthdate(searchProfile.getMinAge());
        birthdateOldest = PersonProfile.getMaxBirthdate(searchProfile.getMaxAge());

        // add country filter
        Country c = searchProfile.getGeoArea().getCountry();
        if (searchProfile.getGeoArea().getGeoLocation() != null && c == null) {
            c = searchProfile.getGeoArea().getGeoLocation().getCountry();
        }
        if (c != null && c != Country.ANY) {
            country = c;
        }

        // smoke type
        final SmokeType sT = searchProfile.getSmokeType();
        if (sT != null && !SmokeType.ANY.equals(sT)) {
            smokeType = sT;
        }
        // profile picture type
        final ProfilePictureType pPT = searchProfile.getProfilePictureType();
        if (pPT != null && !ProfilePictureType.ANY.equals(pPT)) {
            profilePictureType = pPT;
        }
        // ethnicities
        final List<Ethnicity> eth = searchProfile.getEthnicities();
        if (eth != null && !eth.contains(Ethnicity.ANY)) {
            ethnicities = eth;
        }
    }

    public Date getBirthdateOldest() {
        return birthdateOldest;
    }

    public void setBirthdateOldest(Date birthdateOldest) {
        this.birthdateOldest = birthdateOldest;
    }

    public Date getBirthdateYoungest() {
        return birthdateYoungest;
    }

    public void setBirthdateYoungest(Date birthdateYoungest) {
        this.birthdateYoungest = birthdateYoungest;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Ethnicity> getEthnicities() {
        return ethnicities;
    }

    public void setEthnicities(List<Ethnicity> ethnicities) {
        this.ethnicities = ethnicities;
    }

    public Long getIdOfSearchingUser() {
        return idOfSearchingUser;
    }

    public void setIdOfSearchingUser(Long idOfSearchingUser) {
        this.idOfSearchingUser = idOfSearchingUser;
    }

    public Double getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLatitude(Double maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

    public Double getMaxLongitude() {
        return maxLongitude;
    }

    public void setMaxLongitude(Double maxLongitude) {
        this.maxLongitude = maxLongitude;
    }

    public Double getMinLatitude() {
        return minLatitude;
    }

    public void setMinLatitude(Double minLatitude) {
        this.minLatitude = minLatitude;
    }

    public Double getMinLongitude() {
        return minLongitude;
    }

    public void setMinLongitude(Double minLongitude) {
        this.minLongitude = minLongitude;
    }

    public PartnerType getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(PartnerType partnerType) {
        this.partnerType = partnerType;
    }

    public ProfilePictureType getProfilePictureType() {
        return profilePictureType;
    }

    public void setProfilePictureType(ProfilePictureType profilePictureType) {
        this.profilePictureType = profilePictureType;
    }

    public SmokeType getSmokeType() {
        return smokeType;
    }

    public void setSmokeType(SmokeType smokeType) {
        this.smokeType = smokeType;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getZipcodeStartsWith() {
        return zipcodeStartsWith;
    }

    public void setZipcodeStartsWith(String zipcodeStartsWith) {
        this.zipcodeStartsWith = zipcodeStartsWith;
    }
}
