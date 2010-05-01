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
