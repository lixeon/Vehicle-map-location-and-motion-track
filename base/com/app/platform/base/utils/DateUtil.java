/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jul 2, 2011
 */
package com.app.platform.base.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.app.platform.base.exception.BaseSystemException;

/**
 * Date Utils Operations
 *
 * @author colen
 * 
 */
public final class DateUtil {

	/**
	 * Constructor
	 * 
	 */
	private DateUtil() {}

	/** date format: yyyy-MM-dd HH:mm:ss */
	public static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
	/** date format: yyyy-MM-dd */
	public static final String DATE_FORMAT_F = "yyyy-MM-dd";
	/** date format: yyyyMMdd */
    public static final String DATE_FORMAT_FS = "yyyyMMdd";
	/** date format： MMdd */
	public static final String DATE_FORMAT_3 = "MMdd";
	/** date format: yyyyMM */
    public static final String DATE_FORMAT_FYM = "yyyyMM";
	/** date format: yyyyMMddHHmmss */
	public static final String DATE_FORMAT_4 = "yyyyMMddHHmmss";
	/** date format: HH:mm */
	public static final String DATE_FORMAT_HSF = "HH:mm";
	
    public static final String DATE_FORMAT_FSALL = "yyyyMMddHHmmssSSS";	
	/**
	 * Change format
	 *
	 * @param date
	 * @param oldFmt
	 * @param newFmt
	 * @return
	 */
	public static String chgFmt(String date, String oldFmt, String newFmt) {
	    Date d = toDate(date, oldFmt);
	    SimpleDateFormat fmt = new SimpleDateFormat(newFmt);
	    return fmt.format(d);
	}
	
	/**
	 * format date
	 *
	 * @param date
	 * @param fmt
	 * @return
	 */
	public static String format(Date date, String fmt) {
	    SimpleDateFormat fmter = new SimpleDateFormat(fmt);
        return fmter.format(date);
	}
	
	
	/**
	 * Get Date
	 *
	 * @param format
	 * @param offset
	 * @return
	 */
	public static String getDate(String format, int offset) {
        return getTime(format, Calendar.DATE, offset);
	}
	
	
	/**
	 * get current date with format
	 *
	 * @param format format
	 * @return
	 */
	public static String getDate(String format) {
	    return getDate(format, 0);
	}
	
	
	/**
	 * format date
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static java.util.Date toDate(String date, String format) {
	    SimpleDateFormat fmt = new SimpleDateFormat(format);
	    try {
            return fmt.parse(date);
        } catch (ParseException e) {
            throw new BaseSystemException("date format exception for :" + date);
        }
	}
	
	
	/**
	 * Get Time
	 * 
	 * @param format Format
	 * @param offsetType Offset type
	 * @param offset offset.
	 * @return String.
	 */
	public static String getTime(String format, int offsetType, int offset) {
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.add(offsetType, offset);
		return fmt.format(cal.getTime());
	}

	public static Date getDate(int offsetType, int offset) {
	    Calendar cal = Calendar.getInstance();
        cal.add(offsetType, offset);
        return cal.getTime();
	}
	
   public static Date getDateS(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.DATE, offset);
        return cal.getTime();
    }

	/**
	 * Compare specified time to Current time by the Day offset.
	 * 
	 * @param time time
	 * @param dayoffset day offset
	 * @return compare result
	 */
	public static boolean compareTime(long time, int dayoffset) {
		Calendar curtime = GregorianCalendar.getInstance(Locale.getDefault());
		curtime.add(Calendar.DAY_OF_MONTH, dayoffset);
		// OUT >>>
		return curtime.getTimeInMillis() >= time ? true : false;
	}


	/**
	 * Get Current Time
	 * 
	 * @return current time.
	 */
	public static Timestamp getCurrentTime() {
		return new Timestamp(Calendar.getInstance(Locale.CHINA)
				.getTimeInMillis());
	}


	/**
	 * Get Current Date
	 *
	 * @return current date
	 */
	public static Date getCurrentDate() {
		return Calendar.getInstance(Locale.CHINA).getTime();
	}

	/**
	 * Is Date In.
	 * 
	 * @param currentDate Current date.
	 * @param startDate Start date.
	 * @param endDate End date.
	 * @return boolean.
	 */
	public static boolean isDateIn(String currentDate, String startDate,
			String endDate) {
		if (currentDate.compareTo(startDate) >= 0
				&& currentDate.compareTo(endDate) <= 0) {
			return true;
		}
		return false;
	}
	
	 public static String UTCToCST(String UTCStr, String format, int offset) throws ParseException {
	        
			Date date = null;
	        SimpleDateFormat sdf = new SimpleDateFormat(format);
	        date = sdf.parse(UTCStr);
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        calendar.add(Calendar.HOUR,offset);
	        return sdf.format(calendar.getTime()); 
	    }

	public static void main(String[] args) {
	    System.out.println(format(new Date(), "HH点mm分"));
	}
}
