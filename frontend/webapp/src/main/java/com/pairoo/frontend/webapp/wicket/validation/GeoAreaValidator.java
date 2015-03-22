package com.pairoo.frontend.webapp.wicket.validation;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

import com.datazuul.framework.domain.geo.Country;
import com.pairoo.domain.enums.MaxDistance;

public class GeoAreaValidator extends AbstractFormValidator {
	private static final long serialVersionUID = 1L;

	/** form components to be checked. */
	private final FormComponent<?>[] components;

	public GeoAreaValidator(FormComponent<Country> countryFC, FormComponent<String> zipcodeFC,
			FormComponent<MaxDistance> maxDistanceFC) {
		if (countryFC == null || zipcodeFC == null || maxDistanceFC == null) {
			throw new IllegalArgumentException("argument formComponents cannot be null");
		}
		components = new FormComponent[] { countryFC, zipcodeFC, maxDistanceFC };
	}

	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return components;
	}

	@Override
	public void validate(Form<?> form) {
		// we have a choice to validate the type converted values or the raw
		// input values, we validate the raw input
		final FormComponent<?> countryFC = components[0];
		final FormComponent<?> zipcodeFC = components[1];
		final FormComponent<?> maxDistanceFC = components[2];

		Country country = (Country) countryFC.getConvertedInput();
		if (country == null) {
			error(countryFC);
		} else {
			MaxDistance maxDistance = (MaxDistance) maxDistanceFC.getConvertedInput();
			if (maxDistance != null && !MaxDistance.ANY.equals(maxDistance) && !Country.ANY.equals(country)) {
				// if country is sepcific and max. distance is specific, also a
				// zipcode in this country is required
				String zipcode = (String) zipcodeFC.getConvertedInput();
				if (zipcode == null) {
					error(zipcodeFC);
				}
			}
		}
	}

}
