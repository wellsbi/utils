/**
 * 
 */
package com.wellsbi.utils;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.ImmutableMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Hrishi Dixit [hrishi@wellsbi.com]
 *
 */
public class Data {
	// TODO: move to annotation later
	private static final Logger log = LoggerFactory.getLogger (Data.class);
	
	
	/**
	 * Nothing much, just generate a UUID
	 * 
	 * @return
	 */
	public static String uuid() { return uuid (false); }
	
	public static String uuid(boolean stripHyphens) { 
		if (stripHyphens) return StringUtils.remove(UUID.randomUUID().toString(), "-");
		return UUID.randomUUID().toString(); 
	}

	/**
     * Generates an immutable map from a variable set of arguments (has to be even)
     * 
     * @param entries
     * @return
     * @throws Exception
     */
    public static Map<String, Object> map (Object...entries) { return map (Object.class, entries); }


    /**
     * Generates an immutable map from a variable set of arguments (has to be even)
     * 
     * @param entries
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public static <V> Map<String, V> map (Class<V> valueClass, Object...entries) {
    	if (entries == null || entries.length == 0) { return ImmutableMap.<String, V>of(); }
    	
    	int extent = entries.length;
    	// has to be an even number of args (k1, v1, k2, v2, etc)
    	if (entries.length % 2 != 0) {
    		log.warn ("Wrong number of arguments ({}) - needs to be even. " +
    				"Last argument {} will be discarded", entries.length, entries[entries.length-1]);	
    		extent = entries.length-1; 
    	}
    	
    	ImmutableMap.Builder<String, V> builder = new ImmutableMap.Builder<String, V>();
    	int p = 0;
    	while (p < extent) { builder.put(entries[p++].toString(), (V)entries[p++]); }

    	return builder.build();
    }

}

