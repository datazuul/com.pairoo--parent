package com.pairoo.frontend.webapp.wicket.components;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import javax.measure.quantity.Mass;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jscience.physics.amount.Amount;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.domain.geo.Country;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;

public class WeightFormComponent extends DropDownChoice<Integer> {
    private static final long serialVersionUID = 1L;

    @SpringBean(name = PersonProfileService.BEAN_ID)
    private PersonProfileService personProfileService;

    private Language targetLanguage;
    private Country targetCountry;

    public WeightFormComponent(final String id) {
	super(id);

	// ... values
	final ArrayList<Integer> weightValues = new ArrayList<Integer>();
	final Amount<Mass> minWeightInKgAmount = personProfileService.getMinWeight();
	final Amount<Mass> maxWeightInKgAmount = personProfileService.getMaxWeight();
	final int minWeightInKg = (int) minWeightInKgAmount.getExactValue();
	final int maxWeightInKg = (int) maxWeightInKgAmount.getExactValue();
	for (int weight = minWeightInKg; weight <= maxWeightInKg; weight++) {
	    weightValues.add(weight);
	}
	setChoices(weightValues);
	setChoiceRenderer(new IChoiceRenderer<Integer>() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public String getDisplayValue(Integer weightInKg) {
		Amount<Mass> weightInKgAmount = Amount.valueOf(weightInKg.longValue(), SI.KILOGRAM);

		String value;
		final Unit<Mass> preferredUnit = targetCountry.getUnitMass();
		if (!SI.KILOGRAM.equals(preferredUnit)) {
		    double weightInPreferredUnit = weightInKgAmount.doubleValue(preferredUnit);
		    DecimalFormat df = new DecimalFormat("##.#", DecimalFormatSymbols.getInstance(targetLanguage
			    .getLocale()));
		    df.setRoundingMode(RoundingMode.HALF_EVEN);
		    value = df.format(weightInPreferredUnit);
		} else {
		    value = String.valueOf(weightInKgAmount.getExactValue());
		}
		return value + " " + preferredUnit.toString();
	    }

	    @Override
	    public String getIdValue(Integer object, int index) {
		return String.valueOf(index);
	    }
	});
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	WicketWebSession session = ((WicketWebSession) getSession());
	targetLanguage = session.getSessionLanguage();
	targetCountry = session.getUser().getUserProfile().getGeoLocation().getCountry();
    }
}
