package com.genericconf.bbbgateway.web.components;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class JQueryFeedbackPanel extends FeedbackPanel {
	private static final long serialVersionUID = 1L;

	public JQueryFeedbackPanel(String id) {
		super(id);
	}

	public JQueryFeedbackPanel(String id, IFeedbackMessageFilter filter) {
		super(id, filter);
	}

	@Override
	protected String getCSSClass(FeedbackMessage message) {
		return message.getLevel() >= FeedbackMessage.ERROR ? "ui-state-error" : "ui-state-highlight";
	}

}
