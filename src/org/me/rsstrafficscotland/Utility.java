package org.me.rsstrafficscotland;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Utility {

	@SuppressLint("SimpleDateFormat")
	public static String FormatDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE, dd MMM yyyy - HH:mm");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {

			e.printStackTrace();
		}

		SimpleDateFormat newDate = new SimpleDateFormat("d/M/yyyy");
		String formatedD = newDate.format(d.getTime());
		return formatedD;
	}
	
	public static long dateToTimestamp(String string) {
		Calendar cal = new GregorianCalendar(TimeZone.getDefault());
		
		String[] sArray = string.split("/");
		int day = Integer.parseInt(sArray[0]);
		int month = Integer.parseInt(sArray[1]);
		int year = Integer.parseInt(sArray[2]);
		
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return (cal.getTimeInMillis() / 1000);
	}
	
}
