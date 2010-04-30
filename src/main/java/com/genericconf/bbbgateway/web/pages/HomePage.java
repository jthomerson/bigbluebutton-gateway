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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;

import com.genericconf.bbbgateway.domain.Meeting;
import com.genericconf.bbbgateway.services.IMeetingService;

public class HomePage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private IMeetingService meetingService;

    public HomePage(final PageParameters parameters) {
    	IModel<? extends List<? extends Meeting>> model = new LoadableDetachableModel<List<? extends Meeting>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends Meeting> load() {
				return new ArrayList<Meeting>(meetingService.getMeetings());
			}
		};

		add(new PropertyListView<Meeting>("meetings", model) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Meeting> item) {
				item.add(new Label("name"));
				item.add(new Label("startTime") {
					private static final long serialVersionUID = 1L;

					@Override
					public IConverter getConverter(Class<?> type) {
						return new IConverter() {
							private static final long serialVersionUID = 1L;

							@SuppressWarnings("deprecation")
							@Override
							public String convertToString(Object value, Locale locale) {
								return ((Date) value).toGMTString();
							}
							
							@Override
							public Object convertToObject(String value, Locale locale) {
								throw new UnsupportedOperationException("should not need to convert to object");
							}
						};
					}
				});
				item.add(new Label("attendeesInMeeting"));
				item.add(new Label("attendeesWaiting"));
				item.add(new AjaxLink<Void>("join") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						
					}
				});
			}
    		
    	});
    }
    
    @Override
    protected IModel<String> createContentHeaderLabelModel() {
    	return new Model<String>("Current meetings");
    }
}
