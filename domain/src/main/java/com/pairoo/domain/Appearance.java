package com.pairoo.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.pairoo.domain.enums.AppearanceStyle;
import com.pairoo.domain.enums.BodyType;
import com.pairoo.domain.enums.Ethnicity;
import com.pairoo.domain.enums.EyeColor;
import com.pairoo.domain.enums.HairColor;

/**
 * @author ralf
 */
public class Appearance extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private Integer height = null;
    private Integer weight = null;
    private List<AppearanceStyle> appearanceStyles = new ArrayList<>(
	    Arrays.asList(AppearanceStyle.DONT_SAY));
    private BodyType bodyType = BodyType.DONT_SAY;
    private EyeColor eyeColor = EyeColor.DONT_SAY;
    private HairColor hairColor = HairColor.DONT_SAY;
    private Ethnicity ethnicity = Ethnicity.DONT_SAY;

    /**
     * @param height
     *            the height to set
     */
    public void setHeight(final Integer height) {
	this.height = height;
    }

    /**
     * @return the height
     */
    public Integer getHeight() {
	return height;
    }

    /**
     * @param weight
     *            the weight to set
     */
    public void setWeight(final Integer weight) {
	this.weight = weight;
    }

    /**
     * @return the weight
     */
    public Integer getWeight() {
	return weight;
    }

    /**
     * @param eyeColor
     *            the eyeColor to set
     */
    public void setEyeColor(final EyeColor eyeColor) {
	this.eyeColor = eyeColor;
    }

    /**
     * @return the eyeColor
     */
    public EyeColor getEyeColor() {
	return eyeColor;
    }

    /**
     * @param hairColor
     *            the hairColor to set
     */
    public void setHairColor(final HairColor hairColor) {
	this.hairColor = hairColor;
    }

    /**
     * @return the hairColor
     */
    public HairColor getHairColor() {
	return hairColor;
    }

    /**
     * @param bodyType
     *            the bodyType to set
     */
    public void setBodyType(final BodyType bodyType) {
	this.bodyType = bodyType;
    }

    /**
     * @return the bodyType
     */
    public BodyType getBodyType() {
	return bodyType;
    }

    /**
     * @param ethnicity
     *            the ethnicity to set
     */
    public void setEthnicity(final Ethnicity ethnicity) {
	this.ethnicity = ethnicity;
    }

    /**
     * @return the ethnicity
     */
    public Ethnicity getEthnicity() {
	return ethnicity;
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

}
