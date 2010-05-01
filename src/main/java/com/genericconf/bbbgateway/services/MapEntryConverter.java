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
