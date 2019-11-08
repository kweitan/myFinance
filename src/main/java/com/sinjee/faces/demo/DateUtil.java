package com.sinjee.faces.demo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class DateUtil {

	/**
	 * 转换评论的日期
	 * 
	 * @param sDate
	 * @return
	 */
	public static String conevertCommentDateStr(String sDate, Date crawlDate) {

		Date hasYearDate = null;
		SimpleDateFormat df = new SimpleDateFormat("MM月dd日HH:mm");
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			hasYearDate = df1.parse(sDate);
		} catch (ParseException e) {

		}
		if (hasYearDate != null) {
			return sDate;
		}

		// //没有年份的：例如（5月20日13:30）
		Date noneYearDate = null;

		try {
			noneYearDate = df.parse(sDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block

		}
		if (noneYearDate != null) {
			// 当前系统的年份
			Calendar ca = Calendar.getInstance();
			ca.setTime(new Date());
			int year = ca.get(Calendar.YEAR);
			ca.setTime(noneYearDate);
			ca.set(Calendar.YEAR, year);
			return df1.format(ca.getTime());

		}
		// 几分钟前的，例如（55分钟前）
		if (sDate.indexOf("分钟前") > -1) {
			try {
				// 当前系统的年份
				Calendar ca = Calendar.getInstance();
				ca.setTime(crawlDate);

				int minute = Integer.parseInt(sDate.substring(0, sDate
						.lastIndexOf("分钟前")));
				ca.add(Calendar.MINUTE, -minute);
				return df1.format(ca.getTime());
			} catch (Exception e) {

			}
		}
		// 今天的:例如（今天16:38）
		if (sDate.indexOf("今天") > -1) {
			try {
				String today = df2.format(crawlDate);

				return today + " " + sDate.substring(2, sDate.length());

			} catch (Exception e) {

			}
		}
		return "";

	}

	private static Date DTStringtoDate(String dtToDate) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date datetime = formatter.parse(dtToDate, pos);
		java.sql.Timestamp ts = null;

		if (datetime != null) {

			ts = new java.sql.Timestamp(datetime.getTime());
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(df.format(ts));
		return ts;
	}
	
	public static long getCompareDate(String startTime,String endTime) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(startTime.trim().length() == 10) startTime += " 00:00:00"; 
		if(endTime.trim().length() == 10) endTime += " 00:00:00"; 
		Date startDate = formatter.parse(startTime);
		
		Date endDate = formatter.parse(endTime);
		return endDate.getTime() - startDate.getTime();
	}
	
	public static long getCompareDate2(String startTime,String endTime) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(startTime.trim().length() == 10) startTime += " 00:00:00"; 
		if(endTime.trim().length() == 10) endTime += " 00:00:00"; 
		Date startDate = formatter.parse(startTime);
		
		Date endDate = formatter.parse(endTime);
		return endDate.getTime() - startDate.getTime();
	}

	public static boolean isBefore(Date date1, Date date2) {
		if (date1.getTime() > date2.getTime()) {

			return false;
		} else {
			return true;
		}
	}

	public static boolean isAfter(Date date1, Date date2) {
		if (date1.getTime() < date2.getTime()) {

			return false;
		} else {
			return true;
		}
	}

	/**
	 * 格式化日期
	 * 
	 * @param timeMillis
	 * @param formatType
	 *            0:yyyy-MM-dd 1:yyyy-MM-dd HH:mm 2:MM月dd日HH:mm 3:yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String DateFormat(long timeMillis, int formatType) {
		String val = "";
		try {
			String dateFormate = null;
			switch (formatType) {
			case 1:
				dateFormate = "yyyy-MM-dd  HH:mm";
				break;
			case 2:
				dateFormate = "MM月dd日HH:mm";
				break;
			case 3:
				dateFormate = "yyyy-MM-dd HH:mm:ss";
				break;
			default:
				dateFormate = "yyyy-MM-dd";
				break;
			}

			SimpleDateFormat df = new SimpleDateFormat(dateFormate);
			val = df.format(new Date(timeMillis)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			val = Long.toString(timeMillis);
		}
		return val;
	}
	
	/**
	 *  当天时间之前的时间
	 * @param timeMillis
	 * @return
	 */
	public static boolean isBeforetoday(long timeMillis)
	{
		// 当天时间的开始  当天00:00:00
		Calendar todayStart = Calendar.getInstance();
		todayStart.set(Calendar.HOUR, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);

		return timeMillis < todayStart.getTime().getTime() ? true:false;
	}
	
	/**
	 * 获取与当前时间间隔的时间
	 * @param interval
	 * @return
	 */
	public static String getIntervalCurrentTime(int interval){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, interval); 
		return format.format(calendar.getTime());
	}
	

	public static Date parseDate(String dateStr) {
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return df1.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取当前日期的字符串格式
	 * @return
	 */
	public static String getCurrentDateString()
	{
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return df1.format(date);
	}
	
	/**
	 * 获取当前时间
	 */
	public static String getCurrentTimeString()
	{
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return df1.format(date);
	}
	
	/**
	 * 获取当前时间
	 */
	public static String getCurrentTimeFormatString()
	{
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		return df1.format(date);
	}
	
	/**
	 * 获取昨天日期
	 * @return
	 */
	public static String getYesterdayString()
	{
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis()-86400000);
		return df1.format(date);
	}
	
	/**
	 * 获取几天前的日期
	 * @param befort
	 * @return
	 */
	public static String getBefortDateString(int befort)
	{
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis()-(86400000*befort));
		return df1.format(date);
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		long usedtime = getCompareDate("2014-01-01", "2014-01-02");
//		System.out.println(usedtime);
//		System.out.println(usedtime/1000/60/60);
		
		String aa = "2014-02-16T18:33:09+08:00";
		
		Date d = new Date();
