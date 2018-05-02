package com.skb.xpg.nxpg.svc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 날짜를 쉽게 출력하기 위한 utility 클래스
 *
 * @author  지승렬
 */
public class DateUtil {
	
	public static String getYYYYMMDDhhmmss() {
		Locale currLocale = new Locale("KOREAN","KOREA");  
		String pattern = "yyyyMMddHHmmssms";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currLocale);

		return formatter.format(new Date());
	}
	
	public static String getYYYYMMDDhhmmss2() {
		Locale currLocale = new Locale("KOREAN","KOREA");  
		String pattern = "yyyyMMddHHmmss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currLocale);

		return formatter.format(new Date());
	}
	
	public static String getAddDate(String date, int day) {
		Locale currLocale = new Locale("KOREAN","KOREA");  
		String pattern = "yyyyMMddHHmmss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currLocale);
		try {
			Date time = formatter.parse(String.valueOf(date));
			
			Calendar c = Calendar.getInstance();
	        c.setTime(time);
	        c.add(Calendar.DATE, day);
			
	        Date dd = c.getTime();

			return formatter.format(dd);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LogUtil.error("", "", "", "", "", e.toString());
		}
		return "";
	}
	
	public static boolean doCompareSingle(String start, String end, String pattern) {
		Locale currLocale = new Locale("KOREAN","KOREA");  
//		String pattern = "yyyyMMdd";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currLocale);
		
		String today = formatter.format(new Date());
		
		if (start.matches("[0-9]+") && end.matches("[0-9]+")) {
			
			if (today.compareTo(start) >= 0 && today.compareTo(end) <= 0) {
				return true;
			}
		}
		return false;
	}
	
	
	public static String getYYYYMMDDhhmmssms() {
		Locale currLocale = new Locale("KOREAN","KOREA");  
		String pattern = "yyyy-MM-dd HH:mm:ss.ms";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currLocale);

		return formatter.format(new Date());
	}
	
	public static void getCompareObject(List<Object> list, String fromDt, String toDt, boolean isMenu) {

		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		for (Object obj : list) {
			Map<String, Object> object = CastUtil.getObjectToMap(obj);
			// menu_cd 제거
			doCompare(newList, object, fromDt, toDt, isMenu);
		}
		list = new ArrayList<Object>();
		list.addAll(newList);
		
	}
	
	public static void getCompare(List<Map<String, Object>> list, String fromDt, String toDt, boolean isMenu) {

		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> object : list) {
			doCompare(newList, object, fromDt, toDt, isMenu);
		}
		list = new ArrayList<Map<String, Object>>();
		list.addAll(newList);
		
	}

	public static void doCompare(List<Map<String, Object>> newList, Map<String, Object> object, String fromDt, String toDt, boolean isMenu) {
		// menu_cd 제거
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		Date strTime = null;
		Date endTime = null;
		
//		if (isMenu && object.containsKey("menu_cd")) {
//			object.remove("menu_cd");
//		}

		try {
			if (fromDt != null && fromDt.matches("[0-9]{14}")) {
				strTime = dateFormat.parse(String.valueOf(object.get(fromDt)));
			}
			if (toDt != null && toDt.matches("[0-9]{14}")) {
				endTime = dateFormat.parse(String.valueOf(object.get(toDt)));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LogUtil.error("", "", "", "", "", e.toString());
		}

		if (strTime == null || date.before(strTime)) {
			if (endTime == null || date.after(endTime)) {
				newList.add(object);
			}
		}

		if (object.containsKey("menus")) {
			List<Map<String, Object>> deleteListInner = new ArrayList<Map<String, Object>>();
			if (deleteListInner != null && deleteListInner.size() > 0) {
				getCompare(CastUtil.getObjectToMapList(object.get("menus")), fromDt, toDt, isMenu);
			}
		}
	}

}

