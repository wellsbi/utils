/**
 * 
 */
package com.wellsbi.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Hrishi Dixit [hrishi@wellsbi.com]
 *
 */
public class Emails {
	static final Logger log = LoggerFactory.getLogger (Emails.class);
	
	public static String domain (String email) {
		String[] split = StringUtils.split(email, "@");
		if (split == null || split.length != 2) return null;
		return split[1];
				
	}
	
}
