package com.pairoo.frontend.webapp.wicket.panels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.util.EnumUtils;
import com.datazuul.framework.webapp.wicket.components.locale.SortableLocale;

/**
 * @author Ralf Eichinger
 */
public class SwitchLanguagePanel extends BasePanel {

    private static final long serialVersionUID = 2L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SwitchLanguagePanel.class);
    private final Locale locale = getLocale();

    public SwitchLanguagePanel(final String id, final Language[] languages) {
        super(id);

        final PropertyModel<Locale> pm = new PropertyModel<Locale>(SwitchLanguagePanel.this, "locale");
        // set the model that gets the current locale, and that is used for
        // updating the current locale to property 'locale' of FormInput
        setDefaultModel(pm);

        setOutputMarkupId(true);

        final List<Locale> sortedChoicesList = sortLocales(getLocale(), languages);
        final Select select = new Select("select", pm);
        final ListView<Locale> lv = new ListView<Locale>("lv", sortedChoicesList) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<Locale> item) {
                item.setRenderBodyOnly(true);
                final Locale itemLocale = item.getModelObject();
                String country = itemLocale.getCountry();
                if (country == null || country.length() == 0) {
                    country = itemLocale.getLanguage();
                }
                final String display = itemLocale.getDisplayName(locale);
                final SelectOption<Locale> option = new SelectOption<Locale>("option", new Model<Locale>(
                        item.getModelObject())) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
                        // LOGGER.info("Locale display: {}", display);
                        replaceComponentTagBody(markupStream, openTag, display);
                    }
                };
                option.add(new AttributeModifier("class", "flag " + country));
                option.setOutputMarkupId(true);
                item.add(option);
            }
        };
        lv.setOutputMarkupId(true);
        select.add(lv);
        select.setOutputMarkupId(true);
        select.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                final Locale locale = (Locale) select.getDefaultModelObject();

                getSession().setLocale(locale);
                // List<Locale> availableLocales = lv.getList();
                // List<Locale> sortedChoicesList = sortLocales(locale,
                // availableLocales);
                // lv.setList(sortedChoicesList);
                LOGGER.info("Locale changed to: {}", locale);
                setResponsePage(getPage().getClass());
            }
        });
        select.add(new AttributeModifier("class", "flag " + select.getDefaultModelObject()));
        add(select);

        // add(new LocaleDropDownChoice("localeSelect", availableLocales));
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssReferenceHeaderItem.forReference(new CssResourceReference(this.getClass(), "css/localeDropDownChoice.css")));
    }

    private List<Locale> sortLocales(final Locale newSelection, final Language[] languages) {
        final ArrayList<SortableLocale> sortableChoicesList = new ArrayList<SortableLocale>();
        for (int i = 0; i < languages.length; i++) {
            final Language language = languages[i];
            String label = getLocalizer().getString(EnumUtils.getEnumKey(language), this, null, language.getLocale(),
                    null, null);

            final SortableLocale sLocale = new SortableLocale(language.getLocale(), label);
            sortableChoicesList.add(sLocale);
        }
        Collections.sort(sortableChoicesList);

        final List<Locale> sortedChoicesList = new ArrayList<Locale>();
        for (final SortableLocale sLocale : sortableChoicesList) {
            final Locale locale = sLocale.getLocale();
            sortedChoicesList.add(0, locale);
        }
        return sortedChoicesList;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(final Locale locale) {
        getSession().setLocale(locale);
        LOGGER.info("Locale changed to: {}", getLocale());
    }
}
