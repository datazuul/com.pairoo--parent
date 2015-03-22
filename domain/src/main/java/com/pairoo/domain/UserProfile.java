package com.pairoo.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.datazuul.framework.domain.Language;
import com.datazuul.framework.domain.geo.GeoLocation;
import com.pairoo.domain.enums.EducationType;
import com.pairoo.domain.enums.FamilyStatusType;
import com.pairoo.domain.enums.HouseholdType;
import com.pairoo.domain.enums.IncomeType;
import com.pairoo.domain.enums.NumberOfKidsType;
import com.pairoo.domain.enums.OccupationType;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.Religion;
import com.pairoo.domain.enums.WantMoreChildrenType;

public class UserProfile extends AbstractPersistentDomainObject {

    private static final long serialVersionUID = 1L;
    private Appearance appearance = new Appearance();
    private Date birthdate = null;
    private EducationType education = EducationType.DONT_SAY;
    private FamilyStatusType familyStatus = FamilyStatusType.DONT_SAY;
    private GeoLocation geoLocation = null;
    private HouseholdType householdType = HouseholdType.DONT_SAY;
    private List<ImageEntry> imageEntries = new ArrayList<>();
    private IncomeType incomeType = IncomeType.DONT_SAY;
    private List<Language> languages = new ArrayList<>(
            Arrays.asList(Language.DONT_SAY));
    private LifeStyle lifeStyle = new LifeStyle();
    private Language motherLanguage = Language.DONT_SAY;
    private String motto = null;
    private NumberOfKidsType numberOfKidsType = NumberOfKidsType.DONT_SAY;
    private WantMoreChildrenType wantMoreChildrenType = WantMoreChildrenType.DONT_SAY;
    private OccupationType occupationType = OccupationType.DONT_SAY;
    private PartnerType partnerType = null;
    private PersonalValues personalValues = new PersonalValues();
    private String profession = null;
    private Religion religion = Religion.DONT_SAY;

    // FIXME what about this fields?
    // private Boolean ownHousehold;
    // private Integer kidsAtHome;
    // private Locale nationality;
    // private StarSign starSign;
    // private RelocationType relocationType;
    // private AboutMe aboutMe;
    /**
     * @return the appearance
     */
    public Appearance getAppearance() {
        return appearance;
    }

    /**
     * @return the birthdate
     */
    public Date getBirthdate() {
        return birthdate;
    }

    /**
     * @return the education
     */
    public EducationType getEducation() {
        return education;
    }

    /**
     * @return the familyStatus
     */
    public FamilyStatusType getFamilyStatus() {
        return familyStatus;
    }

    /**
     * @return the geoLocation
     */
    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    /**
     * @return the householdType
     */
    public HouseholdType getHouseholdType() {
        return householdType;
    }

    /**
     * @return the imageEntries
     */
    public List<ImageEntry> getImageEntries() {
        return imageEntries;
    }

    /**
     * @return the incomeType
     */
    public IncomeType getIncomeType() {
        return incomeType;
    }

    /**
     * @return the languages
     */
    public List<Language> getLanguages() {
        return languages;
    }

    /**
     * @return the lifeStyle
     */
    public LifeStyle getLifeStyle() {
        return lifeStyle;
    }

    /**
     * @return the motherLanguage
     */
    public Language getMotherLanguage() {
        return motherLanguage;
    }

    /**
     * @return the motto
     */
    public String getMotto() {
        return motto;
    }

    /**
     * @return the numberOfKidsType
     */
    public NumberOfKidsType getNumberOfKidsType() {
        return numberOfKidsType;
    }

    /**
     * @return the occupationType
     */
    public OccupationType getOccupationType() {
        return occupationType;
    }

    /**
     * @return the partnerType
     */
    public PartnerType getPartnerType() {
        return partnerType;
    }

    /**
     * @return the profession
     */
    public String getProfession() {
        return profession;
    }

    /**
     * @return the religion
     */
    public Religion getReligion() {
        return religion;
    }

    /**
     * @param appearance the appearance to set
     */
    public void setAppearance(final Appearance appearance) {
        this.appearance = appearance;
    }

    /**
     * @param birthdate the birthdate to set
     */
    public void setBirthdate(final Date birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * @param education the education to set
     */
    public void setEducation(final EducationType education) {
        this.education = education;
    }

    /**
     * @param familyStatus the familyStatus to set
     */
    public void setFamilyStatus(final FamilyStatusType familyStatus) {
        this.familyStatus = familyStatus;
    }

    /**
     * @param geoLocation the geoLocation to set
     */
    public void setGeoLocation(final GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    /**
     * @param householdType the householdType to set
     */
    public void setHouseholdType(final HouseholdType householdType) {
        this.householdType = householdType;
    }

    /**
     * @param imageEntries the imageEntries to set
     */
    public void setImageEntries(final List<ImageEntry> imageEntries) {
        this.imageEntries = imageEntries;
    }

    /**
     * @param incomeType the incomeType to set
     */
    public void setIncomeType(final IncomeType incomeType) {
        this.incomeType = incomeType;
    }

    /**
     * @param languages the languages to set
     */
    public void setLanguages(final List<Language> languages) {
        this.languages = languages;
    }

    /**
     * @param lifeStyle the lifeStyle to set
     */
    public void setLifeStyle(final LifeStyle lifeStyle) {
        this.lifeStyle = lifeStyle;
    }

    /**
     * @param motherLanguage the motherLanguage to set
     */
    public void setMotherLanguage(final Language motherLanguage) {
        this.motherLanguage = motherLanguage;
    }

    /**
     * @param motto the motto to set
     */
    public void setMotto(final String motto) {
        this.motto = motto;
    }

    /**
     * @param numberOfKidsType the numberOfKidsType to set
     */
    public void setNumberOfKidsType(final NumberOfKidsType numberOfKidsType) {
        this.numberOfKidsType = numberOfKidsType;
    }

    /**
     * @param occupationType the occupationType to set
     */
    public void setOccupationType(final OccupationType occupationType) {
        this.occupationType = occupationType;
    }

    /**
     * @param partnerType the partnerType to set
     */
    public void setPartnerType(final PartnerType partnerType) {
        this.partnerType = partnerType;
    }

    /**
     * @param profession the profession to set
     */
    public void setProfession(final String profession) {
        this.profession = profession;
    }

    /**
     * @param religion the religion to set
     */
    public void setReligion(final Religion religion) {
        this.religion = religion;
    }

    /**
     * @return the wantMoreChildrenType
     */
    public WantMoreChildrenType getWantMoreChildrenType() {
        return wantMoreChildrenType;
    }

    /**
     * @param wantMoreChildrenType the wantMoreChildrenType to set
     */
    public void setWantMoreChildrenType(
            final WantMoreChildrenType wantMoreChildrenType) {
        this.wantMoreChildrenType = wantMoreChildrenType;
    }

    /**
     * @return the personalValues
     */
    public PersonalValues getPersonalValues() {
        if (personalValues == null) {
            personalValues = new PersonalValues();
        }
        return personalValues;
    }

    /**
     * @param personalValues the personalValues to set
     */
    public void setPersonalValues(final PersonalValues personalValues) {
        this.personalValues = personalValues;
    }
}
