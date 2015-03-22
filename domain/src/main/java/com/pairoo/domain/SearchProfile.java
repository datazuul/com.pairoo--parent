package com.pairoo.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.datazuul.framework.domain.Language;
import com.pairoo.domain.enums.AppearanceStyle;
import com.pairoo.domain.enums.BodyType;
import com.pairoo.domain.enums.EducationType;
import com.pairoo.domain.enums.Ethnicity;
import com.pairoo.domain.enums.EyeColor;
import com.pairoo.domain.enums.FamilyStatusType;
import com.pairoo.domain.enums.HairColor;
import com.pairoo.domain.enums.HobbyType;
import com.pairoo.domain.enums.HouseholdType;
import com.pairoo.domain.enums.IncomeType;
import com.pairoo.domain.enums.NumberOfKidsType;
import com.pairoo.domain.enums.OccupationType;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.PartnershipType;
import com.pairoo.domain.enums.ProfilePictureType;
import com.pairoo.domain.enums.Religion;
import com.pairoo.domain.enums.SmokeType;
import com.pairoo.domain.enums.SportType;
import com.pairoo.domain.enums.WantMoreChildrenType;
import com.pairoo.domain.geo.GeoArea;

/**
 * @author Ralf Eichinger
 */
public class SearchProfile extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private PartnerType partnerType = null; // do not set a default
    private Integer minAge = null;
    private Integer maxAge = null;
    private GeoArea geoArea = new GeoArea();
    private PartnershipType partnershipType = PartnershipType.ANY;
    private List<Language> languages = new ArrayList<Language>(
	    Arrays.asList(Language.ANY));
    private Language motherLanguage = Language.ANY;
    private List<FamilyStatusType> familyStatusTypes = new ArrayList<FamilyStatusType>(
	    Arrays.asList(FamilyStatusType.ANY));
    private NumberOfKidsType numberOfKidsType = NumberOfKidsType.ANY;
    private WantMoreChildrenType wantMoreChildrenType = WantMoreChildrenType.ANY;
    private HouseholdType householdType = HouseholdType.ANY;
    private Integer minHeightCm = null;
    private Integer maxHeightCm = null;
    private List<Ethnicity> ethnicities = new ArrayList<Ethnicity>(
	    Arrays.asList(Ethnicity.ANY));
    private List<BodyType> bodyTypes = new ArrayList<BodyType>(
	    Arrays.asList(BodyType.ANY));
    private List<EyeColor> eyeColors = new ArrayList<EyeColor>(
	    Arrays.asList(EyeColor.ANY));
    private List<HairColor> hairColors = new ArrayList<HairColor>(
	    Arrays.asList(HairColor.ANY));
    private List<Religion> religions = new ArrayList<Religion>(
	    Arrays.asList(Religion.ANY));
    private List<AppearanceStyle> appearanceStyles = new ArrayList<AppearanceStyle>(
	    Arrays.asList(AppearanceStyle.ANY));
    private SmokeType smokeType = SmokeType.ANY;
    private List<EducationType> educationTypes = new ArrayList<EducationType>(
	    Arrays.asList(EducationType.ANY));
    private IncomeType incomeType = IncomeType.ANY;
    private OccupationType occupationType = OccupationType.ANY;
    private List<HobbyType> hobbyTypes = new ArrayList<HobbyType>(
	    Arrays.asList(HobbyType.ANY));
    private List<SportType> sportTypes = new ArrayList<SportType>(
	    Arrays.asList(SportType.ANY));
    private ProfilePictureType profilePictureType = ProfilePictureType.ANY;

    /**
     * @return the partnerType
     */
    public PartnerType getPartnerType() {
	return partnerType;
    }

    /**
     * @param partnerType
     *            the partnerType to set
     */
    public void setPartnerType(final PartnerType partnerType) {
	this.partnerType = partnerType;
    }

    /**
     * @return the minAge
     */
    public Integer getMinAge() {
	return minAge;
    }

    /**
     * @param minAge
     *            the minAge to set
     */
    public void setMinAge(final Integer minAge) {
	this.minAge = minAge;
    }

    /**
     * @return the maxAge
     */
    public Integer getMaxAge() {
	return maxAge;
    }

    /**
     * @param maxAge
     *            the maxAge to set
     */
    public void setMaxAge(final Integer maxAge) {
	this.maxAge = maxAge;
    }

    /**
     * @return the partnershipType
     */
    public PartnershipType getPartnershipType() {
	return partnershipType;
    }

    /**
     * @param partnershipType
     *            the partnershipType to set
     */
    public void setPartnershipType(final PartnershipType partnershipType) {
	this.partnershipType = partnershipType;
    }

    /**
     * @return the familyStatusTypes
     */
    public List<FamilyStatusType> getFamilyStatusTypes() {
	return familyStatusTypes;
    }

    /**
     * @param familyStatusTypes
     *            the familyStatusTypes to set
     */
    public void setFamilyStatusTypes(
	    final List<FamilyStatusType> familyStatusTypes) {
	this.familyStatusTypes = familyStatusTypes;
    }

    /**
     * @return the minHeightCm
     */
    public Integer getMinHeightCm() {
	return minHeightCm;
    }

    /**
     * @param minHeightCm
     *            the minHeightCm to set
     */
    public void setMinHeightCm(final Integer minHeightCm) {
	this.minHeightCm = minHeightCm;
    }

    /**
     * @return the maxHeightCm
     */
    public Integer getMaxHeightCm() {
	return maxHeightCm;
    }

    /**
     * @param maxHeightCm
     *            the maxHeightCm to set
     */
    public void setMaxHeightCm(final Integer maxHeightCm) {
	this.maxHeightCm = maxHeightCm;
    }

    /**
     * @return the ethnicities
     */
    public List<Ethnicity> getEthnicities() {
	return ethnicities;
    }

    /**
     * @param ethnicities
     *            the ethnicities to set
     */
    public void setEthnicities(final List<Ethnicity> ethnicities) {
	this.ethnicities = ethnicities;
    }

    /**
     * @return the bodyTypes
     */
    public List<BodyType> getBodyTypes() {
	return bodyTypes;
    }

    /**
     * @param bodyTypes
     *            the bodyTypes to set
     */
    public void setBodyTypes(final List<BodyType> bodyTypes) {
	this.bodyTypes = bodyTypes;
    }

    /**
     * @return the eyeColors
     */
    public List<EyeColor> getEyeColors() {
	return eyeColors;
    }

    /**
     * @param eyeColors
     *            the eyeColors to set
     */
    public void setEyeColors(final List<EyeColor> eyeColors) {
	this.eyeColors = eyeColors;
    }

    /**
     * @return the hairColors
     */
    public List<HairColor> getHairColors() {
	return hairColors;
    }

    /**
     * @param hairColors
     *            the hairColors to set
     */
    public void setHairColors(final List<HairColor> hairColors) {
	this.hairColors = hairColors;
    }

    /**
     * @return the religions
     */
    public List<Religion> getReligions() {
	return religions;
    }

    /**
     * @param religions
     *            the religions to set
     */
    public void setReligions(final List<Religion> religions) {
	this.religions = religions;
    }

    /**
     * @return the educationTypes
     */
    public List<EducationType> getEducationTypes() {
	return educationTypes;
    }

    /**
     * @param educationTypes
     *            the educationTypes to set
     */
    public void setEducationTypes(final List<EducationType> educationTypes) {
	this.educationTypes = educationTypes;
    }

    /**
     * @return the incomeType
     */
    public IncomeType getIncomeType() {
	return incomeType;
    }

    /**
     * @param incomeType
     *            the incomeType to set
     */
    public void setIncomeType(final IncomeType incomeType) {
	this.incomeType = incomeType;
    }

    /**
     * @return the occupationType
     */
    public OccupationType getOccupationType() {
	return occupationType;
    }

    /**
     * @param occupationType
     *            the occupationType to set
     */
    public void setOccupationType(final OccupationType occupationType) {
	this.occupationType = occupationType;
    }

    /**
     * @return the hobbyTypes
     */
    public List<HobbyType> getHobbyTypes() {
	return hobbyTypes;
    }

    /**
     * @param hobbyTypes
     *            the hobbyTypes to set
     */
    public void setHobbyTypes(final List<HobbyType> hobbyTypes) {
	this.hobbyTypes = hobbyTypes;
    }

    /**
     * @param motherLanguage
     *            the motherLanguage to set
     */
    public void setMotherLanguage(final Language motherLanguage) {
	this.motherLanguage = motherLanguage;
    }

    /**
     * @return the motherLanguage
     */
    public Language getMotherLanguage() {
	return motherLanguage;
    }

    /**
     * @param householdType
     *            the householdType to set
     */
    public void setHouseholdType(final HouseholdType householdType) {
	this.householdType = householdType;
    }

    /**
     * @return the householdType
     */
    public HouseholdType getHouseholdType() {
	return householdType;
    }

    /**
     * @param smokeType
     *            the smokeType to set
     */
    public void setSmokeType(final SmokeType smokeType) {
	this.smokeType = smokeType;
    }

    /**
     * @return the smokeType
     */
    public SmokeType getSmokeType() {
	return smokeType;
    }

    /**
     * @return the appearanceStyles
     */
    public List<AppearanceStyle> getAppearanceStyles() {
	return appearanceStyles;
    }

    /**
     * @param appearanceStyles
     *            the appearanceStyles to set
     */
    public void setAppearanceStyles(final List<AppearanceStyle> appearanceStyles) {
	this.appearanceStyles = appearanceStyles;
    }

    /**
     * @return the geoArea
     */
    public GeoArea getGeoArea() {
	return geoArea;
    }

    /**
     * @param geoArea
     *            the geoArea to set
     */
    public void setGeoArea(final GeoArea geoArea) {
	this.geoArea = geoArea;
    }

    /**
     * @return the numberOfKidsType
     */
    public NumberOfKidsType getNumberOfKidsType() {
	return numberOfKidsType;
    }

    /**
     * @param numberOfKidsType
     *            the numberOfKidsType to set
     */
    public void setNumberOfKidsType(final NumberOfKidsType numberOfKidsType) {
	this.numberOfKidsType = numberOfKidsType;
    }

    /**
     * @return the wantMoreChildrenType
     */
    public WantMoreChildrenType getWantMoreChildrenType() {
	return wantMoreChildrenType;
    }

    /**
     * @param wantMoreChildrenType
     *            the wantMoreChildrenType to set
     */
    public void setWantMoreChildrenType(
	    final WantMoreChildrenType wantMoreChildrenType) {
	this.wantMoreChildrenType = wantMoreChildrenType;
    }

    /**
     * @return the languages
     */
    public List<Language> getLanguages() {
	return languages;
    }

    /**
     * @param languages
     *            the languages to set
     */
    public void setLanguages(final List<Language> languages) {
	this.languages = languages;
    }

    /**
     * @return the sportTypes
     */
    public List<SportType> getSportTypes() {
	return sportTypes;
    }

    /**
     * @param sportTypes
     *            the sportTypes to set
     */
    public void setSportTypes(final List<SportType> sportTypes) {
	this.sportTypes = sportTypes;
    }

    /**
     * @return the profile picture type, e.g. "yes" (with pictures)
     */
    public ProfilePictureType getProfilePictureType() {
	return profilePictureType;
    }

    /**
     * @param profilePictureType
     *            set the profile picture type, e.g. "yes" (with pictures)
     */
    public void setProfilePictureType(
	    final ProfilePictureType profilePictureType) {
	this.profilePictureType = profilePictureType;
    }
}
