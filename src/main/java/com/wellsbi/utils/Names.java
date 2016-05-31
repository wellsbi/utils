package com.wellsbi.utils;

import org.apache.commons.lang3.StringUtils;

public class Names {

	private Names() {}
	
	
	public static HumanName humanizeName (String name) {
		String[] names = StringUtils.split(name);
		String fn = "", mn = "", ln = "";
		if (names.length == 2) {
			fn = names[0];
			ln = names[1];
		} else if (names.length >= 3) {
			fn = names[0];
			mn = names[1];
			ln = names[2];
		} else {
			fn = mn = ln = name;
		}
		
		return new HumanName (fn, mn, ln);
	}
	
	
}
