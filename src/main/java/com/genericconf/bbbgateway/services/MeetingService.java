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

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.genericconf.bbbgateway.domain.ApiException;
import com.genericconf.bbbgateway.domain.Meeting;

public class MeetingService implements IMeetingService {

	private final Map<String, Meeting> meetings = new HashMap<String, Meeting>();
	private IBigBlueButtonApiService apiService;

	// INTERFACE METHODS
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

	public Collection<Meeting> getMeetings() {
		Collection<Meeting> vals = null;
		synchronized (meetings) {
			vals = meetings.values();
		}
		return Collections.unmodifiableCollection(vals);
	}

	// IoC Methods
	public IBigBlueButtonApiService getApiService() {
		return apiService;
	}

	public void setApiService(IBigBlueButtonApiService apiService) {
		this.apiService = apiService;
	}

}
