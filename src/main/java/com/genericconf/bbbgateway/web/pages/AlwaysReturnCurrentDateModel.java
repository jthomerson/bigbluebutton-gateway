package com.genericconf.bbbgateway.web.pages;

import java.util.Date;

import org.apache.wicket.model.AbstractReadOnlyModel;

public class AlwaysReturnCurrentDateModel extends AbstractReadOnlyModel<Date> {
	private static final long serialVersionUID = 1L;

	@Override
	public Date getObject() {
		return new Date();
	}

}
