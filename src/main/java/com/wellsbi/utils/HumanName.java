package com.wellsbi.utils;

public class HumanName {

	private String first;
	private String middle;
	private String last;

	public HumanName(String first, String middle, String last) {
		this.first = first;
		this.middle = middle;
		this.last = last;
	}
	
	public String first() { return first; 	}
	public String middle() { return middle; }
	public String last() { return last; }
}
