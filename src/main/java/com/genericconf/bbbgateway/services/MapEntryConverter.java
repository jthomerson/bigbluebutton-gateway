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

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverterWrapper;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public final class MapEntryConverter implements Converter {

	private final XStream xstream;
	
	public MapEntryConverter(XStream xs) {
		this.xstream = xs;
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Map<String, Object> map = new HashMap<String, Object>();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            final String nodeName = reader.getNodeName();
            final String valstr = reader.getValue();
            Object val = valstr;
            // TODO: fix this:
            
            final Class<?> clazz = xstream.getMapper().realClass(nodeName);
            final Converter converter = xstream.getConverterLookup().lookupConverterForType(clazz);
            if (clazz != null && (converter instanceof SingleValueConverterWrapper) == false) {
            	val = context.convertAnother(map, clazz, converter);
            } else if (StringUtils.isEmpty(valstr) || "null".equals(valstr)) {
            	val = null;
            } else if ("true".equals(valstr.toLowerCase())) {
            	val = true;
            } else if ("false".equals(valstr.toLowerCase())) {
            	val = false;
            }
			map.put(nodeName, val);
            reader.moveUp();
        }
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean canConvert(Class type) {
		return AbstractMap.class.isAssignableFrom(type);
	}
	
}
