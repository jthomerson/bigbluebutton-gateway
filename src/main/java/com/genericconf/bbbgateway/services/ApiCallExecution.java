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

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.genericconf.bbbgateway.domain.ApiException;

public abstract class ApiCallExecution {
	
	final boolean execute() throws ApiException {
		try {
			return doExecute();
		} catch (Exception e) {
			throw new ApiException("error calling BigBlueButton API: " + e.getMessage(), e);
		}

	}

	final Document getXmlFromApi(HttpClient httpClient, String url) throws Exception {
		CharSequence xml = makeHttpRequest(httpClient, url);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xml.toString()));
		Document doc = db.parse(is);
		return doc;
	}
	
	final boolean wasSuccess(Document doc) {
		boolean success = "SUCCESS".equals(doc.getElementsByTagName("returncode").item(0).getTextContent());
		return success;
	}

	final CharSequence makeHttpRequest(HttpClient httpClient, String url) throws Exception {
        HttpGet httpget = new HttpGet(url); 
        System.out.println("executing request " + httpget.getURI());
        // Create a response handler
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpClient.execute(httpget, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");
        return responseBody;
	}
	
	abstract boolean doExecute() throws Exception;

}
