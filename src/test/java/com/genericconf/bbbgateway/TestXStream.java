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

package com.genericconf.bbbgateway;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import com.genericconf.bbbgateway.services.ApiCallExecution;
import com.thoughtworks.xstream.XStream;


public class TestXStream {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		XStream xs = ApiCallExecution.XSTREAM;
		File file = new File("/home/jrthomerson/Desktop/xml-with-attendees.xml");
		file = new File("/home/jrthomerson/Desktop/xml-without-attendees.xml");
		final Map<String, Object> res = (Map<String, Object>) xs.fromXML(new FileInputStream(file));
		System.out.println(res);
		System.out.println("list = " + res.get("attendees").getClass() + " = " + res.get("attendees"));
		System.out.println("date = " + res.get("startTime").getClass() + " = " + res.get("startTime"));
		System.out.println("bool = " + res.get("running").getClass() + " = " + res.get("running"));
		System.out.println("int  = " + res.get("moderatorCount").getClass() + " = " + res.get("moderatorCount"));
	}
}
