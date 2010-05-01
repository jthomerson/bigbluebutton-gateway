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

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.genericconf.bbbgateway.domain.Attendee;
import com.genericconf.bbbgateway.domain.Meeting;
import com.genericconf.bbbgateway.services.IMeetingService;

public class JoinMeetingLinkPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public JoinMeetingLinkPanel(String id, final IModel<Meeting> meeting, final IModel<Attendee> attendee) {
		super(id);
		setOutputMarkupId(true);

		add(new Link<Void>("join") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private IMeetingService meetingService;
			
			@Override
			public void onClick() {
				String url = meetingService.joinMeeting(meeting.getObject(), attendee.getObject());
				throw new RestartResponseAtInterceptPageException(new RedirectPage(url));
			}
			
		});
	}

}
