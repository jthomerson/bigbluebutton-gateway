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

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genericconf.bbbgateway.domain.ApiException;
import com.genericconf.bbbgateway.domain.Attendee;
import com.genericconf.bbbgateway.domain.Meeting;
import com.genericconf.bbbgateway.domain.Server;

public class BigBlueButtonApiService implements IBigBlueButtonApiService {

	private static final Logger logger = LoggerFactory.getLogger(BigBlueButtonApiService.class);
	

	private final HttpClient httpClient = new DefaultHttpClient();

	// When HttpClient instance is no longer needed, shut down the connection
	// manager to ensure immediate deallocation of all system resources
	// httpclient.getConnectionManager().shutdown();

	public boolean updateMeeting(final Meeting meeting) throws ApiException {
		logger.info("updating meeting from api: " + meeting.getMeetingID());
		Map<String, String> params = new HashMap<String, String>();
		params.put("meetingID", meeting.getMeetingID());
		params.put("password", meeting.getModeratorPassword());

		final String url = createApiCallUrl("getMeetingInfo", meeting.getServer(), params);
		logger.debug("URL for getMeetingInfo: " + url);
		boolean success = execute(new ApiCallExecution() {
			@Override
			@SuppressWarnings({ "unchecked" })
			boolean doExecute() throws Exception {
				DateFormat dates = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
				
				Map<String, Object> xml = getXmlFromApi(httpClient, url);
				logger.info("getMeetingInfo response: " + xml);
				boolean success = wasSuccess(xml);
				if (success) {
					final String startStr = (String) xml.get("startTime");
					Date start = isDateNull(startStr) ? null : dates.parse(startStr);
					final String endStr = (String) xml.get("endTime");
					Date end = isDateNull(endStr) ? null : dates.parse(endStr);
					boolean running = (Boolean) xml.get("running");
					meeting.setStartTime(start);
					logger.info("meeting [" + meeting.getMeetingID() + "] start: " + start + "; end: " + end + "; running: " + running);

					List<Attendee> atts = (List<Attendee>) xml.get("attendees");
					for (Attendee att : atts) {
						Attendee ourAtt = meeting.getAttendeeByName(att.getName());
						if (ourAtt == null) {
							meeting.attendeeIsJoining(att);
						} else {
							ourAtt.setUserID(att.getUserID());
						}
					}
				}
				return success;
			}
		});
		return success;
	}
	
	protected boolean isDateNull(String str) {
		return StringUtils.isBlank(str) || "null".equals(str);
	}

	public String createJoinMeetingURL(Meeting meeting, Attendee att) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("fullName", att.getName());
		params.put("meetingID", meeting.getMeetingID());
		params.put("password", att.getPassword(meeting));

		return createApiCallUrl("join", meeting.getServer(), params);
	}
	
	@Override
	public void createMeeting(final Meeting meeting) throws ApiException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", meeting.getName());
		params.put("meetingID", meeting.getMeetingID());
		params.put("welcome", meeting.getWelcome());
		params.put("logoutURL", meeting.getLogoutURL());

		final String url = createApiCallUrl("create", meeting.getServer(), params);
		logger.info("URL for createMeeting: " + url);

		boolean success = execute(new ApiCallExecution() {
			@Override
			boolean doExecute() throws Exception {
				Map<String, Object> xml = getXmlFromApi(httpClient, url);
				boolean success = wasSuccess(xml);
				if (success) {
					String attPW = (String) xml.get("attendeePW");
					String modPW = (String) xml.get("moderatorPW");
					meeting.setAttendeePassword(attPW);
					meeting.setModeratorPassword(modPW);
				}
				return success;
			}
		});

		logger.info("meeting: " + meeting);
		if (!success) {
			throw new RuntimeException("unknown error made API call unsuccessful");
		}
	}

	private boolean execute(ApiCallExecution ace) throws ApiException {
		return ace.execute();
	}

	private String createApiCallUrl(String callName, Server server, Map<String, String> params) {
		StringBuffer sb = new StringBuffer();
		String context = server.getContext();
		if (context.startsWith("/") == false) {
			context = "/" + context;
		}
		if (context.endsWith("/") == false) {
			context += "/";
		}
		sb.append("http://").append(server.getDomain()).append(context).append(callName).append("?");
		String queryString = createQueryString(params);
		sb.append(queryString).append("&checksum=").append(createChecksum(server, queryString));
		return sb.toString();
	}

	@SuppressWarnings("deprecation")
	private String createQueryString(Map<String, String> params) {
		StringBuffer sb = new StringBuffer();
		boolean amp = false;
		for (Entry<String, String> entry : params.entrySet()) {
			if (entry.getValue() == null) {
				continue;
			}
			if (amp) {
				sb.append('&');
			}
			sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue()));
			amp = true;
		}
		return sb.toString();
	}

	private String createChecksum(Server server, String queryString) {
		return DigestUtils.shaHex(queryString + server.getSecuritySalt());
	}

}
