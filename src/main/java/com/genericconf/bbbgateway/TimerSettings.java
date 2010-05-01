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

package com.genericconf.bbbgateway;

import org.apache.wicket.Application;


public class TimerSettings {

	public static final TimerSettings INSTANCE = new TimerSettings();
	
	private TimerSettings() {
		// no-op
	}

	private boolean isDev() {
		return Application.exists() && Application.DEVELOPMENT.equals(Application.get().getConfigurationType());
	}
	
	// MeetingService: 
	// (NOTE: these will currently always be non-dev since they are initialized when the app doesn't yet exist)
	public int getSecondsBetweenMeetingUpdateRuns() { return isDev() ? 15 : 45; }
	public int getSecondsBeforeFirstMeetingUpdateRun() { return isDev() ? 15 : 45; }
	
	// ManageMeeting page:
	public int getSecondsBetweenManageMeetingPagePolls() { return isDev() ? 15 : 30; }
	
	// WaitingRoom page:
	public int getSecondsBetweenWaitingRoomPagePolls() { return isDev() ? 15 : 90; }
	public int getSecondsBeforeFirstWaitingRoomPagePoll() { return isDev() ? 2 : 2; }

}
