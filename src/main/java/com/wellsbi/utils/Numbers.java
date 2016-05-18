/**
 * 
 */
package com.wellsbi.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;


/**
 *
 * @author Hrishi Dixit [hrishi@wellsbi.com]
 *
 */
public class Numbers {
	static final Logger log = LoggerFactory.getLogger (Numbers.class);
	
	public static String phone (String unformatted) {
		if (StringUtils.isNotEmpty(unformatted)) {
//			log.info("Formatting {}", unformatted);
			try {
				return PhoneNumberUtil.getInstance().format( 
						PhoneNumberUtil.getInstance().parse(unformatted, "US"),
						PhoneNumberFormat.NATIONAL
					);
	
			} catch (NumberParseException e) {
				log.error("Error formatting phone number {}: {}", unformatted, e.getMessage());
			}
		}
		return unformatted;
	}
	
	public static BigDecimal percent (String s) {
		if (StringUtils.isEmpty(s)) return null;
		try {
			return BigDecimal.valueOf(
					NumberFormat.getPercentInstance().parse(s).doubleValue()
				).setScale(2, RoundingMode.HALF_EVEN);
		} catch (ParseException e) {
			log.warn("Error parsing {} as a percentage: {}", s, e.getMessage());
		}
		return null;
	}
	
	public static boolean isNegative (BigDecimal d) { return (d != null && d.signum() == -1); }

	public static BigDecimal round (BigDecimal d) { return d.setScale(2, RoundingMode.CEILING); } 
	public static BigDecimal zero() { return round (BigDecimal.ZERO); }
	public static BigDecimal nullZero (BigDecimal d) { return (d == null) ? zero() : round(d); }
	
	public static BigDecimal money (Double d) {
		if (d == null) return null;
		return money (BigDecimal.valueOf(d));
	}
	public static BigDecimal money (BigDecimal d) {
		if (d == null) return null;
		return d.setScale(2, RoundingMode.HALF_EVEN);
	}

}