//		System.out.println(d.parse(aa));
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+08:00'");

		d = df2.parse(aa);
		System.out.println(df1.format(d));
//		System.out.println(df1.parse(aa));
//		System.out.println(conevertCommentDateStr("20分钟前", new Date()));
		
//		Calendar todayStart = Calendar.getInstance();
//		todayStart.set(Calendar.HOUR, 0);
//		todayStart.set(Calendar.MINUTE, 0);
//		todayStart.set(Calendar.SECOND, 0);
//		todayStart.set(Calendar.MILLISECOND, 0);
//
//		System.out.println(todayStart.getTime().getTime());
//		System.out.println(System.currentTimeMillis());
//		
//		SimpleDateFormat df = new SimpleDateFormat("HH");

//		System.out.println(todayStart.getTime().getTime());
//		System.out.println(System.currentTimeMillis());
//		System.out.println(DateFormat(1384932580401L,3));
//		SimpleDateFormat df = new SimpleDateFormat("HH");

//		System.out.println((int)(10*0.5));
//		System.out.println(dd);
//		System.out.println(DateUtil.getBefortDateString(30));
		//System.out.println(getIntervalCurrentTime(30));
//		dd2();
		
		
	}
	
	public static String dd() throws IOException
	{
		Runtime runtime = Runtime.getRuntime();
		String cmdString = "";
		runtime.exec(cmdString);
		return null;
	}
	/**
	 * 获取时间的数字
	 * @param timpstepp
	 * @return
	 */
	public static int getCurrentDateHour(long timpstepp)
	{
		SimpleDateFormat df = new SimpleDateFormat("HH");
		
		String str = df.format(new Date(timpstepp));
		if(str.startsWith("0"))
			str = str.substring(1, str.length());
		
		return Integer.parseInt(str);
	}
	
	/**
	 * 获取时间的数字
	 * @return
	 */
	public static int getCurrentHour()
	{
		SimpleDateFormat df = new SimpleDateFormat("HH");
		
		String str = df.format(new Date());
		if(str.startsWith("0"))
			str = str.substring(1, str.length());
		
		return Integer.parseInt(str);
	}
	
	/**
	 * 获取系统时间分钟的数字
	 * @param timpstepp
	 * @return
	 */
	public static int getCurrentMinute()
	{
		 Calendar c = Calendar.getInstance();
		 return c.get(Calendar.MINUTE);
	}
	
	public static void  dd2()
	{
		String dd = "d:/java/file/downLoad/taobaoTmp";
		System.out.println(dd.contains("taobao"));
	}
	
	/**
	 * 根据年龄，计算出生年份。比如：23岁，1990年出生
	 * guixin.chen 2014-6-25 
	 * @return
	 * @return String
	 * @throws
	 */
	public static String getBirthDay(String age){
		Calendar   mycalendar=Calendar.getInstance();//获取现在时间
		String year = String.valueOf(mycalendar.get(Calendar.YEAR));//获取年份
		int birth=Integer.parseInt(year)-Integer.parseInt(age);
		return String.valueOf(birth) ;
	}
	
	/**
	 * 转化新闻页面上的日期格式统一为：yyyy-MM-dd HH:mm:ss
	 * 包含：
	 * yyyy年MM月dd日 HH:mm:ss，yyyy年MM月dd日HH:mm:ss，yyyy年MM月dd日 HH:mm，yyyy年MM月dd日HH:mm，yyyy年MM月dd日，
	 * yyyy-MM-dd HH:mm，yyyy-MM-dd，MM-dd HH:mm:ss，MM-dd HH:mm，MM-dd，
	 * yyyy/MM/dd HH:mm:ss，yyyy/MM/dd HH:mm，yyyy/MM/dd，MM/dd HH:mm:ss，MM/dd HH:mm，MM/dd，
	 * MM月dd日 HH:mm:ss，MM月dd日HH:mm:ss，MM月dd日 HH:mm，MM月dd日HH:mm，MM月dd日，
	 * 几秒前，几分钟前，几小时前，几天前，今天12：12，昨天：12:12，前天12:12，
	 * 时间戳（1392510270000）
	 * @param date
	 * @return
	 */

	public static String changeDateStr(String date){
		SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sd2 = null;
		String s = "";
		try {
			if(date == null || date.isEmpty()){
				return "";
			}
			if(date.contains("年") && date.contains("月") && date.contains("日")){
				if(date.split(":").length==3){
					if(date.contains("\\s")){
						sd2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
						Date d = sd2.parse(date);
						s = sd1.format(d);
					}else if(date.contains(" ")){
						sd2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
						Date d = sd2.parse(date);
						s = sd1.format(d);
					}
					else{
						sd2 = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
						Date d = sd2.parse(date);
						s = sd1.format(d);
					}
				}
				else if(date.split(":").length==2){
					if(date.contains("\\s")){
						sd2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
						Date d = sd2.parse(date);
						s = sd1.format(d);
					}else if(date.contains(" ")){
						sd2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
						Date d = sd2.parse(date);
						s = sd1.format(d);
					}
					else{
						sd2 = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
						Date d = sd2.parse(date);
						s = sd1.format(d);
					}
				}
				else{
					sd2 = new SimpleDateFormat("yyyy年MM月dd日");
					Date d = sd2.parse(date);
					s = sd1.format(d);
				}
				
			}
			if(date.split("-").length == 3){
				if(date.split(":").length == 3){
					s = date;
				}else if(date.split(":").length == 2){
					sd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date d = sd2.parse(date);
					s = sd1.format(d);
				}else{
					sd2 = new SimpleDateFormat("yyyy-MM-dd");
					Date d = sd2.parse(date);
					s = sd1.format(d);
				}
				
			}
			if(date.split("-").length == 2){
				if(date.split(":").length == 3){
					sd2 = new SimpleDateFormat("MM-dd HH:mm:ss");
					Date d = sd2.parse(date);
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					s = sd1.format(ca.getTime());
				}else if(date.split(":").length == 2){
					sd2 = new SimpleDateFormat("MM-dd HH:mm");
					Date d = sd2.parse(date);
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					s = sd1.format(ca.getTime());
				}else{
					sd2 = new SimpleDateFormat("MM-dd");
					Date d = sd2.parse(date);
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					s = sd1.format(ca.getTime());
				}
				
			}
			if(date.split("/").length == 3){
				if(date.split(":").length == 3){
					sd2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date d = sd2.parse(date);
					s = sd1.format(d);
				}else if(date.split(":").length == 2){
					sd2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
					Date d = sd2.parse(date);
					s = sd1.format(d);
				}else{
					sd2 = new SimpleDateFormat("yyyy/MM/dd");
					Date d = sd2.parse(date);
					s = sd1.format(d);
				}
			}
			if(date.split("/").length == 2){
				if(date.split(":").length == 3){
					sd2 = new SimpleDateFormat("MM/dd HH:mm:ss");
					Date d = sd2.parse(date);
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					s = sd1.format(ca.getTime());
				}else if(date.split(":").length == 2){
					sd2 = new SimpleDateFormat("MM/dd HH:mm");
					Date d = sd2.parse(date);
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					s = sd1.format(ca.getTime());
				}else{
					sd2 = new SimpleDateFormat("MM/dd");
					Date d = sd2.parse(date);
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					s = sd1.format(ca.getTime());
				}
				
			}
			if(date.indexOf("年")<0 && date.contains("月") && date.contains("日")){
				if(date.split(":").length==3){
					if(date.contains("\\s")){
						sd2 = new SimpleDateFormat("MM月dd日 HH:mm:ss");
						Date d = sd2.parse(date);
						Calendar ca = Calendar.getInstance();
						int year = ca.get(Calendar.YEAR);
						ca.setTime(d);
						ca.set(Calendar.YEAR,year);
						s = sd1.format(ca.getTime());
					}else if(date.contains(" ")){
						sd2 = new SimpleDateFormat("MM月dd日 HH:mm:ss");
						Date d = sd2.parse(date);
						Calendar ca = Calendar.getInstance();
						int year = ca.get(Calendar.YEAR);
						ca.setTime(d);
						ca.set(Calendar.YEAR,year);
						s = sd1.format(ca.getTime());
					}
					else{
						sd2 = new SimpleDateFormat("MM月dd日HH:mm:ss");
						Date d = sd2.parse(date);
						Calendar ca = Calendar.getInstance();
						int year = ca.get(Calendar.YEAR);
						ca.setTime(d);
						ca.set(Calendar.YEAR,year);
						s = sd1.format(ca.getTime());
					}
				}
				else if(date.split(":").length==2){
					if(date.contains("\\s")){
						sd2 = new SimpleDateFormat("MM月dd日 HH:mm");
						Date d = sd2.parse(date);
						Calendar ca = Calendar.getInstance();
						int year = ca.get(Calendar.YEAR);
						ca.setTime(d);
						ca.set(Calendar.YEAR,year);
						s = sd1.format(ca.getTime());
					}else if(date.contains(" ")){
						sd2 = new SimpleDateFormat("MM月dd日 HH:mm");
						Date d = sd2.parse(date);
						Calendar ca = Calendar.getInstance();
						int year = ca.get(Calendar.YEAR);
						ca.setTime(d);
						ca.set(Calendar.YEAR,year);
						s = sd1.format(ca.getTime());
					}
					else{
						sd2 = new SimpleDateFormat("MM月dd日HH:mm");
						Date d = sd2.parse(date);
						Calendar ca = Calendar.getInstance();
						int year = ca.get(Calendar.YEAR);
						ca.setTime(d);
						ca.set(Calendar.YEAR,year);
						s = sd1.format(ca.getTime());
					}
				}
				else{
					sd2 = new SimpleDateFormat("MM月dd日");
					Date d = sd2.parse(date);
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					s = sd1.format(ca.getTime());
				}
				
			}
			
			
			if(date.indexOf("秒前") > -1){
				Calendar ca = Calendar.getInstance();
				int second = Integer.parseInt(date.substring(0, date.indexOf("秒前")));
				ca.add(Calendar.SECOND, -second);
				s = sd1.format(ca.getTime());
			}
			if(date.indexOf("分钟前") > -1){
				Calendar ca = Calendar.getInstance();
				int minute = Integer.parseInt(date.substring(0, date.indexOf("分钟前")));
				ca.add(Calendar.MINUTE, -minute);
				s = sd1.format(ca.getTime());
			}
			if(date.indexOf("小时前") > -1){
				Calendar ca = Calendar.getInstance();
				int hour = Integer.parseInt(date.substring(0, date.indexOf("小时前")));
				ca.add(Calendar.HOUR, -hour);
				s = sd1.format(ca.getTime());
			}
			if(date.indexOf("天前") > -1){
				Calendar ca = Calendar.getInstance();
				int day = Integer.parseInt(date.substring(0, date.indexOf("天前")));
				ca.add(Calendar.DAY_OF_MONTH, -day);
				s = sd1.format(ca.getTime());
			}
			if(date.indexOf("今天")>-1){
				Calendar ca = Calendar.getInstance();
				String time = date.substring(2, date.length());
				if(time.contains(":") && time.split(":").length ==3){
					sd2 = new SimpleDateFormat("HH:mm:ss");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day);
					s = sd1.format(ca.getTime());
				}else if(time.contains(":") && time.split(":").length ==2){
					sd2 = new SimpleDateFormat("HH:mm");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day);
					s = sd1.format(ca.getTime());
				}
				else if(time.contains("-") && time.split("-").length ==3){
					sd2 = new SimpleDateFormat("HH-mm-ss");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day);
					s = sd1.format(ca.getTime());
					
				}
				else if(time.contains("-") && time.split("-").length ==2){
					sd2 = new SimpleDateFormat("HH-mm");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day);
					s = sd1.format(ca.getTime());
				}
				
			}
			if(date.indexOf("昨天")>-1){
				Calendar ca = Calendar.getInstance();
				String time = date.substring(2, date.length());
				if(time.contains(":") && time.split(":").length ==3){
					sd2 = new SimpleDateFormat("HH:mm:ss");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-1);
					s = sd1.format(ca.getTime());
				}else if(time.contains(":") && time.split(":").length ==2){
					sd2 = new SimpleDateFormat("HH:mm");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-1);
					s = sd1.format(ca.getTime());
				}
				else if(time.contains("-") && time.split("-").length ==3){
					sd2 = new SimpleDateFormat("HH-mm-ss");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-1);
					s = sd1.format(ca.getTime());
					
				}
				else if(time.contains("-") && time.split("-").length ==2){
					sd2 = new SimpleDateFormat("HH-mm");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-1);
					s = sd1.format(ca.getTime());
				}
				
			}
			if(date.indexOf("前天")>-1){
				Calendar ca = Calendar.getInstance();
				String time = date.substring(2, date.length());
				if(time.contains(":") && time.split(":").length ==3){
					sd2 = new SimpleDateFormat("HH:mm:ss");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-2);
					s = sd1.format(ca.getTime());
				}else if(time.contains(":") && time.split(":").length ==2){
					sd2 = new SimpleDateFormat("HH:mm");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-2);
					s = sd1.format(ca.getTime());
				}
				else if(time.contains("-") && time.split("-").length ==3){
					sd2 = new SimpleDateFormat("HH-mm-ss");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-2);
					s = sd1.format(ca.getTime());
					
				}
				else if(time.contains("-") && time.split("-").length ==2){
					sd2 = new SimpleDateFormat("HH-mm");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-2);
					s = sd1.format(ca.getTime());
				}
				
			}
			if(date.matches("\\d{13}")){
				s = sd1.format(new Date(Long.parseLong(date)));
			}
			
		} catch (Exception e) {
			System.out.println("处理时间格式异常,时间为："+date);
			e.printStackTrace();
		}
		return s;
	}
	
	/**
     * 替换字符串对象中的空格&nbsp，回车\n br,tab键
     * @param content
     * @return
     */
    public static String replaceKeyTab(String content)
    {
    	content = content.replaceAll("<br/>", "");
    	content = content.replaceAll("<br>", "");
    	content = content.replaceAll("\\n", "");
    	content = content.replaceAll("\n", "");
    	content = content.replaceAll("\\r", "");
    	content = content.replaceAll("\r", "");
    	content = content.replaceAll("\\t", "");
    	content = content.replaceAll("\\u000a", "");
    	content = content.replaceAll("&nbsp;", "");
    	content = content.replaceAll("&nbsp", "");
    	content = content.replaceAll(" ","");       //去掉&nbsp引起的空格
    	return content;
    }
    
    /**
	 * 移动成功导入数据库的文件到历史目录
	 * @param filePath
	 * @param historyPath
	 */
	public static void moveFileToHistory(String filePath,String historyPath)
	{
		// 以当天日期创建文件夹
		String historyFileFolder = historyPath+File.separator+DateUtil.getCurrentDateString();
		FileUtil.newFolder(historyFileFolder);
		
		String fileName = filePath.substring(filePath.lastIndexOf(File.separator)+1, filePath.length());
		FileUtil.moveFile(filePath, historyFileFolder+File.separator+fileName,null, true);
	}
	
	/**
	 * 转化新闻页面上的日期格式统一为：yyyy-MM-dd HH:mm:ss
	 * 包含：
	 * yyyy年MM月dd日 HH:mm:ss，yyyy年MM月dd日HH:mm:ss，yyyy年MM月dd日 HH:mm，yyyy年MM月dd日HH:mm，yyyy年MM月dd日，
	 * yyyy-MM-dd HH:mm，yyyy-MM-dd，MM-dd HH:mm:ss，MM-dd HH:mm，MM-dd，
	 * yyyy/MM/dd HH:mm:ss，yyyy/MM/dd HH:mm，yyyy/MM/dd，MM/dd HH:mm:ss，MM/dd HH:mm，MM/dd，
	 * MM月dd日 HH:mm:ss，MM月dd日HH:mm:ss，MM月dd日 HH:mm，MM月dd日HH:mm，MM月dd日，
	 * 几秒前，几分钟前，几小时前，几天前，今天12：12，昨天：12:12，前天12:12，
	 * 时间戳（1392510270000）
	 * @param date
	 * @return
	 */

	public static String changeDateStr1(String date){
		SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sd2 = null;
		String s = "";
		try {
			if(date == null || date.isEmpty()){
				return "";
			}
			if(date.contains("年") && date.contains("月") && date.contains("日")){
				if(date.split(":").length==3){
					if(date.contains("\\s")){
						sd2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
						Date d = sd2.parse(date);
						s = sd1.format(d);
					}else if(date.contains(" ")){
						sd2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
						Date d = sd2.parse(date);
						s = sd1.format(d);
					}
					else{
						sd2 = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
						Date d = sd2.parse(date);
						s = sd1.format(d);
					}
				}
				else if(date.split(":").length==2){
					if(date.contains("\\s")){
						sd2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
						Date d = sd2.parse(date);
						s = sd1.format(d);
					}else if(date.contains(" ")){
						sd2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
						Date d = sd2.parse(date);
						s = sd1.format(d);
					}
					else{
						sd2 = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
						Date d = sd2.parse(date);
						s = sd1.format(d);
					}
				}
				else{
					sd2 = new SimpleDateFormat("yyyy年MM月dd日");
					Date d = sd2.parse(date);
					s = sd1.format(d);
				}
				
			}
			if(date.split("-").length == 3){
				if(date.split(":").length == 3){
					s = date;
				}else if(date.split(":").length == 2){
					sd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date d = sd2.parse(date);
					s = sd1.format(d);
				}else{
					sd2 = new SimpleDateFormat("yyyy-MM-dd");
					Date d = sd2.parse(date);
					s = sd1.format(d);
				}
				
			}
			if(date.split("-").length == 2){
				if(date.split(":").length == 3){
					sd2 = new SimpleDateFormat("MM-dd HH:mm:ss");
					Date d = sd2.parse(date);
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					s = sd1.format(ca.getTime());
				}else if(date.split(":").length == 2){
					sd2 = new SimpleDateFormat("MM-dd HH:mm");
					Date d = sd2.parse(date);
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					s = sd1.format(ca.getTime());
				}else{
					sd2 = new SimpleDateFormat("MM-dd");
					Date d = sd2.parse(date);
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					s = sd1.format(ca.getTime());
				}
				
			}
			if(date.split("/").length == 3){
				if(date.split(":").length == 3){
					sd2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date d = sd2.parse(date);
					s = sd1.format(d);
				}else if(date.split(":").length == 2){
					sd2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
					Date d = sd2.parse(date);
					s = sd1.format(d);
				}else{
					sd2 = new SimpleDateFormat("yyyy/MM/dd");
					Date d = sd2.parse(date);
					s = sd1.format(d);
				}
			}
			if(date.split("/").length == 2){
				if(date.split(":").length == 3){
					sd2 = new SimpleDateFormat("MM/dd HH:mm:ss");
					Date d = sd2.parse(date);
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					s = sd1.format(ca.getTime());
				}else if(date.split(":").length == 2){
					sd2 = new SimpleDateFormat("MM/dd HH:mm");
					Date d = sd2.parse(date);
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					s = sd1.format(ca.getTime());
				}else{
					sd2 = new SimpleDateFormat("MM/dd");
					Date d = sd2.parse(date);
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					s = sd1.format(ca.getTime());
				}
				
			}
			if(date.indexOf("年")<0 && date.contains("月") && date.contains("日")){
				if(date.split(":").length==3){
					if(date.contains("\\s")){
						sd2 = new SimpleDateFormat("MM月dd日 HH:mm:ss");
						Date d = sd2.parse(date);
						Calendar ca = Calendar.getInstance();
						int year = ca.get(Calendar.YEAR);
						ca.setTime(d);
						ca.set(Calendar.YEAR,year);
						s = sd1.format(ca.getTime());
					}else if(date.contains(" ")){
						sd2 = new SimpleDateFormat("MM月dd日 HH:mm:ss");
						Date d = sd2.parse(date);
						Calendar ca = Calendar.getInstance();
						int year = ca.get(Calendar.YEAR);
						ca.setTime(d);
						ca.set(Calendar.YEAR,year);
						s = sd1.format(ca.getTime());
					}
					else{
						sd2 = new SimpleDateFormat("MM月dd日HH:mm:ss");
						Date d = sd2.parse(date);
						Calendar ca = Calendar.getInstance();
						int year = ca.get(Calendar.YEAR);
						ca.setTime(d);
						ca.set(Calendar.YEAR,year);
						s = sd1.format(ca.getTime());
					}
				}
				else if(date.split(":").length==2){
					if(date.contains("\\s")){
						sd2 = new SimpleDateFormat("MM月dd日 HH:mm");
						Date d = sd2.parse(date);
						Calendar ca = Calendar.getInstance();
						int year = ca.get(Calendar.YEAR);
						ca.setTime(d);
						ca.set(Calendar.YEAR,year);
						s = sd1.format(ca.getTime());
					}else if(date.contains(" ")){
						sd2 = new SimpleDateFormat("MM月dd日 HH:mm");
						Date d = sd2.parse(date);
						Calendar ca = Calendar.getInstance();
						int year = ca.get(Calendar.YEAR);
						ca.setTime(d);
						ca.set(Calendar.YEAR,year);
						s = sd1.format(ca.getTime());
					}
					else{
						sd2 = new SimpleDateFormat("MM月dd日HH:mm");
						Date d = sd2.parse(date);
						Calendar ca = Calendar.getInstance();
						int year = ca.get(Calendar.YEAR);
						ca.setTime(d);
						ca.set(Calendar.YEAR,year);
						s = sd1.format(ca.getTime());
					}
				}
				else{
					sd2 = new SimpleDateFormat("MM月dd日");
					Date d = sd2.parse(date);
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					s = sd1.format(ca.getTime());
				}
				
			}
			
			
			if(date.indexOf("秒前") > -1){
				Calendar ca = Calendar.getInstance();
				int second = Integer.parseInt(date.substring(0, date.indexOf("秒前")));
				ca.add(Calendar.SECOND, -second);
				s = sd1.format(ca.getTime());
			}
			if(date.indexOf("分钟前") > -1){
				Calendar ca = Calendar.getInstance();
				int minute = Integer.parseInt(date.substring(0, date.indexOf("分钟前")));
				ca.add(Calendar.MINUTE, -minute);
				s = sd1.format(ca.getTime());
			}
			if(date.indexOf("小时前") > -1){
				Calendar ca = Calendar.getInstance();
				int hour = Integer.parseInt(date.substring(0, date.indexOf("小时前")));
				ca.add(Calendar.HOUR, -hour);
				s = sd1.format(ca.getTime());
			}
			if(date.indexOf("天前") > -1){
				Calendar ca = Calendar.getInstance();
				int day = Integer.parseInt(date.substring(0, date.indexOf("天前")));
				ca.add(Calendar.DAY_OF_MONTH, -day);
				s = sd1.format(ca.getTime());
			}
			if(date.indexOf("今天")>-1){
				Calendar ca = Calendar.getInstance();
				String time = date.substring(2, date.length());
				if(time.contains(":") && time.split(":").length ==3){
					sd2 = new SimpleDateFormat("HH:mm:ss");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day);
					s = sd1.format(ca.getTime());
				}else if(time.contains(":") && time.split(":").length ==2){
					sd2 = new SimpleDateFormat("HH:mm");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day);
					s = sd1.format(ca.getTime());
				}
				else if(time.contains("-") && time.split("-").length ==3){
					sd2 = new SimpleDateFormat("HH-mm-ss");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day);
					s = sd1.format(ca.getTime());
					
				}
				else if(time.contains("-") && time.split("-").length ==2){
					sd2 = new SimpleDateFormat("HH-mm");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day);
					s = sd1.format(ca.getTime());
				}
				
			}
			if(date.indexOf("昨天")>-1){
				Calendar ca = Calendar.getInstance();
				String time = date.substring(2, date.length());
				if(time.contains(":") && time.split(":").length ==3){
					sd2 = new SimpleDateFormat("HH:mm:ss");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-1);
					s = sd1.format(ca.getTime());
				}else if(time.contains(":") && time.split(":").length ==2){
					sd2 = new SimpleDateFormat("HH:mm");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-1);
					s = sd1.format(ca.getTime());
				}
				else if(time.contains("-") && time.split("-").length ==3){
					sd2 = new SimpleDateFormat("HH-mm-ss");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-1);
					s = sd1.format(ca.getTime());
					
				}
				else if(time.contains("-") && time.split("-").length ==2){
					sd2 = new SimpleDateFormat("HH-mm");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-1);
					s = sd1.format(ca.getTime());
				}
				
			}
			if(date.indexOf("前天")>-1){
				Calendar ca = Calendar.getInstance();
				String time = date.substring(2, date.length());
				if(time.contains(":") && time.split(":").length ==3){
					sd2 = new SimpleDateFormat("HH:mm:ss");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-2);
					s = sd1.format(ca.getTime());
				}else if(time.contains(":") && time.split(":").length ==2){
					sd2 = new SimpleDateFormat("HH:mm");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-2);
					s = sd1.format(ca.getTime());
				}
				else if(time.contains("-") && time.split("-").length ==3){
					sd2 = new SimpleDateFormat("HH-mm-ss");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-2);
					s = sd1.format(ca.getTime());
					
				}
				else if(time.contains("-") && time.split("-").length ==2){
					sd2 = new SimpleDateFormat("HH-mm");
					Date d = sd2.parse(time);
					int year = ca.get(Calendar.YEAR);
					int month = ca.get(Calendar.MONTH)+1;
					int day = ca.get(Calendar.DAY_OF_MONTH);
					ca.setTime(d);
					ca.set(Calendar.YEAR,year);
					ca.set(Calendar.MONTH,month);
					ca.set(Calendar.DAY_OF_MONTH,day-2);
					s = sd1.format(ca.getTime());
				}
				
			}
			if(date.matches("\\d{13}")){
				s = sd1.format(new Date(Long.parseLong(date)));
			}
			
		} catch (Exception e) {
			System.out.println("处理时间格式异常,时间为："+date);
			e.printStackTrace();
		}
		return s;
	}
	
	public static void downloadImageToLocal(ImageReader imageReader,
			String fileName) throws Exception {
		ImageOutputStream iis = null ;
		try {
			BufferedImage bufferedImage = imageReader.read(0);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();    
	        ImageIO.write(bufferedImage, "jpg", baos); 
	        byte[] bytes = baos.toByteArray();   
	        iis = new  FileImageOutputStream(new File(fileName)) ;
	        iis.write(bytes) ;
		} catch (IOException ex) {
			ex.printStackTrace();
		}finally{
			
		}
	}
	
	/**
	 * 读取url中数据，并以字节的形式返回
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inputStream) throws Exception{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while((len = inputStream.read(buffer)) !=-1){
			outputStream.write(buffer, 0, len);
		}
		outputStream.close();
		inputStream.close();
		return outputStream.toByteArray();
	}

}
