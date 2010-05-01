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

package com.genericconf.bbbgateway.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genericconf.bbbgateway.TimerSettings;
import com.genericconf.bbbgateway.domain.ApiException;
import com.genericconf.bbbgateway.domain.Attendee;
import com.genericconf.bbbgateway.domain.Meeting;

public class MeetingService implements IMeetingService {

	private static final Logger logger = LoggerFactory.getLogger(MeetingService.class);
	
	private final Map<String, Meeting> meetings = new HashMap<String, Meeting>();
	private final Timer timer;
	private IBigBlueButtonApiService apiService;
	
	public MeetingService() {
		TimerTask updateMeetings = new TimerTask() {
    		@Override
    		public void run() {
    			logger.info("update meetings timer task running");
    			for (Meeting meeting : meetings.values()) {
    				tryToUpdateMeeting(meeting);
    			}
    		}
    	};

    	timer = new Timer(true);
    	long timerDelay = TimerSettings.INSTANCE.getSecondsBeforeFirstMeetingUpdateRun() * 1000;
    	long timerInterval = TimerSettings.INSTANCE.getSecondsBetweenMeetingUpdateRuns() * 1000;
    	logger.info("starting timer to update meetings every {} millis, after an initial delay of {} millis", new Object[] { timerInterval, timerDelay });
		timer.scheduleAtFixedRate(updateMeetings, timerDelay, timerInterval);
	}
	
	// INTERFACE METHODS
	@Override
	public void bulkAllowAttendees(Meeting meeting) {
		tryToUpdateMeeting(meeting);
		
		List<Attendee> waiters = new ArrayList<Attendee>(meeting.getWaiters());
		final int allowIn = Math.max(0, (meeting.getMaximumAttendees() - meeting.getAttendeesInMeeting()));
		Collections.sort(waiters);
		waiters = waiters.subList(0, Math.min(waiters.size(), allowIn));
		for (Attendee att : waiters) {
			att.setAllowedToJoin(true);
		}
	}

	private void tryToUpdateMeeting(Meeting meeting) {
		try {
			// update our waiting room attendees first:
			Set<Attendee> toRemove = new HashSet<Attendee>();
			for (Attendee att : meeting.getWaiters()) {
				if (att.isTimedOut()) {
					toRemove.add(att);
				}
			}
			logger.debug("waiting room attendees to remove: " + toRemove);
			for (Attendee rem : toRemove) {
				logger.debug("removing waiting room attendee: " + rem);
				meeting.removeAttendee(rem);
			}
			
			// now update actual attendees from api:
			apiService.updateMeeting(meeting);
		} catch (ApiException e) {
			logger.error("error updating meeting: " + e.getMessage(), e);
		}
	}

	public void addToWaitingRoom(Meeting meeting, Attendee att) {
		meeting.addWaiter(att);
	}
	
	public String joinMeeting(Meeting meeting, Attendee att) {
		meeting.attendeeIsJoining(att);
		return apiService.createJoinMeetingURL(meeting, att);
	}
	
	public void createMeeting(Meeting meeting) {
		synchronized (meetings) {
			if (meetings.containsKey(meeting.getMeetingID())) {
				throw new RuntimeException("Meeting with that meeting ID already exists.  Please try again");
			}
		}

		meeting.setStartTime(new Date());
		try {
			apiService.createMeeting(meeting);
		} catch (ApiException e) {
			throw new RuntimeException("error trying to create meeting on the BigBlueButton server: " + e.getMessage(), e);
		}

		synchronized (meetings) {
			meetings.put(meeting.getMeetingID(), meeting);
		}
	}
	
	@Override
	public Meeting findByMeetingID(String meetingID) {
		synchronized (meetings) {
			return meetings.get(meetingID);
		}
	}

	public Collection<Meeting> getMeetings() {
		synchronized (meetings) {
			return Collections.unmodifiableCollection(meetings.values());
		}
	}

	// IoC Methods
	public IBigBlueButtonApiService getApiService() {
		return apiService;
	}

	public void setApiService(IBigBlueButtonApiService apiService) {
		this.apiService = apiService;
	}

}
