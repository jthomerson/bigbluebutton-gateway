package com.genericconf.bbbgateway.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.util.convert.ConversionException;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DateConverter implements Converter {

	private final DateFormat dateFormat;
	
	public DateConverter(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		throw new UnsupportedOperationException("not yet supported");
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		final String val = reader.getValue();
		if (StringUtils.isEmpty(val) || "null".equals(val.toLowerCase())) {
			return null;
		}
		synchronized (dateFormat) {
			try {
				return dateFormat.parse(val);
			} catch (ParseException e) {
				throw new ConversionException("error unmarshalling date: " + val, e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean canConvert(Class type) {
		return Date.class.isAssignableFrom(type);
	}

}
