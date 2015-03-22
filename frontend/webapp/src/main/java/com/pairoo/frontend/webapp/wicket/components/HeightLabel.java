package com.pairoo.frontend.webapp.wicket.components;

import javax.measure.quantity.Length;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.jscience.physics.amount.Amount;

public class HeightLabel extends Label {
    private static final long serialVersionUID = 1L;

    public HeightLabel(String id, Unit<Length> targetUnit, int heightInCm) {
	super(id);

	String heightStr = "--";
	if (heightInCm > 0) {
	    Amount<Length> heightInCmAmount = Amount.valueOf(heightInCm, SI.CENTIMETER);
	    long heightInPreferredUnit = Math.round(heightInCmAmount.doubleValue(targetUnit));
	    heightStr = String.valueOf(heightInPreferredUnit);
	}
	setDefaultModel(new Model<String>(heightStr + " " + targetUnit.toString()));
    }
}
