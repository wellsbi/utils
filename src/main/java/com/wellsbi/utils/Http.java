package com.wellsbi.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

public class Http {
	/* consts */
	public static final String CONTENT_TYPE_JSON		= "application/json";
	public static final String CONTENT_TYPE_FORM 		= "application/x-www-form-urlencoded";
	public static final String CONTENT_TYPE_MULTIPART 	= "multipart/form-data";

	public static void getdump (HttpClient client, HttpGet get) {
		/* dump client */
	}
	
	public static void postdump (HttpClient client, HttpGet get) {
		
	}

	
	public static class Request {
		/**
		 * Returns the headers in an http request as a map
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public static Map<String,String> headers (HttpServletRequest request) {
			Map<String,String> headerMap = new HashMap<>();
			
			// null check
			if (request == null) return headerMap;
			
		    Enumeration<String> names = (Enumeration<String>) request.getHeaderNames();
		    while (names.hasMoreElements()) {
		    	String name = names.nextElement();
		    	Enumeration values = request.getHeaders(name); // support multiple values
		    	if (values != null) {
		    		while (values.hasMoreElements()) {
		            	String value = (String) values.nextElement();
		            	headerMap.put(name, value);
		            }
		        }
		    }
		    return headerMap;
		}
	
		public static String params (HttpServletRequest request) {
			if(request == null) {
				return null;
			}
			
			Map<String, String[]> paramMap = request.getParameterMap();
			if(MapUtils.isEmpty(paramMap)) {
				return null;
			} else {
				StringBuilder sb = new StringBuilder();
				for(String name : paramMap.keySet()) {
					String[] vals = paramMap.get(name);
					if(vals != null) {
						for(String value : vals) sb.append(name).append("=").append(value).append("&");
					}
				}
				return StringUtils.removeEnd (sb.toString(), "&");
			}
		}
	
		/**
		 * Returns the value of a header in the request, or null if the header was not set in the request
		 */
		public static String getHeader (final String header, HttpServletRequest request) {
			return headers (request).get(header);
		}
	
		/**
		 * Checks if the specified header is set in the request
		 */
		public static boolean isHeaderSet (final String header, HttpServletRequest request) {
			return getHeader(header, request) != null;
		}
		
		/**
		 * Returns the cookies in the request as a Map
		 */
		public static Map<String,Cookie> cookies (HttpServletRequest request) {
			Map<String,Cookie> cookieMap = new HashMap<>();
			if (request == null) {
			    return cookieMap;
			}
	
			Cookie[] cookies = request.getCookies();
			if(!ArrayUtils.isEmpty(cookies)) {
			    for (Cookie cookie : cookies) {
			        cookieMap.put(cookie.getName(), cookie);
			    }
			}
			return cookieMap;
		}
	
		/**
		 * Returns the value for the specified cookie in the request, or null if the cookie is not set
		 */
		public static Cookie cookie (final String name, HttpServletRequest request) {
			return cookies(request).get(name);
		}
	
		/**
		 * Gets the value of the cookie in the request, or null if cookie is not set.
		 */
		public static String cookieValue (final String name, HttpServletRequest request) {
			Cookie cookie = cookie (name, request);
			return (cookie == null) ? null : cookie.getValue();
		}
		
		/**
		 * Checks if the specified cookie is set in the request
		 */
		public static boolean isCookieSet (final String name, HttpServletRequest request) {
			return cookie(name, request) != null;
		}
	
		/**
		 * Removes the specified cookie
		 */
		public static boolean removeCookie(Cookie cookie,
				HttpServletRequest request, 
				HttpServletResponse response) {
			if (cookie != null)
				return removeCookie (
						cookie.getName(),
						cookie.getPath(),
						cookie.getDomain(),
						request,
						response
					);
			return true;
		}
	

		/**
		 * Removes the specified cookie
		 */
		public static boolean removeCookie(
				final String cookieName, 
				final String cookiePath, 
				final String domain,
				HttpServletRequest request, 
				HttpServletResponse response) {
	
			if (response != null) {
				Cookie cookie = new Cookie(cookieName, null);
				if (cookie != null) {
					cookie.setPath(cookiePath);
					//Age zero deletes the cookie.
					cookie.setMaxAge(0);
					if(domain != null) cookie.setDomain(domain);
					response.addCookie(cookie);
				} 
				return true;
			} 
			return false;
		}
	
		public static String ipaddress (HttpServletRequest request) {
	        String ip = request.getHeader("X-FORWARDED-FOR");
	        return ip == null ? request.getRemoteAddr() : ip;
		}
	
		public static String printCookie (Cookie cookie) {
			return new StringBuilder("Cookie: ")
				.append("Name = ").append(cookie.getName()).append(", ")
				.append("Value = ").append(cookie.getValue()).append(", ")
				.append("Domain = ").append(cookie.getDomain()).append(", ")
				.append("Path = ").append(cookie.getPath()).append(", ")
				.append("Secure = ").append(cookie.getSecure()).append(", ")
				.append("HttpOnly = ").append(cookie.isHttpOnly()).append(", ")
				.append("Max Age = ").append(cookie.getMaxAge()).toString();
		}
		
		
	}
	
	
	public static class Response {
		
	}
}
