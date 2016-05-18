/**
 * 
 */
package com.wellsbi.utils;

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author Hrishi Dixit [hrishi@wellsbi.com]
 *
 */
public class TimesAndDates {
	public static final String GRANULAR = "hh:mm a z 'on' MMMM dd, yyyy";
	public static final String COARSE = "MMM dd, yyyy";
	public static final String SQLDATE  = "yyyy-MM-dd"; 
	public static final String SQLTIME  = "yyyy-MM-dd hh:mm:ss"; 
	
	public static String granular (Date d) {
		return format (d, GRANULAR, false);
	}
	
	public static String coarse (Date d) {
		return format (d, COARSE, true);
	}

	public static String sqldate (Date d) {
		return format (d, SQLDATE, true);
	}
	
	public static String sqltime (Date d) {
		return format (d, SQLTIME, false);
	}

	public static String format (Date d, String f, boolean dateOnly) {
		return format (d, f, Defaults.TZ, dateOnly);
	}

	public static String format (Date d, String f, String z, boolean dateOnly) {
		if(d == null) return "-";
		if (dateOnly) {
			return DateTimeFormat.forPattern(f)
					.print(new LocalDate(d.getTime()).toDate().getTime());
		} else {
			return DateTimeFormat.forPattern(f)
					.withZone(DateTimeZone.forID(z))
					.print(new DateTime(d.getTime()).getMillis());
		}
	}
	
	public static Date from (String formatted, String format) {
		return DateTimeFormat.forPattern(format).parseDateTime(formatted).toDate();
		
	}

	public static Date fromSqlDate(String yyyyMMdd) {
		return from (yyyyMMdd, SQLDATE);
		
	}
	
	public static long secondsBetween(Date d1, Date d2) {
		if (d2.before(d1)) return 0L;
		return (d2.getTime()-d1.getTime()) / 1000L;
	}
	
	public static long minutesBetween (Date d1, Date d2) {
		return secondsBetween(d1, d2) / 60L;
	}
	
	public static long hoursBetween (Date d1, Date d2) {
		return minutesBetween(d1, d2) / 60L;
	}
	
	public static Date now () {
		return new LocalDateTime().toDate();
	}

	public static Date today () {
		return new LocalDate().toDate();
	}

	public static Date yesterday () {
		return daysBefore(1);
	}
	
	public static Date tomorrow () {
		return daysAfter(1);
	}

	public static Date firstDayOfThisMonth () {
		return new LocalDate().dayOfMonth().withMinimumValue().toDate();
	}
	
	public static Date lastDayOfThisMonth () {
		return new LocalDate().dayOfMonth().withMaximumValue().toDate();
	}

	public static Date firstDayOfThisYear () {
		return new LocalDate().monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().toDate();
	}
	
	public static Date lastDayOfThisYear () {
		return new LocalDate().monthOfYear().withMaximumValue().dayOfMonth().withMaximumValue().toDate();
	}
	
	public static Date daysBefore(int days) {
		return new LocalDate().minusDays(days).toDate();
	}
	
	public static Date daysAfter(int days) {
		return new LocalDate().plusDays(days).toDate();
	}

	public static String window (Date start, Date end) {
		if(start == null || end == null) { 
			return null; 
		}

		DateTime startDt = new DateTime(start.getTime());
		DateTime endDt = new DateTime(end.getTime());

		if(Days.daysBetween(startDt, endDt).getDays() < 14) {
			return Days.daysBetween(startDt, endDt).getDays() + " days";
		} else if(Weeks.weeksBetween(startDt, endDt).getWeeks() < 8) {
			return Weeks.weeksBetween(startDt, endDt).getWeeks() + " weeks";
		} else {
			return Months.monthsBetween(startDt, endDt).getMonths() + " months";
		}
	}
	
	public static DateTime after(Integer seconds) {
		return new DateTime().plusSeconds(seconds); //.toDate();
	}
	
	public static boolean isToday(Date d) {
		return new DateTime(d).toLocalDate().equals(new LocalDate());
	}

	public static boolean isFirstDayOfMonth(Date d) {
		return new DateTime(d).dayOfMonth().get() == new DateTime(d).dayOfMonth().withMinimumValue().dayOfMonth().get();
	}

	public static boolean isLastDayOfMonth(Date d) {
		return new DateTime(d).dayOfMonth().get() == new DateTime(d).dayOfMonth().withMaximumValue().dayOfMonth().get();
	}
	
	public static List<Interval> monthSplits (Date s, Date e) {
		/* TODO: make sure s is before e */
		/* dial back to the the 1st of the start month */
		DateTime start = new DateTime(s).dayOfMonth().withMinimumValue();
		
		/* set this to the last day of the preceding month, unless it is the last day of the current month */
		DateTime end = new DateTime(e).dayOfMonth().withMaximumValue();
		boolean lastDayOfMonth = isLastDayOfMonth (end.toDate());
		if (! lastDayOfMonth) {
			/* set end to end of previous month */
			end = end.minusMonths(1).dayOfMonth().withMaximumValue();
		}
		
		List<Interval> intervals = new ArrayList<Interval>();
		DateTime pointer = start;
		while (pointer.isBefore(end.getMillis())) {
            intervals.add (new Interval(pointer.getMillis(), pointer.dayOfMonth().withMaximumValue().getMillis()));
            /* move the pointer to the first of the next month */
            pointer = pointer.plusMonths(1).dayOfMonth().withMinimumValue();
        }
		if (!lastDayOfMonth) {
			/* pointer should be pointing to 1st of the month for e */
			intervals.add (new Interval(pointer.getMillis(), new DateTime(e).minusDays(1).getMillis()));
		}
        return intervals;	
	}
}
