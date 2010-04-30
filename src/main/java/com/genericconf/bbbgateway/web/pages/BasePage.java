package com.genericconf.bbbgateway.web.pages;

import org.apache.wicket.IPageMap;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class BasePage extends WebPage {
	private static final long serialVersionUID = 1L;
	
	public BasePage(IModel<?> model) {
		super(model);
		init();
	}

	public BasePage(IPageMap pageMap, IModel<?> model) {
		super(pageMap, model);
		init();
	}

	public BasePage(IPageMap pageMap, PageParameters parameters) {
		super(pageMap, parameters);
		init();
	}

	public BasePage(IPageMap pageMap) {
		super(pageMap);
		init();
	}

	public BasePage(PageParameters parameters) {
		super(parameters);
		init();
	}

	public BasePage() {
		super();
		init();
	}
	
	private final void init() {
		add(CSSPackageResource.getHeaderContribution(new ResourceReference(BasePage.class, "res/css/custom-theme/jquery-ui-1.8.custom.css")));
		add(CSSPackageResource.getHeaderContribution(new ResourceReference(BasePage.class, "res/css/master.css", getSession().getLocale(), getSession().getStyle())));
		add(JavascriptPackageResource.getHeaderContribution(new ResourceReference(BasePage.class, "res/js/jquery-1.4.2.min.js")));
		add(JavascriptPackageResource.getHeaderContribution(new ResourceReference(BasePage.class, "res/js/jquery-ui-1.8.custom.min.js")));
		add(JavascriptPackageResource.getHeaderContribution(new ResourceReference(BasePage.class, "BasePage.js", getSession().getLocale(), getSession().getStyle())));

		add(new BookmarkablePageLink<Void>("homelink", getApplication().getHomePage()));
		add(new Label("content-header", createContentHeaderLabelModel()));
	}

	protected IModel<String> createContentHeaderLabelModel() {
		return new Model<String>("test");
	}

}
