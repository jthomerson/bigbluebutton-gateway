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
