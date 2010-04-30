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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.genericconf.bbbgateway.domain.ApiException;
import com.genericconf.bbbgateway.domain.Meeting;
import com.genericconf.bbbgateway.domain.Server;

public class BigBlueButtonApiService implements IBigBlueButtonApiService {

	private static final Logger logger = LoggerFactory.getLogger(BigBlueButtonApiService.class);

	private final HttpClient httpClient = new DefaultHttpClient();

	// When HttpClient instance is no longer needed, shut down the connection
	// manager to ensure immediate deallocation of all system resources
	// httpclient.getConnectionManager().shutdown();

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
				Document xml = getXmlFromApi(httpClient, url);
				boolean success = wasSuccess(xml);
				if (success) {
					String attPW = xml.getElementsByTagName("attendeePW").item(0).getTextContent();
					String modPW = xml.getElementsByTagName("moderatorPW").item(0).getTextContent();
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
		for (Entry<String, String> entry : params.entrySet()) {
			if (entry.getValue() == null) {
				continue;
			}
			sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue()));
		}
		return sb.toString();
	}

	private String createChecksum(Server server, String queryString) {
		return DigestUtils.shaHex(queryString + server.getSecuritySalt());
	}

}
