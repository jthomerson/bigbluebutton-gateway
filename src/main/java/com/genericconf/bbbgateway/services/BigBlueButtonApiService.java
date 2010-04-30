package com.genericconf.bbbgateway.services;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genericconf.bbbgateway.domain.ApiException;
import com.genericconf.bbbgateway.domain.Meeting;
import com.genericconf.bbbgateway.domain.Server;

public class BigBlueButtonApiService implements IBigBlueButtonApiService {
	
	private static final Logger logger = LoggerFactory.getLogger(BigBlueButtonApiService.class);

	@Override
	public void createMeeting(Meeting meeting) throws ApiException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", meeting.getName());
		params.put("meetingID", meeting.getMeetingID());
		params.put("welcome", meeting.getWelcome());
		params.put("logoutURL", meeting.getLogoutURL());
		
		String url = createApiCallUrl("create", meeting.getServer(), params);
		logger.info("URL for createMeeting: " + url);
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
		for(Entry<String, String> entry : params.entrySet()) {
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
