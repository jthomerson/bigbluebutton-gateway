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

package com.genericconf.bbbgateway.domain;

import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

public class Meeting extends Entity {

	private static final long serialVersionUID = 1L;

	private Server server;

	private String name;
	private String meetingID;
	private String attendeePassword;
	private String moderatorPassword;
	private String welcome;
	private String logoutURL;

	private int maximumAttendees;
	private Date startTime;

	public int getAttendeesInMeeting() {
		return new Random().nextInt(100);
	}
	
	public int getAttendeesWaiting() {
		return new Random().nextInt(100);
	}
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMeetingID() {
		if (meetingID == null) {
			this.meetingID = RandomStringUtils.randomAlphanumeric(8);
		}
		return meetingID;
	}

	public void setMeetingID(String meetingID) {
		this.meetingID = meetingID;
	}

	public String getAttendeePassword() {
		return attendeePassword;
	}

	public void setAttendeePassword(String attendeePassword) {
		this.attendeePassword = attendeePassword;
	}

	public String getModeratorPassword() {
		return moderatorPassword;
	}

	public void setModeratorPassword(String moderatorPassword) {
		this.moderatorPassword = moderatorPassword;
	}

	public String getWelcome() {
		return welcome;
	}

	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}

	public String getLogoutURL() {
		return logoutURL;
	}

	public void setLogoutURL(String logoutURL) {
		this.logoutURL = logoutURL;
	}

	public Server getServer() {
		if (server == null) {
			this.server = new Server();
		}
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public void setMaximumAttendees(int maximumAttendees) {
		this.maximumAttendees = maximumAttendees;
	}

	public int getMaximumAttendees() {
		return maximumAttendees;
	}

}
