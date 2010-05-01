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

package com.genericconf.bbbgateway.wicket;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.IndexedHybridUrlCodingStrategy;
import org.apache.wicket.request.target.coding.IndexedParamUrlCodingStrategy;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import com.genericconf.bbbgateway.TimerSettings;
import com.genericconf.bbbgateway.web.pages.CreateMeeting;
import com.genericconf.bbbgateway.web.pages.HomePage;
import com.genericconf.bbbgateway.web.pages.ManageMeeting;
import com.genericconf.bbbgateway.web.pages.WaitingRoom;

public class GatewayApplication extends WebApplication {
	
	public GatewayApplication() {
	}

	public Class<HomePage> getHomePage() {
		return HomePage.class;
	}

	@Override
	protected void init() {
		addComponentInstantiationListener(new SpringComponentInjector(this));

		mount(new IndexedHybridUrlCodingStrategy("home", HomePage.class));
		mount(new IndexedHybridUrlCodingStrategy("create", CreateMeeting.class));
		mount(new IndexedParamUrlCodingStrategy("waiting-room", WaitingRoom.class));
		mount(new IndexedParamUrlCodingStrategy("manage", ManageMeeting.class));
		
		TimerSettings.INSTANCE.setDevelopment(DEVELOPMENT.equals(getConfigurationType()));
	}
}
