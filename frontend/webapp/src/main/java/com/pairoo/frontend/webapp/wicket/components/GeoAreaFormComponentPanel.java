package com.pairoo.frontend.webapp.wicket.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.GeoLocation;
import com.datazuul.framework.util.EnumUtils;
import com.pairoo.business.api.CountryService;
import com.pairoo.business.api.GeoLocationService;
import com.pairoo.domain.enums.MaxDistance;
import com.pairoo.domain.geo.GeoArea;
import com.pairoo.frontend.webapp.wicket.panels.BaseFormComponentPanel;

/**
 * Form component for editing a GeoArea domain object. Fields are: country,
 * zipcode, max distance
 * 
 * @author Ralf Eichinger
 */
public class GeoAreaFormComponentPanel extends BaseFormComponentPanel<GeoArea> {
    private static final long serialVersionUID = 1L;

    @SpringBean(name = CountryService.BEAN_ID)
    private CountryService countryService;

    @SpringBean(name = GeoLocationService.BEAN_ID)
    private GeoLocationService geoLocationService;

    private DropDownChoice<Country> countryFC;
    private FormComponent<MaxDistance> maxDistanceFC;
    private FormComponent<String> zipcodeStartsWithFC;

    private IModel<GeoArea> workingModel;

    /**
     * Use CompoundPropertyModel and id is path to GeoArea.
     * 
     * @param id
     *            id of panel
     */
    private GeoAreaFormComponentPanel(String id) {
	super(id);
	addComponents();
    }

    public GeoAreaFormComponentPanel(String id, IModel<GeoArea> model) {
	super(id, model);

	workingModel = new Model<GeoArea>(new GeoArea());
	GeoArea workingGeoArea = workingModel.getObject();
	GeoArea originalGeoArea = model.getObject();
	//	workingGeoArea.setContinent(originalGeoArea.getContinent());
	workingGeoArea.setCountry(originalGeoArea.getCountry());
	workingGeoArea.setGeoLocation(originalGeoArea.getGeoLocation());
	workingGeoArea.setMaxDistance(originalGeoArea.getMaxDistance());
	//	workingGeoArea.setSubdivision(originalGeoArea.getSubdivision());
	workingGeoArea.setZipcodeStartsWith(originalGeoArea.getZipcodeStartsWith());

	GeoLocation geoLocation = originalGeoArea.getGeoLocation();
	if (geoLocation != null) {
	    workingGeoArea.setCountry(geoLocation.getCountry());
	    workingGeoArea.setZipcodeStartsWith(geoLocation.getZipcode());
	}
	addComponents();
    }

    private void addComponents() {
	add(createCountryFormComponent("country", new PropertyModel<Country>(workingModel, "country")));
	add(createFormComponentLabel("countryLabel", countryFC));

	add(createZipcodeStartsWithFormComponent("zipcodeStartsWith", new PropertyModel<String>(workingModel,
		"zipcodeStartsWith")));
	add(createFormComponentLabel("zipcodeStartsWithLabel", zipcodeStartsWithFC));

	add(createMaxDistanceFormComponent("maxDistance", new PropertyModel<MaxDistance>(workingModel, "maxDistance")));
	add(createFormComponentLabel("maxDistanceLabel", maxDistanceFC));
    }

