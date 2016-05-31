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
	static final Gson PRETTY = new GsonBuilder().setPrettyPrinting().create();
	static final Gson LEAN = new GsonBuilder().create();
	
	public static <T> String pretty (T t) { return PRETTY.toJson(t); }
	
	public static <T> String lean (T t) { return LEAN.toJson(t); }
	
	public static <T> byte[] bytes (T t) { return lean(t).getBytes(); }
	
	public static <T> T hydrate (String json, Class<T> clazz) { return LEAN.fromJson (json, clazz); }
}
