package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.util.EnumUtils;
import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.pairoo.business.api.CountryService;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;

public class PersonalDataFormComponentPanel extends BasePanel {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = CountryService.BEAN_ID)
    private CountryService countryService;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;
    FormComponent<String> firstnameFC;
    FormComponent<String> lastnameFC;

    public PersonalDataFormComponentPanel(final String id, IModel<User> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // form
        final ShinyForm form = createForm("form", new CompoundPropertyModel<User>((IModel<User>) getDefaultModel()));
        add(form);

        firstnameFC = createFirstnameFC("firstname");
        form.add(firstnameFC);
        // firstnameFC.add(new
        // ClientAndServerRequiredValidatingBehavior<String>(form));
        // firstnameFC.add(new
        // ClientAndServerMaximumLengthValidatingBehavior(form, 255));
        form.add(labelForFormComponent("firstnameLabel", firstnameFC));

        lastnameFC = createLastnameFC("lastname");
        form.add(lastnameFC);
        // lastnameFC.add(new
        // ClientAndServerRequiredValidatingBehavior<String>(form));
        // lastnameFC.add(new
        // ClientAndServerMaximumLengthValidatingBehavior(form, 255));
        form.add(labelForFormComponent("lastnameLabel", lastnameFC));

        final FormComponent<String> streetFC = createStreetFC("address.street");
        form.add(streetFC);
        // streetFC.add(new
        // ClientAndServerRequiredValidatingBehavior<String>(form));
        // streetFC.add(new ClientAndServerMaximumLengthValidatingBehavior(form,
        // 255));
        form.add(labelForFormComponent("streetLabel", streetFC));

        final FormComponent<String> houseNumberFC = createHouseNumberFC("address.housenr");
        form.add(houseNumberFC);
        // houseNumberFC.add(new
        // ClientAndServerRequiredValidatingBehavior<String>(form));
        // houseNumberFC.add(new
        // ClientAndServerMaximumLengthValidatingBehavior(form, 10));
        form.add(labelForFormComponent("houseNumberLabel", houseNumberFC));

        final FormComponent<String> zipcodeFC = createZipcodeFC("address.zipcode");
        form.add(zipcodeFC);
        // zipcodeFC.add(new
        // ClientAndServerRequiredValidatingBehavior<String>(form));
        // zipcodeFC.add(new
        // ClientAndServerMaximumLengthValidatingBehavior(form, 20));
        form.add(labelForFormComponent("zipcodeLabel", zipcodeFC));

        final FormComponent<String> cityFC = createCityFC("address.city");
        form.add(cityFC);
        // cityFC.add(new
        // ClientAndServerRequiredValidatingBehavior<String>(form));
        // cityFC.add(new ClientAndServerMaximumLengthValidatingBehavior(form,
        // 255));
        form.add(labelForFormComponent("cityLabel", cityFC));

        final FormComponent<Country> countryFC = createCountryFC("address.country");
        form.add(countryFC);
        // countryFC.add(new
        // ClientAndServerRequiredValidatingBehavior<Country>(form));
        form.add(labelForFormComponent("countryLabel", countryFC));
    }

    private FormComponent<Country> createCountryFC(String id) {
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
        final DropDownChoice<Country> fc = new DropDownChoice<Country>(id, countryOptionsModel);
        fc.setChoiceRenderer(new IChoiceRenderer<Country>() {
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
                final List<? extends Country> choices = fc.getChoices();
                if (choices == null || index == -1) {
                    return "";
                }
                return String.valueOf(choices.get(index));
            }
        });

        fc.setLabel(new ResourceModel("country"));
        fc.setEscapeModelStrings(false);
        fc.setOutputMarkupId(true);
        // ... validation
        fc.setRequired(true);
        return fc;
    }

    private FormComponent<String> createCityFC(String id) {
        // ... field
        final FormComponent<String> fc = new TextField<String>(id);
        fc.setLabel(new ResourceModel("city"));
        // ... behavior
        fc.setOutputMarkupId(true);
        // ... validation
        fc.add(new AttributeModifier("maxLength", "255"));
        fc.add(StringValidator.maximumLength(255));
        return fc;
    }

    private FormComponent<String> createZipcodeFC(String id) {
        // ... field
        FormComponent<String> fc = new TextField<String>(id);
        fc.setLabel(new ResourceModel("zipcode"));
        fc.add(new AttributeModifier("size", "5"));
        // ... behavior
        fc.setOutputMarkupId(true);
        // ... validation
        fc.add(new AttributeModifier("maxLength", "20"));
        fc.add(StringValidator.maximumLength(20));
        return fc;
    }

    private FormComponent<String> createHouseNumberFC(String id) {
        // ... field
        final FormComponent<String> fc = new TextField<String>(id);
        fc.setLabel(new ResourceModel("house_number"));
        fc.add(new AttributeModifier("size", "5"));
        // ... behavior
        fc.setOutputMarkupId(true);
        // ... validation
        fc.add(new AttributeModifier("maxLength", "10"));
        fc.add(StringValidator.maximumLength(10));
        return fc;
    }

    private FormComponent<String> createStreetFC(String id) {
        // ... field
        final FormComponent<String> fc = new TextField<String>(id);
        fc.setLabel(new ResourceModel("street"));
        fc.add(new AttributeModifier("size", "25"));
        // ... behavior
        fc.setOutputMarkupId(true);
        // ... validation
        fc.add(new AttributeModifier("maxLength", "255"));
        fc.add(StringValidator.maximumLength(255));
        return fc;
    }

    private FormComponent<String> createFirstnameFC(String id) {
        // ... field
        final FormComponent<String> fc = new RequiredTextField<String>(id);
        fc.setLabel(new ResourceModel("firstName"));
        fc.add(new AttributeModifier("size", "25"));
        // ... behavior
        fc.setOutputMarkupId(true);
        // ... validation
        fc.add(new AttributeModifier("maxLength", "255"));
        fc.add(StringValidator.lengthBetween(1, 255));
        return fc;
    }

    private FormComponent<String> createLastnameFC(String id) {
        // ... field
        final FormComponent<String> fc = new RequiredTextField<String>(id);
        fc.setLabel(new ResourceModel("lastName"));
        fc.add(new AttributeModifier("size", "25"));
        // ... behavior
        fc.setOutputMarkupId(true);
        // ... validation
        fc.add(new AttributeModifier("maxLength", "255"));
        fc.add(StringValidator.lengthBetween(1, 255));
        return fc;
    }

    private ShinyForm createForm(String id, CompoundPropertyModel<User> model) {
        final ShinyForm form = new ShinyForm(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                // save personal data
                User user = (User) getDefaultModelObject();
                userService.save(user);
            }
        };
        form.setOutputMarkupId(true);
        return form;
    }
}
