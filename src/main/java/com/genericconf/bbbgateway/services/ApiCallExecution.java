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

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import com.genericconf.bbbgateway.domain.ApiException;
import com.genericconf.bbbgateway.domain.Attendee;
import com.genericconf.bbbgateway.domain.Role;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;

public abstract class ApiCallExecution {
	
	final boolean execute() throws ApiException {
		try {
			return doExecute();
		} catch (Exception e) {
			throw new ApiException("error calling BigBlueButton API: " + e.getMessage(), e);
		}

	}

	@SuppressWarnings("unchecked")
	final Map<String, Object> getXmlFromApi(HttpClient httpClient, String url) throws Exception {
		CharSequence rawxml = makeHttpRequest(httpClient, url);
		XStream xs = new XStream();
		xs.registerConverter(new MapEntryConverter(xs.getMapper()));
		xs.registerConverter(new CollectionConverter(xs.getMapper()));
		xs.alias("response", Map.class);
		xs.alias("attendees", List.class);
		xs.addImplicitCollection(Map.class, "attendees");
		xs.alias("attendee", Attendee.class);
		xs.alias("returncode", String.class);
		xs.aliasField("fullName", Attendee.class, "name");
		xs.alias("fullName", String.class);
		xs.alias("userID", String.class);
		xs.alias("role", Role.class);
		xs.alias("startTime", Date.class);
		xs.alias("endTime", Date.class);
		return (Map<String, Object>) xs.fromXML(rawxml.toString());
	}
	
	final boolean wasSuccess(Map<String, Object> response) {
		return "SUCCESS".equals(response.get("returncode"));
	}

	final CharSequence makeHttpRequest(HttpClient httpClient, String url) throws Exception {
        HttpGet httpget = new HttpGet(url); 
        System.out.println("executing request " + httpget.getURI());
        // Create a response handler
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpClient.execute(httpget, responseHandler);
        System.out.println("Response: " + responseBody);
        return responseBody;
	}
	
	abstract boolean doExecute() throws Exception;

}
