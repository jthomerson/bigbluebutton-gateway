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

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.genericconf.bbbgateway.domain.Meeting;
import com.genericconf.bbbgateway.services.IMeetingService;

public class CreateMeeting extends BasePage {

	public CreateMeeting() {
		Form<Meeting> form = new Form<Meeting>("form", new CompoundPropertyModel<Meeting>(new Model<Meeting>(new Meeting()))) {
			private static final long serialVersionUID = 1L;
			
			@SpringBean
			private IMeetingService meetingService;
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				meetingService.createMeeting(getModelObject());
				setResponsePage(ManageMeeting.class, ManageMeeting.createParams(getModelObject()));
			}
		};
		
		form.add(new TextField<String>("server.domain").setRequired(true));
		form.add(new TextField<String>("server.context").setRequired(true));
		form.add(new TextField<String>("server.securitySalt").setRequired(true));
		form.add(new TextField<String>("name").setRequired(true));
		
		add(form);
	}

	@Override
	protected IModel<String> createContentHeaderLabelModel() {
		return new Model<String>("Create a meeting");
	}
}
