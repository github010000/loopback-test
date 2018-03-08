package com.skb.xpg.nxpg.svc.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 날짜를 쉽게 출력하기 위한 utility 클래스
 *
 * @author  지승렬
 */
public class DateUtil {
//	public static String eventStartDate="200410202255"; // 원하는 달에서 1을 빼주는 것에 주의 !!
//	public static String eventEndDate="200410210705"; // 원하는 달에서 1을 빼주는 것에 주의 !!

	// 현재날짜 (format : YYYYMMDDhhmmss)
//	private static String getYYYYMMDDhhmmss(){
//		int iYear, iMonth, iDay, iHour, iMinute, iSecond;
//		String strYear, strMonth, strDay, strHour, strMinute, strSecond;
//		Calendar cal = Calendar.getInstance();
//		
//		iYear 	= cal.get(Calendar.YEAR);
//		iMonth	= cal.get(Calendar.MONTH) + 1;
//		iDay	= cal.get(Calendar.DATE);
//		
//		if (cal.get(Calendar.AM_PM) == 0) // 오전
//		{
//			iHour	= cal.get(Calendar.HOUR);
//		}
//		else
//		{
//			iHour	= 12 + cal.get(Calendar.HOUR);
//		}
//		iMinute	= cal.get(Calendar.MINUTE);
//		iSecond	= cal.get(Calendar.SECOND);
//		
//		strYear 	= Integer.toString(iYear);
//		strMonth 	= Integer.toString(iMonth + 100).substring(1, 3);
//		strDay		= Integer.toString(iDay + 100).substring(1, 3);
//		strHour		= Integer.toString(iHour + 100).substring(1, 3);
//		strMinute	= Integer.toString(iMinute + 100).substring(1, 3);
//		strSecond	= Integer.toString(iSecond + 100).substring(1, 3);
//		
//		return (strYear + strMonth + strDay + strHour + strMinute + strSecond);
//	}
	
//	// 지정된 날짜로부터 +, - 날짜 (format : YYYYMMDD)
//	public static String getYYYYMMDD(String strYYYYMMDD, String strType, int iAdded)
//	{
//		Integer intYear, intMonth, intDay;
//		int iYear, iMonth, iDay;
//		String strYear, strMonth, strDay;
//		
//		
//		intYear 	= Integer.valueOf(strYYYYMMDD.substring(0, 4));
//		intMonth	= Integer.valueOf(strYYYYMMDD.substring(4, 6));
//		intDay		= Integer.valueOf(strYYYYMMDD.substring(6, 8));
//		
//		Calendar cal = Calendar.getInstance();
//		cal.set(intYear, intMonth - 1, intDay);
//		
//		if (strType.compareTo("DAY") == 0)
//		{
//			cal.add(Calendar.DATE, iAdded);
//		}
//		else if (strType.compareTo("YEAR") == 0)
//		{
//			cal.add(Calendar.YEAR, iAdded);
//		}
//		else if(strType.compareTo("MONTH") ==0)
//		{
//			cal.add(Calendar.MONTH,iAdded);
//		}
//		else
//		{
//			return strYYYYMMDD;
//		}
//		
//		iYear 	= cal.get(Calendar.YEAR);
//		iMonth	= cal.get(Calendar.MONTH) + 1;
//		iDay	= cal.get(Calendar.DATE);
//		
//		
//		strYear 	= Integer.toString(iYear);
//		strMonth 	= Integer.toString(iMonth + 100).substring(1, 3);
//		strDay		= Integer.toString(iDay + 100).substring(1, 3);
//		
//		return (strYear + strMonth + strDay);
//	}
//	
//	public static String getYYMMDDhhmmss() {
//		Locale currLocale = new Locale("KOREAN","KOREA");  
//		String pattern = "yyMMddHHmmss";
//		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currLocale);
//
//		return formatter.format(new Date());
//	}
	
	public static String getYYYYMMDDhhmmss() {
		Locale currLocale = new Locale("KOREAN","KOREA");  
		String pattern = "yyyyMMddHHmmss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currLocale);

		return formatter.format(new Date());
	}
}

