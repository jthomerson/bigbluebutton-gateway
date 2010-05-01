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

import java.text.SimpleDateFormat;
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
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.ClassAliasingMapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public abstract class ApiCallExecution {

	public static final XStream XSTREAM;
	
	static {
		final XStream xs = new XStream(new DomDriver()) {
			@Override
			protected MapperWrapper wrapMapper(final MapperWrapper next) {
				return new MapperWrapper(next) {
					@SuppressWarnings("unchecked")
					@Override
					public Class realClass(String elementName) {
						final ClassAliasingMapper cam = (ClassAliasingMapper) next.lookupMapperOfType(ClassAliasingMapper.class);
						final boolean found = cam.aliasIsAttribute(elementName);
						return found ? super.realClass(elementName) : String.class;
					}
					
				};
			}
		};
		xs.registerConverter(new MapEntryConverter(xs));
		xs.registerConverter(new CollectionConverter(xs.getMapper()));
		xs.registerConverter(new IntegerConverter());
		xs.registerConverter(new DateConverter(new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")));
		xs.alias("response", Map.class);
		xs.alias("attendees", List.class);
		xs.addImplicitCollection(Map.class, "attendees");
		xs.alias("attendee", Attendee.class);
		xs.aliasField("fullName", Attendee.class, "name");
		xs.alias("role", Role.class);
		xs.alias("running", Boolean.class);
		xs.alias("participantCount", Integer.class);
		xs.alias("moderatorCount", Integer.class);
		xs.alias("startTime", Date.class);
		xs.alias("endTime", Date.class);
		XSTREAM = xs;
	}
	
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
		return (Map<String, Object>) XSTREAM.fromXML(rawxml.toString());
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
