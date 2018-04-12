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
	
	public static void getCompare(List<Map<String, Object>> map, String fromDt, String toDt, boolean isMenu) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

		Date date = new Date();
		Date strTime = null;
		Date endTime = null;

		List<Map<String, Object>> deleteList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> object : map) {
			// menu_cd 제거
			if (isMenu && object.containsKey("menu_cd")) {
				object.remove("menu_cd");
			}

			try {
				strTime = dateFormat.parse(String.valueOf(object.get(fromDt)));
				endTime = dateFormat.parse(String.valueOf(object.get(toDt)));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (date.before(strTime) || date.after(endTime)) {
				deleteList.add(object);
			}
		}
		
		for (Map<String, Object> del : deleteList) {
			map.remove(del);
		}

	}
	
}

