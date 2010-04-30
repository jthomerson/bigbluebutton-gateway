package com.genericconf.bbbgateway.wicket;

import org.apache.wicket.protocol.http.WebApplication;

import com.genericconf.bbbgateway.web.pages.HomePage;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see com.genericconf.bbbgateway.Start#main(String[])
 */
public class GatewayApplication extends WebApplication {
	/**
	 * Constructor
	 */
	public GatewayApplication() {
	}

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	public Class<HomePage> getHomePage() {
		return HomePage.class;
	}

}
