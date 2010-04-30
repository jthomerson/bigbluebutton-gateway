package com.genericconf.bbbgateway.services;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public final class MapEntryConverter implements Converter {

	private final Mapper mapper;
	
	public MapEntryConverter(Mapper mapper) {
		this.mapper = mapper;
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
            if ("attendees".equals(nodeName)) {
            	val = context.convertAnother(map, List.class);
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
