/**
 * 
 */
package com.wellsbi.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author Hrishi Dixit [hrishi@wellsbi.com]
 *
 */
public class Money {
	static final Logger log = LoggerFactory.getLogger (Money.class);
	
	public static BigDecimal round (BigDecimal d) { return d.setScale(2, RoundingMode.CEILING); } 
	public static BigDecimal nullZero (BigDecimal d) { return (d == null) ? zero() : d; }
	public static BigDecimal zero() { return round (BigDecimal.ZERO); }
	public static boolean equal (BigDecimal d1, BigDecimal d2) { return nullZero(d1).doubleValue() == nullZero(d2).doubleValue(); }
	public static boolean gte (BigDecimal d1, BigDecimal d2) {  return nullZero(d1).doubleValue() >= nullZero(d2).doubleValue(); }
	public static boolean lte (BigDecimal d1, BigDecimal d2) {  return nullZero(d1).doubleValue() <= nullZero(d2).doubleValue(); }
	public static BigDecimal plus(BigDecimal d1, BigDecimal d2) { return round(nullZero(d1).add(round(nullZero(d2)))); }
	public static BigDecimal minus(BigDecimal d1, BigDecimal d2) { return round(nullZero(d1).subtract(round(nullZero(d2)))); }
	public static boolean equals (BigDecimal d1, BigDecimal d2) { return round(d1).doubleValue() == round(d2).doubleValue(); }
	public static boolean isZero (BigDecimal d) { return equals(d, zero()); }
	public static String format (BigDecimal d) { return NumberFormat.getCurrencyInstance().format(round(d).doubleValue()); }
	
	public static BigDecimal sum(BigDecimal...d) {
		if (d == null || d.length == 0) return zero();
		return Arrays.asList(d).stream().reduce(zero(), (_acc, _d) -> plus(_acc, _d));
	}
}
