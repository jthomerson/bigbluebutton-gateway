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

package com.genericconf.bbbgateway.web.panels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import com.genericconf.bbbgateway.domain.Attendee;
import com.genericconf.bbbgateway.domain.Meeting;
import com.genericconf.bbbgateway.domain.Role;

public class AttendeeAndWaitingListPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	private final IModel<Meeting> meeting;
	private boolean allowAdminControls = false;
	
	public AttendeeAndWaitingListPanel(String id, IModel<Meeting> model) {
		super(id, model);
		this.meeting = model;
		setOutputMarkupId(true);
		
		add(new Label("attendeeCount", new PropertyModel<Integer>(meeting, "attendeesInMeeting")));
		add(new Label("waitingCount", new PropertyModel<Integer>(meeting, "attendeesWaiting")));
		
		add(createAttendeeList("attendees", new LoadableDetachableModel<List<? extends Attendee>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends Attendee> load() {
				List<Attendee> list = new ArrayList<Attendee>(meeting.getObject().getAttendees());
				Collections.sort(list);
				return list;
			}
		}));
		add(createAttendeeList("waiters", new LoadableDetachableModel<List<? extends Attendee>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends Attendee> load() {
				List<Attendee> list = new ArrayList<Attendee>(meeting.getObject().getWaiters());
				Collections.sort(list);
				return list;
			}
		}));
	}

	public void onAjaxRequest(AjaxRequestTarget target) {
		target.addComponent(this);
	}

	public AttendeeAndWaitingListPanel setAllowAdminControls(boolean allow) {
		this.allowAdminControls = allow;
		return this;
	}
	
	private Component createAttendeeList(final String id, IModel<? extends List<? extends Attendee>> listmodel) {
		ListView<Attendee> lv = new PropertyListView<Attendee>(id, listmodel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Attendee> item) {
				item.setOutputMarkupId(true);
				item.add(new Label("name"));
				item.add(new Label("userID") {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return getDefaultModelObject() != null;
					}
				});
				item.add(new Label("role"));
				
				final WebMarkupContainer admin = new WebMarkupContainer("admin") {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return "waiters".equals(id) && allowAdminControls;
					}
				};
				item.add(admin.setOutputMarkupId(true));
				admin.add(new AjaxLink<Void>("allowToJoin") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						item.getModelObject().setAllowedToJoin(true);
						target.addComponent(admin);
					}
					
					@Override
					protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
						final Attendee att = item.getModelObject();
						String text = att.isAllowedToJoin() ? "already allowed in" : "allow to join";
						replaceComponentTagBody(markupStream, openTag, text);
					}
					@Override
					public boolean isEnabled() {
						return item.getModelObject().isAllowedToJoin() == false;
					}
					
				});
				admin.add(new AjaxLink<Void>("swapRole") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						Role newRole = Role.VIEWER.equals(item.getModelObject().getRole()) ? Role.MODERATOR : Role.VIEWER;
						item.getModelObject().setRole(newRole);
						target.addComponent(AttendeeAndWaitingListPanel.this);
					}
					
					@Override
					protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
						final Attendee att = item.getModelObject();
						String text = Role.VIEWER.equals(att.getRole()) ? "make moderator" : "make viewer";
						replaceComponentTagBody(markupStream, openTag, text);
					}
					
				});
			}
		};
		lv.setOutputMarkupId(true);
		return lv;
	}

}
