package com.pairoo.business.services.impl;

import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import javax.measure.unit.SI;

import org.jscience.physics.amount.Amount;

import com.pairoo.business.api.PersonProfileService;

/**
 * @author Ralf Eichinger
 */
public class PersonProfileServiceImpl implements PersonProfileService {

    @Override
    public Amount<Length> getMaxHeight() {
        return Amount.valueOf(220, SI.CENTIMETER);
    }

    @Override
    public Amount<Length> getMinHeight() {
        return Amount.valueOf(135, SI.CENTIMETER);
    }

    @Override
    public Amount<Mass> getMaxWeight() {
        return Amount.valueOf(180, SI.KILOGRAM);
    }

    @Override
    public Amount<Mass> getMinWeight() {
        return Amount.valueOf(35, SI.KILOGRAM);
    }

    @Override
    public int getMaxAge() {
        return 90;
    }

    @Override
    public int getMinAge() {
        return 18;
    }
}
