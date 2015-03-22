package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import java.util.List;

import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.pairoo.business.api.ProductService;
import com.pairoo.domain.Membership;
import com.pairoo.domain.Product;
import com.pairoo.domain.enums.ProductDurationType;
import com.pairoo.domain.enums.Role;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;

/**
 * @author Ralf Eichinger
 */
public class EditMembershipProductPanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    @SpringBean(name = ProductService.BEAN_ID)
    private ProductService productService;

    public EditMembershipProductPanel(final String id, final IModel<Membership> model) {
	super(id, new CompoundPropertyModel<Membership>(model));
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(EditMembershipProductPanel.class);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	final RadioGroup<Product> group = productFormComponent("group");
	add(group);

	final ListView<Product> productsList = createProductListFormComponent("products");
	group.add(productsList);
    }

    private RadioGroup<Product> productFormComponent(String id) {
	final RadioGroup<Product> group = new RadioGroup<Product>(id, new PropertyModel<Product>(
		(Membership) getDefaultModelObject(), "product"));
	return group;
    }

    private ListView<Product> createProductListFormComponent(String id) {
	List<Product> products = productService.getAllProducts(Role.PREMIUM);
	final ListView<Product> productsFC = new ListView<Product>(id, products) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(final ListItem<Product> item) {
		final IModel<Product> itemModel = item.getModel();

		Radio<Product> radio = new Radio<Product>("radio", itemModel);
		radio.setOutputMarkupId(true);

		FormComponentLabel labelContainer = new FormComponentLabel("boxLabel", radio);
		item.add(labelContainer);

		labelContainer.add(radio);
		final Product product = itemModel.getObject();

		labelContainer.add(new EnumLabel<ProductDurationType>("duration", product.getDuration()));
		labelContainer.add(new EnumLabel<Role>("role", product.getRole()));
		labelContainer.add(new Label("abo", new ResourceModel("productAbo_" + product.isAbo())));
		labelContainer.add(new Label("monthlyRate", new Model<String>() {
		    private static final long serialVersionUID = 1L;

		    public String getObject() {
			return productService.getMonthlyRateAsString(product, getUsersCountry().getUnitMoney(),
				getPreferredLanguage());
		    };
		}));
		labelContainer.add(new Label("rateLabel", new Model<String>() {
		    private static final long serialVersionUID = 1L;

		    public String getObject() {
			return getUsersCountry().getUnitMoney().toString();
		    };
		}));
	    };
	}.setReuseItems(true);
	return productsFC;
    }
}
