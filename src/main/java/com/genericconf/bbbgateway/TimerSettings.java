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



public class TimerSettings {

	public static final TimerSettings INSTANCE = new TimerSettings();
	
	private boolean dev = false;
	private TimerSettings() {
		// no-op
	}

	private boolean isDev() {
		return dev;
	}
	
	// MeetingService:
	// NOTE: these will only be called before app is started, resulting in non-dev values always
	public int getSecondsBetweenMeetingUpdateRuns() { return isDev() ? 15 : 45; }
	public int getSecondsBeforeFirstMeetingUpdateRun() { return isDev() ? 15 : 45; }
	public int getSecondsBeforeMeetingIsRemovedAfterEnding() { return 15 * 60; } /* 15 minutes */
	
	// ManageMeeting page:
	public int getSecondsBetweenManageMeetingPagePolls() { return isDev() ? 5 : 20; }
	
	// WaitingRoom page:
	public int getSecondsBetweenWaitingRoomPagePolls() { return isDev() ? 5 : 20; }
	public int getSecondsBeforeFirstWaitingRoomPagePoll() { return isDev() ? 2 : 2; }
	public int getSecondsWithNoPingThatIndicatesTimeOut() { return isDev() ? 15 : 90; }

	// HomePage :
	public int getSecondsBetweenHomePagePolls() { return getSecondsBetweenWaitingRoomPagePolls(); }

	public void setDevelopment(boolean devMode) {
		this.dev = devMode;
	}

}
