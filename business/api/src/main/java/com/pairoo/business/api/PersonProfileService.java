package com.pairoo.business.api;

import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;

import org.jscience.physics.amount.Amount;

/**
 * @author Ralf Eichinger
 */
public interface PersonProfileService {

    public static final String BEAN_ID = "personProfileService";

    /**
     * @return get maximum body height
     */
    public Amount<Length> getMaxHeight();

    /**
     * @return get minimum body height
     */
    public Amount<Length> getMinHeight();

    /**
     * @return maximum age
     */
    public int getMaxAge();

    /**
     * @return minimum age
     */
    public int getMinAge();

    /**
     * @return minimum mass
     */
    public Amount<Mass> getMinWeight();

    /**
     * @return maximum mass
     */
    public Amount<Mass> getMaxWeight();
}
