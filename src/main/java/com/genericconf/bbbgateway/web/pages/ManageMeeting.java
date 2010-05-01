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

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genericconf.bbbgateway.TimerSettings;
import com.genericconf.bbbgateway.domain.Meeting;
import com.genericconf.bbbgateway.services.IMeetingService;
import com.genericconf.bbbgateway.web.components.DateTimeLabel;
import com.genericconf.bbbgateway.web.panels.AttendeeAndWaitingListPanel;

public class ManageMeeting extends BasePage {

	private static final Logger logger = LoggerFactory.getLogger(ManageMeeting.class);
	private static final String SALT = UUID.randomUUID().toString();
	
	@SpringBean
	private IMeetingService meetingService;
	
	private final DateTimeLabel checked;
	private final AttendeeAndWaitingListPanel attendeeList;
	private final Label maxAtts;
	
	public ManageMeeting(PageParameters params) {
		final String meetingID = params.getString("0");
		String check = params.getString("1");
		boolean okay = createCheck(meetingID).equals(check);
		logger.info("meeting ID: " + meetingID + "; check: " + check + "; okay: " + okay);
		if (!okay) {
			getSession().error("You are not authorized to manage that meeting");
			throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());
		}
		IModel<Meeting> model = new LoadableDetachableModel<Meeting>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Meeting load() {
				final Meeting mtg = meetingService.findByMeetingID(meetingID);
				if (mtg == null) {
					getSession().error("That meeting no longer exists");
					throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());
				}
				return mtg;
			}
		};
		setDefaultModel(new CompoundPropertyModel<Meeting>(model));

		add(new Label("name"));
		add(new Label("meetingID"));
		add(new Label("attendeePassword"));
		add(new Label("moderatorPassword"));
		add(maxAtts = new Label("maximumAttendees"));
		maxAtts.setOutputMarkupId(true);
		attendeeList = new AttendeeAndWaitingListPanel("attendeeList", getModel());
		add(attendeeList.setAllowAdminControls(true));

		checked = new DateTimeLabel("checkedTime", new AlwaysReturnCurrentDateModel());
		add(checked.setOutputMarkupId(true));
		
		final Form<Void> bulkAllowForm = new Form<Void>("bulkAllowForm");
		add(bulkAllowForm.setOutputMarkupId(true));
		final Model<Integer> howMany = new Model<Integer>(0);
		bulkAllowForm.add(new TextField<Integer>("howMany", howMany, Integer.class));
		bulkAllowForm.add(new AjaxButton("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (howMany.getObject() <= 0) {
					target.appendJavascript("alert('must be a positive number');");
				} else {
					final Meeting meeting = ManageMeeting.this.getModel().getObject();
					meeting.increaseMaximumAttendees(howMany.getObject());
					meetingService.bulkAllowAttendees(meeting);
					ManageMeeting.this.onPageAjaxRequest(target);
				}
			}
		});
		
		add(new AbstractAjaxTimerBehavior(Duration.seconds(TimerSettings.INSTANCE.getSecondsBetweenManageMeetingPagePolls())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTimer(AjaxRequestTarget target) {
				ManageMeeting.this.onPageAjaxRequest(target);
			}
		});
	}
	
	protected final void onPageAjaxRequest(AjaxRequestTarget target) {
		target.addComponent(checked);
		target.addComponent(maxAtts);
		attendeeList.onAjaxRequest(target);
	}

	@Override
	protected IModel<String> createContentHeaderLabelModel() {
		return new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return "Manage meeting: " + ManageMeeting.this.getModel().getObject().getName();
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	protected IModel<Meeting> getModel() {
		return (IModel<Meeting>) getDefaultModel();
	}
	
	public static PageParameters createParams(Meeting meeting) {
		PageParameters pp = new PageParameters();
		pp.put("0", meeting.getMeetingID());
		pp.put("1", createCheck(meeting.getMeetingID()));
		return pp;
	}

	private static String createCheck(String meetingID) {
		return DigestUtils.md5Hex(SALT + meetingID);
	}
}