    private FormComponent<Country> createCountryFormComponent(final String id, final IModel<Country> model) {
	// ... values
	final IModel<List<Country>> countryOptionsModel = new AbstractReadOnlyModel<List<Country>>() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public List<Country> getObject() {
		List<Country> selectableCountries = countryService.getSelectableCountries();
		// sort in current locale
		selectableCountries = sortCountries(getLocale(), selectableCountries);
		return selectableCountries;
	    }
	};
	// ... field
	countryFC = new DropDownChoice<Country>(id, model, countryOptionsModel);
	countryFC.setChoiceRenderer(new IChoiceRenderer<Country>() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public Object getDisplayValue(final Country country) {
		if (country == null) {
		    return "";
		}
		return ((IModel<String>) new ResourceModel(EnumUtils.getEnumKey(country))).getObject();
	    }

	    @Override
	    public String getIdValue(final Country object, final int index) {
		final List<? extends Country> choices = countryFC.getChoices();
		if (choices == null || index == -1) {
		    return "";
		}
		return String.valueOf(choices.get(index));
	    }
	});

	// behavior...
	countryFC.add(new OnChangeAjaxBehavior() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void onUpdate(AjaxRequestTarget target) {
		Country country = countryFC.getModelObject();

		//		IModel<GeoArea> geoArea = getModel();
		//		
		//		WicketWebSession session = (WicketWebSession) getSession();
		//		SearchProfile searchProfile = session.getSearchProfile();
		//		searchProfile.getGeoArea().setGeoLocation(null);
		//		searchProfile.getGeoArea().setMaxDistance(null);

		zipcodeStartsWithFC.setModelObject(null);
		if (country != null && !Country.ANY.equals(country)) {
		    zipcodeStartsWithFC.setEnabled(true);
		} else {
		    zipcodeStartsWithFC.setEnabled(false);
		}
		target.add(zipcodeStartsWithFC);

		maxDistanceFC.setEnabled(false);
		maxDistanceFC.setModelObject(MaxDistance.ANY);
		target.add(maxDistanceFC);
	    }

	});
	countryFC.setLabel(new ResourceModel("country"));
	countryFC.setEscapeModelStrings(false);
	countryFC.setOutputMarkupId(true);
	// ... validation
	countryFC.setRequired(true);
	return countryFC;
    }

    private FormComponent<String> createZipcodeStartsWithFormComponent(final String id, final IModel<String> model) {
	// ... field
	// final AutoCompleteTextField<String> searchProfileZipcodeField = new
	// AutoCompleteTextField<String>(
	// "geoArea.zipcodeStartsWith") {
	// @Override
	// protected Iterator<String> getChoices(String input) {
	// List<String> choices;
	// if (Strings.isEmpty(input)) {
	// choices = Collections.emptyList();
	// } else {
	// choices = new ArrayList<String>();
	// final Search search = new Search(GeoLocation.class);
	// search.addFilterILike("zipcode", input + "%");
	// List<GeoLocation> choicesGeoLocations =
	// geoLocationService.search(search);
	//
	// if (choicesGeoLocations != null) {
	//
	// for (Iterator<GeoLocation> iterator = choicesGeoLocations.iterator();
	// iterator.hasNext();) {
	// GeoLocation geoLocation = iterator.next();
	// String displayValue = geoLocation.getZipcode() + " " +
	// geoLocation.getName();
	// choices.add(displayValue);
	// }
	// }
	// }
	// return choices.iterator();
	// }
	//
	// @Override
	// protected void convertInput() {
	// String input = getInput();
	// if (input != null && input.length() > 0 && input.contains(" ")) {
	// input = input.substring(0, input.indexOf(" "));
	// }
	// setConvertedInput(input);
	// }
	// };
	zipcodeStartsWithFC = new TextField<String>(id, model);
	zipcodeStartsWithFC.add(new AttributeModifier("maxLength", "20"));
	zipcodeStartsWithFC.add(new AttributeModifier("size", "5"));
	zipcodeStartsWithFC.setLabel(new ResourceModel("zipcodeStartsWith"));
	zipcodeStartsWithFC.setOutputMarkupId(true);

	// ... behavior
	OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void onUpdate(AjaxRequestTarget target) {
		String zipcode = zipcodeStartsWithFC.getModelObject();
		Country country = countryFC.getModelObject();
		final GeoLocation geoLocation = geoLocationService.getByCountryAndZipcode(country, zipcode);
		if (geoLocation != null) {
		    maxDistanceFC.setEnabled(true);
		    zipcodeStartsWithFC.setRequired(true);
		} else {
		    maxDistanceFC.setModelObject(MaxDistance.ANY);
		    maxDistanceFC.setEnabled(false);
		    zipcodeStartsWithFC.setRequired(false);
		}
		workingModel.getObject().setGeoLocation(geoLocation);
		target.add(maxDistanceFC);
	    }
	};
	zipcodeStartsWithFC.add(onChangeAjaxBehavior);

	// ... validation
	zipcodeStartsWithFC.add(StringValidator.maximumLength(20));
	return zipcodeStartsWithFC;
    }

    private FormComponent<MaxDistance> createMaxDistanceFormComponent(final String id, final IModel<MaxDistance> model) {
	// ... values
	final MaxDistance[] maxDistanceValues = MaxDistance.values();
	final ArrayList<MaxDistance> maxDistances = new ArrayList<MaxDistance>(Arrays.asList(maxDistanceValues));

	// ... field
	maxDistanceFC = new DropDownChoice<MaxDistance>(id, model, maxDistances, new EnumChoiceRenderer<MaxDistance>(
		this));
	maxDistanceFC.setLabel(new ResourceModel("maxDistance"));
	maxDistanceFC.setEscapeModelStrings(false);
	maxDistanceFC.setOutputMarkupId(true);

	// ... validation
	maxDistanceFC.setRequired(true);

	// ... behavior
	OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void onUpdate(AjaxRequestTarget target) {
		MaxDistance maxDistance = maxDistanceFC.getConvertedInput();
		if (MaxDistance.ANY.equals(maxDistance)) {
		    zipcodeStartsWithFC.setRequired(false);
		} else {
		    zipcodeStartsWithFC.setRequired(true);
		}
	    }
	};
	maxDistanceFC.add(onChangeAjaxBehavior);

	return maxDistanceFC;
    }

    @Override
    protected void convertInput() {
	final Country country = countryFC.getConvertedInput();
	final String zipcodeStartsWith = zipcodeStartsWithFC.getConvertedInput();
	final MaxDistance maxDistance = maxDistanceFC.getConvertedInput();
	final GeoLocation geoLocation = geoLocationService.getByCountryAndZipcode(country, zipcodeStartsWith);

	GeoArea geoArea = new GeoArea();
	geoArea.setCountry(country);
	geoArea.setZipcodeStartsWith(zipcodeStartsWith);
	geoArea.setMaxDistance(maxDistance);
	geoArea.setGeoLocation(geoLocation);

	setConvertedInput(geoArea);
    }

    @Override
    protected void onBeforeRender() {
	GeoArea geoArea = workingModel.getObject();
	if (geoArea.getGeoLocation() != null) {
	    maxDistanceFC.setEnabled(true);
	    maxDistanceFC.setModelObject(geoArea.getMaxDistance());
	    zipcodeStartsWithFC.setEnabled(true);
	    zipcodeStartsWithFC.setModelObject(geoArea.getGeoLocation().getZipcode());
	    countryFC.setModelObject(geoArea.getGeoLocation().getCountry());
	} else {
	    maxDistanceFC.setEnabled(false);
	    maxDistanceFC.setModelObject(geoArea.getMaxDistance());
	    zipcodeStartsWithFC.setModelObject(geoArea.getZipcodeStartsWith());
	    Country country = geoArea.getCountry();
	    countryFC.setModelObject(country);
	    if (country == null || Country.ANY.equals(country)) {
		zipcodeStartsWithFC.setEnabled(false);
	    } else {
		zipcodeStartsWithFC.setEnabled(true);
	    }
	}
	super.onBeforeRender();
    }
}
