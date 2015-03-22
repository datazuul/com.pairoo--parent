package com.pairoo.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.pairoo.domain.enums.DrinkType;
import com.pairoo.domain.enums.FitnessType;
import com.pairoo.domain.enums.FoodType;
import com.pairoo.domain.enums.HobbyType;
import com.pairoo.domain.enums.HolidayPlanning;
import com.pairoo.domain.enums.HolidayType;
import com.pairoo.domain.enums.HomeType;
import com.pairoo.domain.enums.KitchenType;
import com.pairoo.domain.enums.LivingSituation;
import com.pairoo.domain.enums.MusicType;
import com.pairoo.domain.enums.OccupationType;
import com.pairoo.domain.enums.PetType;
import com.pairoo.domain.enums.ProfessionType;
import com.pairoo.domain.enums.SmokeType;
import com.pairoo.domain.enums.SportType;
import com.pairoo.domain.enums.SportsActivityType;

/**
 * @author buntschw, Ralf Eichinger
 */
public class LifeStyle extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private DrinkType drinkType = DrinkType.DONT_SAY;
    private SmokeType smokeType = SmokeType.DONT_SAY;
    private OccupationType occupationType = OccupationType.DONT_SAY;
    private ProfessionType professionType = ProfessionType.DONT_SAY;
    private HomeType homeType = HomeType.DONT_SAY;
    private LivingSituation livingSituation = LivingSituation.DONT_SAY;
    private Boolean playInstrument;
    private SportsActivityType sportsActivityType = SportsActivityType.DONT_SAY;
    private HolidayPlanning holidayPlanningType = HolidayPlanning.DONT_SAY;
    private FitnessType fitnessType = FitnessType.DONT_SAY;

    private List<FoodType> foodTypes = new ArrayList<FoodType>(
	    Arrays.asList(FoodType.DONT_SAY));
    private List<KitchenType> kitchenTypes = new ArrayList<KitchenType>(
	    Arrays.asList(KitchenType.DONT_SAY));
    private List<PetType> petTypes = new ArrayList<PetType>(
	    Arrays.asList(PetType.DONT_SAY));
    private List<HobbyType> hobbyTypes = new ArrayList<HobbyType>(
	    Arrays.asList(HobbyType.DONT_SAY));
    private List<MusicType> musicTypes = new ArrayList<MusicType>(
	    Arrays.asList(MusicType.DONT_SAY));
    private List<SportType> sportTypes = new ArrayList<SportType>(
	    Arrays.asList(SportType.DONT_SAY));
    private List<HolidayType> holidayTypes = new ArrayList<HolidayType>(
	    Arrays.asList(HolidayType.DONT_SAY));

    /**
     * @param drinkType
     *            the drinkType to set
     */
    public void setDrinkType(final DrinkType drinkType) {
	this.drinkType = drinkType;
    }

    /**
     * @return the drinkType
     */
    public DrinkType getDrinkType() {
	return drinkType;
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
     * @param livingSituation
     *            the livingSituation to set
     */
    public void setLivingSituation(final LivingSituation livingSituation) {
	this.livingSituation = livingSituation;
    }

    /**
     * @return the livingSituation
     */
    public LivingSituation getLivingSituation() {
	return livingSituation;
    }

    /**
     * @param homeType
     *            the homeType to set
     */
    public void setHomeType(final HomeType homeType) {
	this.homeType = homeType;
    }

    /**
     * @return the homeType
     */
    public HomeType getHomeType() {
	return homeType;
    }

    /**
     * @param professionType
     *            the professionType to set
     */
    public void setProfessionType(final ProfessionType professionType) {
	this.professionType = professionType;
    }

    /**
     * @return the professionType
     */
    public ProfessionType getProfessionType() {
	return professionType;
    }

    /**
     * @param occupationType
     *            the occupationType to set
     */
    public void setOccupationType(final OccupationType occupationType) {
	this.occupationType = occupationType;
    }

    /**
     * @return the occupationType
     */
    public OccupationType getOccupationType() {
	return occupationType;
    }

    /**
     * @param holidayPlanningType
     *            the holidayPlanningType to set
     */
    public void setHolidayPlanningType(final HolidayPlanning holidayPlanningType) {
	this.holidayPlanningType = holidayPlanningType;
    }

    /**
     * @return the holidayPlanningType
     */
    public HolidayPlanning getHolidayPlanningType() {
	return holidayPlanningType;
    }

    /**
     * @return the sportsActivityType
     */
    public SportsActivityType getSportsActivityType() {
	return sportsActivityType;
    }

    /**
     * @param sportsActivityType
     *            the sportsActivityType to set
     */
    public void setSportsActivityType(
	    final SportsActivityType sportsActivityType) {
	this.sportsActivityType = sportsActivityType;
    }

    /**
     * @param playInstrument
     *            the playInstrument to set
     */
    public void setPlayInstrument(final Boolean playInstrument) {
	this.playInstrument = playInstrument;
    }

    /**
     * @return the playInstrument
     */
    public Boolean getPlayInstrument() {
	return playInstrument;
    }

    public List<KitchenType> getKitchenTypes() {
	return kitchenTypes;
    }

    public void setKitchenTypes(final List<KitchenType> kitchenTypes) {
	this.kitchenTypes = kitchenTypes;
    }

    public List<PetType> getPetTypes() {
	return petTypes;
    }

    public void setPetTypes(final List<PetType> petTypes) {
	this.petTypes = petTypes;
    }

    public List<HobbyType> getHobbyTypes() {
	return hobbyTypes;
    }

    public void setHobbyTypes(final List<HobbyType> hobbyTypes) {
	this.hobbyTypes = hobbyTypes;
    }

    public List<MusicType> getMusicTypes() {
	return musicTypes;
    }

    public void setMusicTypes(final List<MusicType> musicTypes) {
	this.musicTypes = musicTypes;
    }

    public List<SportType> getSportTypes() {
	return sportTypes;
    }

    public void setSportTypes(final List<SportType> sportTypes) {
	this.sportTypes = sportTypes;
    }

    public List<HolidayType> getHolidayTypes() {
	return holidayTypes;
    }

    public void setHolidayTypes(final List<HolidayType> holidayTypes) {
	this.holidayTypes = holidayTypes;
    }

    public List<FoodType> getFoodTypes() {
	return foodTypes;
    }

    public void setFoodTypes(final List<FoodType> foodTypes) {
	this.foodTypes = foodTypes;
    }

    /**
     * @param fitnessType
     *            the fitnessType to set
     */
    public void setFitnessType(final FitnessType fitnessType) {
	this.fitnessType = fitnessType;
    }

    /**
     * @return the fitnessType
     */
    public FitnessType getFitnessType() {
	return fitnessType;
    }
}
