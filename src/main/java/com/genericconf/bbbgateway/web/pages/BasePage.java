/**
 * Copyright 2010 Generic Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

import com.genericconf.bbbgateway.web.components.JQueryFeedbackPanel;

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
		add(JavascriptPackageResource.getHeaderContribution(new ResourceReference(BasePage.class, "res/js/jquery.dataTables.min.js")));
//		add(JavascriptPackageResource.getHeaderContribution(new ResourceReference(BasePage.class, "res/js/jquerytablesorter/tablesorter_modified_for_tablesorter_filter.js")));
//		add(JavascriptPackageResource.getHeaderContribution(new ResourceReference(BasePage.class, "res/js/jquerytablesorter/tablesorter_filter.js")));
		add(JavascriptPackageResource.getHeaderContribution(new ResourceReference(BasePage.class, "BasePage.js", getSession().getLocale(), getSession().getStyle())));

		add(new BookmarkablePageLink<Void>("homelink", getApplication().getHomePage()));
		add(new BookmarkablePageLink<Void>("createmeeting", CreateMeeting.class));
		
		add(new Label("content-header", createContentHeaderLabelModel()));

		add(new JQueryFeedbackPanel("feedback"));
	}

	protected IModel<String> createContentHeaderLabelModel() {
		return new Model<String>("test");
	}

}
