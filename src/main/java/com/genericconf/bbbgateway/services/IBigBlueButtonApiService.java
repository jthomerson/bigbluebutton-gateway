package com.genericconf.bbbgateway.services;

import com.genericconf.bbbgateway.domain.ApiException;
import com.genericconf.bbbgateway.domain.Meeting;

public interface IBigBlueButtonApiService {

	public void createMeeting(Meeting meeting) throws ApiException;
	
}
