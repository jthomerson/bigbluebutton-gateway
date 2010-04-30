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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attendeePassword == null) ? 0 : attendeePassword.hashCode());
		result = prime * result + ((logoutURL == null) ? 0 : logoutURL.hashCode());
		result = prime * result + maximumAttendees;
		result = prime * result + ((meetingID == null) ? 0 : meetingID.hashCode());
		result = prime * result + ((moderatorPassword == null) ? 0 : moderatorPassword.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((server == null) ? 0 : server.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((welcome == null) ? 0 : welcome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Meeting other = (Meeting) obj;
		if (attendeePassword == null) {
			if (other.attendeePassword != null)
				return false;
		} else if (!attendeePassword.equals(other.attendeePassword))
			return false;
		if (logoutURL == null) {
			if (other.logoutURL != null)
				return false;
		} else if (!logoutURL.equals(other.logoutURL))
			return false;
		if (maximumAttendees != other.maximumAttendees)
			return false;
		if (meetingID == null) {
			if (other.meetingID != null)
				return false;
		} else if (!meetingID.equals(other.meetingID))
			return false;
		if (moderatorPassword == null) {
			if (other.moderatorPassword != null)
				return false;
		} else if (!moderatorPassword.equals(other.moderatorPassword))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (server == null) {
			if (other.server != null)
				return false;
		} else if (!server.equals(other.server))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (welcome == null) {
			if (other.welcome != null)
				return false;
		} else if (!welcome.equals(other.welcome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Meeting [attendeePassword=" + attendeePassword + ", logoutURL=" + logoutURL + ", maximumAttendees=" + maximumAttendees
				+ ", meetingID=" + meetingID + ", moderatorPassword=" + moderatorPassword + ", name=" + name + ", server=" + server + ", startTime="
				+ startTime + ", welcome=" + welcome + "]";
	}

}
