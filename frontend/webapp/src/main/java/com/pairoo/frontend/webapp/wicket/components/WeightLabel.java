package com.pairoo.frontend.webapp.wicket.components;

import javax.measure.quantity.Mass;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.jscience.physics.amount.Amount;

public class WeightLabel extends Label {
    private static final long serialVersionUID = 1L;

    public WeightLabel(String id, Unit<Mass> targetUnit, int weightInKg) {
	super(id);

	String weightStr = "--";
	if (weightInKg > 0) {
	    Amount<Mass> weightInKgAmount = Amount.valueOf(weightInKg, SI.KILOGRAM);
	    long weightInPreferredUnit = Math.round(weightInKgAmount.doubleValue(targetUnit));
	    weightStr = String.valueOf(weightInPreferredUnit);
	}
	setDefaultModel(new Model<String>(weightStr + " " + targetUnit.toString()));
    }
}
