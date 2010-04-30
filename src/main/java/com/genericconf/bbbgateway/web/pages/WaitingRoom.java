package com.genericconf.bbbgateway.web.pages;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import com.genericconf.bbbgateway.domain.Attendee;
import com.genericconf.bbbgateway.domain.Meeting;
import com.genericconf.bbbgateway.services.IMeetingService;
import com.genericconf.bbbgateway.web.components.DateTimeLabel;
import com.genericconf.bbbgateway.web.panels.AttendeeAndWaitingListPanel;
import com.genericconf.bbbgateway.web.panels.JoinMeetingLinkPanel;

public class WaitingRoom extends BasePage {

	// TODO: update this before release:
	private static final int WAIT_SECONDS = 6;

	@SpringBean
	private IMeetingService meetingService;

	private final IModel<Meeting> meeting;
	private final IModel<Attendee> attendee;

	public WaitingRoom(PageParameters params) {
		final String meetingID = params.getString("0");
		final String attUniqID = params.getString("1");
		if (StringUtils.isEmpty(meetingID) || StringUtils.isEmpty(attUniqID)) {
			throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());
		}
		meeting = new LoadableDetachableModel<Meeting>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Meeting load() {
				return meetingService.findByMeetingID(meetingID);
			}
		};
		attendee = new LoadableDetachableModel<Attendee>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Attendee load() {
				return meeting.getObject().getAttendeeByUniqueID(attUniqID);
			}
		};

		add(new Label("name", new PropertyModel<String>(meeting, "name")));
		add(new Label("meetingID", new PropertyModel<String>(meeting, "meetingID")));

		add(new Label("attName", new PropertyModel<String>(attendee, "name")));
		add(new Label("attRole", new PropertyModel<String>(attendee, "role")));

		final AttendeeAndWaitingListPanel attendeeList = new AttendeeAndWaitingListPanel("attendeeList", meeting);
		add(attendeeList);

		final DateTimeLabel checked = new DateTimeLabel("checkedTime", new AlwaysReturnCurrentDateModel());
		add(checked.setOutputMarkupId(true));

		final WebMarkupContainer joinDialog = new WebMarkupContainer("joinDialog");
		add(joinDialog.setOutputMarkupId(true));

		add(new AbstractAjaxTimerBehavior(Duration.seconds(5)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTimer(AjaxRequestTarget target) {
				target.addComponent(checked);
				setUpdateInterval(Duration.seconds(WAIT_SECONDS));
				attendeeList.onAjaxRequest(target);
				final Attendee att = attendee.getObject();
				if (att.isAllowedToJoin()) {
					stop();

					final JoinMeetingLinkPanel panel = new JoinMeetingLinkPanel(joinDialog.getId(), meeting, attendee);
					joinDialog.replaceWith(panel);
					target.addComponent(panel);

					StringBuffer js = new StringBuffer().append("$('#").append(panel.getMarkupId()).append(
							"').dialog({ modal: true, title: 'Join Meeting' });");
					target.appendJavascript(js.toString());
				}
			}
		});
	}

	@Override
	protected IModel<String> createContentHeaderLabelModel() {
		return new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return "Waiting to join: " + meeting.getObject().getName();
			}
		};
	}

	public static PageParameters createPageParameters(Meeting meeting, Attendee att) {
		PageParameters pp = new PageParameters();
		pp.add("0", meeting.getMeetingID());
		pp.add("1", att.getUniqueID());
		return pp;
	}
}
