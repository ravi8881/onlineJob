package com.main.interconnection.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.apache.solr.common.util.DateUtil;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrDateUtil extends JsonSerializer<Date>{

	static Object dateObject = null;
	static Object formatdate = null;
	private static final Logger logger = LoggerFactory.getLogger(SolrDateUtil.class);

	public static String addDateToSolr(String parseDate) {
		SimpleDateFormat dateFormatSimple = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormatSolr = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		try {
			dateObject = dateFormatSimple.parse(parseDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date dateString = (Date) dateObject;
		return dateFormatSolr.format(dateString);
	}
	public static String addDateToSolrWithUtilDate(Date parseSqlDate) {

		SimpleDateFormat dateFormatSimple = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormatSolr = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		try {

			String parseDate = dateFormatSimple.format(parseSqlDate);
			dateObject = dateFormatSimple.parse(parseDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date dateString = (Date) dateObject;
		return dateFormatSolr.format(dateString);
	}

	public static String addDateFromSolrToServiceforPdate(Object SolrDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String solrDate = dateFormat.format(SolrDate);

		try {
			formatdate = dateFormat.parse(solrDate);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		Date FormatTimeStamp = (Date) formatdate;
		SimpleDateFormat dateFormatToService = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormatToService.format(FormatTimeStamp);
	}

	public static String addDateDifferenceFromSolrToServiceforPdate(
			Object solrDate) {
		if (null != solrDate&&!"".equals(solrDate)) {
			Date date1 = convertSolrDateToDate(solrDate);
			Date currentDate = new Date();
			try {
				return calculateDifference(date1, currentDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return solrDate.toString();
	}
	/**
	 * This will convert solr(2014-07-04T01:58:43Z) date format to date object
	 * @param solrDate(2014-07-04T01:58:43Z)
	 * @return Date
	 */
	public static Date convertSolrDateToDate(Object solrDate) {
		if (null != solrDate) {
			try {
				return DateUtil.parseDate(solrDate.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * Return difference in dates as Days,Hours,Minutes,seconds.
	 * @param a : Date object
	 * @param b : Date Object
	 * @return
	 * @throws Exception
	 */
	public static String calculateDifference(Date a, Date b) throws Exception{
		Calendar earlier = Calendar.getInstance();
		Calendar later = Calendar.getInstance();
		if(null!=a&&null!=b){
			a = getDateByTimeZone(a);
			b = getDateByTimeZone(b);
		}
		if (a.compareTo(b) < 0) {
			earlier.setTime(a);
			later.setTime(b);
		} else {
			earlier.setTime(b);
			later.setTime(a);
		}
		Date date1 = earlier.getTime();
		Date date2 = later.getTime();
		// Time Difference Calculations Begin
		long milliSec1 = date1.getTime();
		long milliSec2 = date2.getTime();
		long timeDifInMilliSec;
		if (milliSec1 >= milliSec2) {
			timeDifInMilliSec = milliSec1 - milliSec2;
		} else {
			timeDifInMilliSec = milliSec2 - milliSec1;
		}

		long timeDifSeconds = timeDifInMilliSec / 1000 % 60;
		long timeDifMinutes = timeDifInMilliSec / (60 * 1000) % 60;
		long timeDifHours = timeDifInMilliSec / (60 * 60 * 1000) % 24;
		long timeDifDays = timeDifInMilliSec / (24 * 60 * 60 * 1000);
		if (timeDifDays > 0 && timeDifDays <= 7) {
			/*if (timeDifDays == 1)
				return "" + timeDifDays + " Day" + " Ago ";
			return "" + timeDifDays + " Days" + " Ago ";*/
			SimpleDateFormat dateFormatToService = new SimpleDateFormat(
					"MMM dd,yyyy");
			return dateFormatToService.format(date2);
		} else if (timeDifHours > 0 && timeDifHours < 24) {
				if (timeDifHours == 1)
				return "" + timeDifHours + " Hour" + " Ago ";
			return "" + timeDifHours + " Hours" + " Ago ";
		} else if (timeDifMinutes > 0 & timeDifMinutes < 60) {
			if (timeDifMinutes == 1)
			return "" + timeDifMinutes + " Minute" + " Ago ";
			return "" + timeDifMinutes + " Minutes" + " Ago ";
		} else if (timeDifSeconds > 0 && timeDifSeconds < 60) {
				if (timeDifSeconds == 1)
				return "" + timeDifSeconds + " Second" + " Ago ";
			return "" + timeDifSeconds + " Seconds" + " Ago ";
		} else {
			/*SimpleDateFormat dateFormatToService = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			return dateFormatToService.format(date1);*/
			return "Just Now";
		}

	}
	/**
	 * Return new date object based on the time zone
	 * @param date
	 * @return
	 */
	public static Date getDateByTimeZone(Date date) {
		Calendar cal = Calendar.getInstance();
		TimeZone zone1 = cal.getTimeZone();
		int tzt1 = zone1.getOffset(System.currentTimeMillis());
		return new Date(date.getTime() - tzt1);
	}
	
	public static String convertSolrDateToSimpleDate(String solrDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = df.parse(solrDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d.toString();
	}
	/*
	 * @author Name:Gaurav chugh
	 * @Created Date:17/10/2014
	 * @update Date:17/10/2014
	 * @purpose:convertSQLDateToSolrDate 
	 *  
	 */
	public static String addDateToSolrWithSQLDate(String parseSqlDate) {

		SimpleDateFormat dateFormatSimple = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFormatSolr = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		try {
			dateObject = dateFormatSimple.parse(parseSqlDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date dateString = (Date) dateObject;
		return dateFormatSolr.format(dateString);
		}
	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = dateFormat.format(date);
		gen.writeString(formattedDate);
	}

	/*
	 * @author :rajiv kumar
	 * @created Date:21/11/2014
	 * @purose:add Date Difference From Solr To Service for Update Bo
	 * 
	 */
	public static String addDateDifferenceFromSolrToServiceforUpdatedate(
			Object solrDate) {
		if (null != solrDate&&!"".equals(solrDate)) {
			Date date1 = convertSimpleDateFormatToDate(solrDate);
			Date currentDate = new Date();
			try {
				return calculateDifferenceStringDate(date1, currentDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return solrDate.toString();
	}
	private static String calculateDifferenceStringDate(Date date1, Date currentDate) {
		// TODO Auto-generated method stub
		//in milliseconds
		SimpleDateFormat dateFormatToService = new SimpleDateFormat(
				"MMM dd,yyyy");
		long diff = currentDate.getTime() - date1.getTime();
		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);

		logger.info(diffDays + " days, ");
		logger.info(diffHours + " hours, ");
		logger.info(diffMinutes + " minutes, ");
		logger.info(diffSeconds + " seconds.");
		if(diffDays>10)
			return dateFormatToService.format(currentDate);
		else if (diffDays > 0 && diffDays <= 7) {
			if (diffDays == 1)
				return "" + diffDays + " Day" + " Ago ";
			return "" + diffDays + " Days" + " Ago ";
		
			
		} else if (diffHours > 0 && diffHours < 24) {
			if (diffHours == 1)
				return "" + diffHours + " Hour" + " Ago ";
			return "" + diffHours + " Hours" + " Ago ";
		} else if (diffMinutes > 0 & diffMinutes < 60) {
			if (diffMinutes == 1)
			return "" + diffMinutes + " Minute" + " Ago ";
			return "" + diffMinutes + " Minutes" + " Ago ";
		} else if (diffSeconds > 0 && diffSeconds < 60) {
				if (diffSeconds == 1)
				return "" + diffSeconds + " Second" + " Ago ";
			return "" + diffSeconds + " Seconds" + " Ago ";
		} else {
			
			return "Just Now";
		}
		
	
	}
	/*
	 * @author :rajiv kumar
	 * @created Date:21/11/2014
	 * @purose:convertSimpleDateFormatToDate Update Bo
	 * 
	 */
	public static Date convertSimpleDateFormatToDate(Object solrDate)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date date = null;
		try {
			 date = dateFormat.parse(solrDate.toString());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return date;
	}
	
	/*
	 * @author :rajiv kumar
	 * @created Date:27/11/2014
	 * @purose:User Update Since
	 * 
	 */
	public static String userUpdateSincePdate(Object solrDate) {
		// TODO Auto-generated method stub
		if (null != solrDate&&!"".equals(solrDate)) {
			Date date1 = convertSolrDateToDate(solrDate);
			Date currentDate = new Date();
			try {
				return calculateDifferenceUser(date1, currentDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return solrDate.toString();
	}
	/*
	 * @author :rajiv kumar
	 * @created Date:27/11/2014
	 * @purose:calculate Difference User from create date to New Date
	 * 
	 */
	private static String calculateDifferenceUser(Date a, Date b) {
		// TODO Auto-generated method stub
		// in milliseconds
		Date createUserDate = a;
		Date nowUserDate = b;
		String output = "";
		// in milliseconds
		long diff = nowUserDate.getTime() - createUserDate.getTime();
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);

		if (diffDays <= 0) {
			output = "Today";
		} else {
			if (diffDays > 0 && diffDays <= 10) {
				if (diffDays == 1) {
					output = "" + diffDays + " Day" + " Ago ";
					return output;
				} else {
					output = "" + diffDays + " Days" + " Ago ";
					return output;
				}
			}

			if (diffDays > 10) {
				SimpleDateFormat dateFormatToService = new SimpleDateFormat(
						"MMM dd,yyyy");

				return dateFormatToService.format(b);
			}
		}
		return output;

	}
}
