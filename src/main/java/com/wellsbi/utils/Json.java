/**
 * 
 */
package com.wellsbi.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Hrishi Dixit [hrishi@wellsbi.com]
 *
 */
public class Json {
	static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static String pretty (Object bean) {
		return GSON.toJson(bean);
	}
}
