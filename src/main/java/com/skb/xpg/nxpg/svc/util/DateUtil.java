package com.skb.xpg.nxpg.svc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		String pattern = "yyyyMMddHHmmss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currLocale);

		return formatter.format(new Date());
	}
	
	public static String getYYYYMMDDhhmmssms() {
		Locale currLocale = new Locale("KOREAN","KOREA");  
		String pattern = "yyyyMMddHHmmssms";
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
		
		if (isMenu && object.containsKey("menu_cd")) {
			object.remove("menu_cd");
		}

		try {
			if (fromDt != null && fromDt.matches("[0-9]{14}")) {
				strTime = dateFormat.parse(String.valueOf(object.get(fromDt)));
			}
			if (toDt != null && toDt.matches("[0-9]{14}")) {
				endTime = dateFormat.parse(String.valueOf(object.get(toDt)));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

