package com.genericconf.bbbgateway.web.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.genericconf.bbbgateway.domain.Attendee;
import com.genericconf.bbbgateway.domain.Meeting;
import com.genericconf.bbbgateway.domain.Role;
import com.genericconf.bbbgateway.services.IMeetingService;
import com.genericconf.bbbgateway.web.components.JQueryFeedbackPanel;
import com.genericconf.bbbgateway.web.pages.WaitingRoom;

public class JoinMeetingFormPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private String attendeeName;
	private final Form<Void> form;
	
	public JoinMeetingFormPanel(String id, final IModel<Meeting> model) {
		super(id, model);
		setOutputMarkupId(true);
		
		form = new Form<Void>("joinForm");
		final FeedbackPanel fb = new JQueryFeedbackPanel("feedback", new ContainerFeedbackMessageFilter(form));
		fb.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
		form.add(fb);
		
		form.add(new TextField<String>("name", new PropertyModel<String>(this, "attendeeName")).setRequired(true));
		form.add(new AjaxButton("submit") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private IMeetingService meetingService;
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.addComponent(fb);
				onAjaxRequest(target);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				final Attendee att = new Attendee();
				att.setRole(Role.VIEWER);
				att.setName(attendeeName);
				
				meetingService.addToWaitingRoom(model.getObject(), att);
				setResponsePage(WaitingRoom.class, WaitingRoom.createPageParameters(model.getObject(), att));
			}
		});
		add(form);
	}
	
	public final void onAjaxRequest(AjaxRequestTarget target) {
		target.appendJavascript("initializeFormStuff('#" + form.getMarkupId() + " ');");
	}

}
