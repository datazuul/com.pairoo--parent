package com.pairoo.frontend.webapp.wicket.components;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import javax.measure.quantity.Length;
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

public class HeightFormComponent extends DropDownChoice<Integer> {
    private static final long serialVersionUID = 1L;

    @SpringBean(name = PersonProfileService.BEAN_ID)
    private PersonProfileService personProfileService;

    private Language targetLanguage;
    private Country targetCountry;

    public HeightFormComponent(final String id) {
	super(id);

	// ... values
	final ArrayList<Integer> heightValues = new ArrayList<Integer>();
	final Amount<Length> minHeightInCmAmount = personProfileService.getMinHeight();
	final Amount<Length> maxHeightInCmAmount = personProfileService.getMaxHeight();
	final int minHeightInCm = (int) minHeightInCmAmount.getExactValue();
	final int maxHeightInCm = (int) maxHeightInCmAmount.getExactValue();
	for (int height = minHeightInCm; height <= maxHeightInCm; height++) {
	    heightValues.add(height);
	}
	setChoices(heightValues);
	setChoiceRenderer(new IChoiceRenderer<Integer>() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public String getDisplayValue(Integer heightInCm) {
		Amount<Length> heightInCmAmount = Amount.valueOf(heightInCm.longValue(), SI.CENTIMETER);

		String value;
		final Unit<Length> preferredUnit = targetCountry.getUnitLength();
		if (!SI.CENTIMETER.equals(preferredUnit)) {
		    double heightInPreferredUnit = heightInCmAmount.doubleValue(preferredUnit);
		    DecimalFormat df = new DecimalFormat("##.#", DecimalFormatSymbols.getInstance(targetLanguage
			    .getLocale()));
		    df.setRoundingMode(RoundingMode.HALF_EVEN);
		    value = df.format(heightInPreferredUnit);
		} else {
		    value = String.valueOf(heightInCmAmount.getExactValue());
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
	targetCountry = session.getUsersCountry();
    }
}
