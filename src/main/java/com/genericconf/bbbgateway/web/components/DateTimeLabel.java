package com.genericconf.bbbgateway.web.components;

import java.util.Date;
import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

public class DateTimeLabel extends Label {
	private static final long serialVersionUID = 1L;

	public DateTimeLabel(String id, String label) {
		super(id, label);
	}

	public DateTimeLabel(String id) {
		super(id);
	}

	public DateTimeLabel(String id, IModel<?> model) {
		super(id, model);
	}

	@Override
	public IConverter getConverter(Class<?> type) {
		return new IConverter() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("deprecation")
			@Override
			public String convertToString(Object value, Locale locale) {
				return ((Date) value).toGMTString();
			}

			@Override
			public Object convertToObject(String value, Locale locale) {
				throw new UnsupportedOperationException("should not need to convert to object");
			}
		};
	}
}
