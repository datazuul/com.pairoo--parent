package com.pairoo.domain.search;

import com.datazuul.framework.domain.geo.GeoLocation;
import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.enums.Ethnicity;
import com.pairoo.domain.enums.FamilyStatusType;
import com.pairoo.domain.enums.NumberOfKidsType;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.PartnershipType;
import com.pairoo.domain.enums.SmokeType;
import java.util.Date;
import java.util.List;

/**
 * @author ralf
 */
public class UserSearchResult extends SearchResult {

    private int age;
    private Date birthdate;
    private String distance;
    private Ethnicity ethnicity;
    private FamilyStatusType familyStatusType;
    private GeoLocation geoLocation;
    private Integer height;
    private Date lastLogin;
    private NumberOfKidsType numberOfKidsType;
    private boolean online;
    private PartnerType partnerType;
    private boolean premium;
    private Date premiumEndDate;
    private ImageEntry profileImageEntry;
    private PartnershipType searchesPartnershipType;
    private SmokeType smokeType;
    private String username;
    private List<ImageEntry> images;

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Ethnicity getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(Ethnicity ethnicity) {
        this.ethnicity = ethnicity;
    }

    public FamilyStatusType getFamilyStatusType() {
        return familyStatusType;
    }

    public void setFamilyStatusType(FamilyStatusType familyStatusType) {
        this.familyStatusType = familyStatusType;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public List<ImageEntry> getImages() {
        return images;
    }

    public void setImages(List<ImageEntry> images) {
        this.images = images;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public NumberOfKidsType getNumberOfKidsType() {
        return numberOfKidsType;
    }

    public void setNumberOfKidsType(NumberOfKidsType numberOfKidsType) {
        this.numberOfKidsType = numberOfKidsType;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public PartnerType getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(PartnerType partnerType) {
        this.partnerType = partnerType;
    }

    public Date getPremiumEndDate() {
        return premiumEndDate;
    }

    public void setPremiumEndDate(Date premiumEndDate) {
        this.premiumEndDate = premiumEndDate;
    }

    public ImageEntry getProfileImageEntry() {
        return profileImageEntry;
    }

    public void setProfileImageEntry(ImageEntry profileImageEntry) {
        this.profileImageEntry = profileImageEntry;
    }

    public PartnershipType getSearchesPartnershipType() {
        return searchesPartnershipType;
    }

    public void setSearchesPartnershipType(PartnershipType searchesPartnershipType) {
        this.searchesPartnershipType = searchesPartnershipType;
    }

    public SmokeType getSmokeType() {
        return smokeType;
    }

    public void setSmokeType(SmokeType smokeType) {
        this.smokeType = smokeType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
