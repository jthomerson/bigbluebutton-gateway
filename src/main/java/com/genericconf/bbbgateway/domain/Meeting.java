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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

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

	private Collection<Attendee> waiters;
	private Collection<Attendee> attendees;

	private int maximumAttendees;

	private Date startTime;
	private Date endTime;

	public void addWaiter(Attendee att) {
		att.setJoinedWaitingRoomTime(new Date());
		ensureUniqueAttendeeName(att);
		getWaiters().add(att);

		int waitersAllowed = 0;
		for (Attendee waiter : getWaiters()) {
			if (waiter.isAllowedToJoin()) {
				waitersAllowed++;
			}
		}
		if ((getAttendeesInMeeting() + waitersAllowed) < maximumAttendees) {
			att.setAllowedToJoin(true);
		}
	}

	public void attendeeIsJoining(Attendee att) {
		final boolean removed = getWaiters().remove(att);
		att.setJoinedMeetingTime(new Date());
		if (removed) {
			ensureUniqueAttendeeName(att);
		} else {
			// someone joined straight from the API and was not in our waiting room
		}
		getAttendees().add(att);
	}

	private synchronized void ensureUniqueAttendeeName(Attendee att) {
		if (getAttendeeByName(att.getName()) != null) {
			att.setName(att.getName() + " - " + RandomStringUtils.randomAlphanumeric(4));
			ensureUniqueAttendeeName(att);
		}
	}

	public int getAttendeesInMeeting() {
		return getAttendees().size();
	}

	public int getAttendeesWaiting() {
		return getWaiters().size();
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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

	public Collection<Attendee> getWaiters() {
		if (waiters == null) {
			waiters = new ArrayList<Attendee>();
		}
		return waiters;
	}

	public void setWaiters(Collection<Attendee> waiters) {
		this.waiters = waiters;
	}

	public Collection<Attendee> getAttendees() {
		if (attendees == null) {
			attendees = new LinkedHashSet<Attendee>();
		}
		return attendees;
	}

	public void setAttendees(Collection<Attendee> attendees) {
		this.attendees = attendees;
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
		result = prime * result + ((attendees == null) ? 0 : attendees.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((logoutURL == null) ? 0 : logoutURL.hashCode());
		result = prime * result + maximumAttendees;
		result = prime * result + ((meetingID == null) ? 0 : meetingID.hashCode());
		result = prime * result + ((moderatorPassword == null) ? 0 : moderatorPassword.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((server == null) ? 0 : server.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((waiters == null) ? 0 : waiters.hashCode());
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
		if (attendees == null) {
			if (other.attendees != null)
				return false;
		} else if (!attendees.equals(other.attendees))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
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
		if (waiters == null) {
			if (other.waiters != null)
				return false;
		} else if (!waiters.equals(other.waiters))
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
		return "Meeting [attendeePassword=" + attendeePassword + ", attendees=" + attendees + ", endTime=" + endTime + ", logoutURL=" + logoutURL
				+ ", maximumAttendees=" + maximumAttendees + ", meetingID=" + meetingID + ", moderatorPassword=" + moderatorPassword + ", name="
				+ name + ", server=" + server + ", startTime=" + startTime + ", waiters=" + waiters + ", welcome=" + welcome + "]";
	}

	public Attendee getAttendeeByUniqueID(String uniqueID) {
		List<Attendee> atts = new ArrayList<Attendee>();
		atts.addAll(getAttendees());
		atts.addAll(getWaiters());
		for (Attendee att : atts) {
			if (att.getUniqueID().equals(uniqueID)) {
				return att;
			}
		}
		return null;
	}

	public Attendee getAttendeeByName(String name) {
		List<Attendee> atts = new ArrayList<Attendee>();
		atts.addAll(getAttendees());
		atts.addAll(getWaiters());
		for (Attendee att : atts) {
			if (att.getName().equals(name)) {
				return att;
			}
		}
		return null;
	}

	public void increaseMaximumAttendees(int howMany) {
		maximumAttendees += howMany;
	}

}
