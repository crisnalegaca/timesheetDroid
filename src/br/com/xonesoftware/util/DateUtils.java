/**
 * 
 */
package br.com.xonesoftware.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author cris
 * Classe utilitária para manipulação de datas
 *
 */
public class DateUtils {
	
	private static SimpleDateFormat df = new SimpleDateFormat(
			"dd-MM-yyyy HH:mm");
	
	public static String today(){
		return getAsString(Calendar.getInstance());
	}

	public static String getAsString(Calendar date){
		return df.format(date.getTime());
	}
	
	public static Calendar getAsCalendar(String dateString) throws ParseException{
		Calendar date = Calendar.getInstance();
		date.setTime((Date) df.parse(dateString));
		return date;
	}
}
