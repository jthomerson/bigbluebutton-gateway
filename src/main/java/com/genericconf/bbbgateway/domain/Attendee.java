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
import java.util.UUID;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class Attendee extends Entity implements Comparable<Attendee> {
	private static final long serialVersionUID = 1L;

	private String uniqueID = UUID.randomUUID().toString();
	private String userID;
	private String name;
	private Role role;

	private Date joinedWaitingRoomTime;
	private Date joinedMeetingTime;
	private boolean allowedToJoin = false;

	public String getPassword(Meeting meeting) {
		return Role.MODERATOR.equals(role) ? meeting.getModeratorPassword() : meeting.getAttendeePassword();
	}

	public String getUserID() {
		return userID;
	}

	public String getUniqueID() {
		return uniqueID;
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
		this.joinedWaitingRoomTime = joinedWaitingRoomTime;
	}

	public Date getJoinedMeetingTime() {
		return joinedMeetingTime;
	}

	public void setJoinedMeetingTime(Date joinedMeetingTime) {
		this.joinedMeetingTime = joinedMeetingTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (allowedToJoin ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
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
