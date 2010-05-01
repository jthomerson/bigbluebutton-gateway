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

import org.apache.commons.lang.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genericconf.bbbgateway.TimerSettings;

public class Attendee extends Entity implements Comparable<Attendee> {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(Attendee.class);
	
	private String userID;
	private String name;
	private Role role;

	private long lastPingTime = 0l;
	private Date joinedWaitingRoomTime;
	private Date joinedMeetingTime;
	private boolean allowedToJoin = false;

	public String getPassword(Meeting meeting) {
		return Role.MODERATOR.equals(role) ? meeting.getModeratorPassword() : meeting.getAttendeePassword();
	}
	
	public boolean isTimedOut() {
		if (lastPingTime == 0l) {
			return false;
		}
		final int elapsed = (int) ((System.currentTimeMillis() - lastPingTime) / 1000);
		final boolean timedOut = elapsed > TimerSettings.INSTANCE.getSecondsWithNoPingThatIndicatesTimeOut();
		logger.debug("attendee: {}, last ping: {}, elapsed: {}, threshold: {}, timed out: {}", new Object[] { name, lastPingTime, elapsed, TimerSettings.INSTANCE.getSecondsWithNoPingThatIndicatesTimeOut(), timedOut });
		return timedOut;
	}
	
	public void pinged() {
		logger.debug("attendee: {}, PING!", new Object[] { name });
		lastPingTime = System.currentTimeMillis();
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setAllowedToJoin(boolean allowedToJoin) {
		this.allowedToJoin = allowedToJoin;
	}

	public boolean isAllowedToJoin() {
		return allowedToJoin;
	}

	public Date getJoinedWaitingRoomTime() {
		return joinedWaitingRoomTime;
	}

	public void setJoinedWaitingRoomTime(Date joinedWaitingRoomTime) {
		pinged();
		this.joinedWaitingRoomTime = joinedWaitingRoomTime;
	}

	public Date getJoinedMeetingTime() {
		return joinedMeetingTime;
	}

	public void setJoinedMeetingTime(Date joinedMeetingTime) {
		pinged();
		this.joinedMeetingTime = joinedMeetingTime;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
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
		Attendee other = (Attendee) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Attendee [allowedToJoin=" + allowedToJoin + ", name=" + name + ", role=" + role + ", userID=" + userID + "]";
	}

	@Override
	public int compareTo(Attendee o) {
		return new CompareToBuilder().append(role, o.role).append(joinedWaitingRoomTime, o.joinedWaitingRoomTime).append(joinedMeetingTime,
				o.joinedMeetingTime).append(name, o.name).toComparison();
	}

}
