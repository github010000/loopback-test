package com.jung.sys.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 날짜를 쉽게 출력하기 위한 utility 클래스
 *
 * @author  지승렬
 */

@Component
@RefreshScope
public class DateUtil {

	public static boolean expiryDate;
	
	@Value("${user.expiryDate}")
    private void setCimodeCheck(boolean expiryDate){
		DateUtil.expiryDate = expiryDate;
    }

	public static String getYYYYMMDD() {
		Locale currLocale = new Locale("KOREAN","KOREA");  
		String pattern = "yyyyMMdd";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currLocale);

		return formatter.format(new Date());
	}
	
	public static Date getStringToDate(String value) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			return formatter.parse(value);
		} catch (Exception e) {
			// LogUtil.error("", "", "", "", "", e.toString());
			return null;
		}
	}
	
	// 입력 일자 기준으로 며칠 이내인지 확인
	public static boolean checkDate(String strDate, int value) {
		if (strDate == null) return false;
		if (value == 0) return false;
		
		try {
			// 현재시간 년월일만 가지고 오기
			Date now = getStringToDate(getYYYYMMDD());
			
			// 입력받은값 년월일만 가지고 오기
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			Date chkDate = formatter.parse(strDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(chkDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			chkDate = cal.getTime();
			//////////////////////////
			
			int compare = now.compareTo(chkDate);
			// 지금시간이랑 같으면 
			if (compare == 0)
				return true;
			
			// 이전일 확인  (prd_prc_to_dt)
			if (value < 0) {
				// 지금 시간보다 더 작으면
				if (compare > 0) return true;
			}
			// 며칠 이내 확인 
			else {
				// 지금 시간보다 더 작으면 
				if (compare > 0) return false;
			}
			
			long diffDay = (chkDate.getTime() - now.getTime()) / (24*60*60*1000);
			return Math.abs(diffDay) <= Math.abs(value);
		} catch (Exception e) {
			// LogUtil.error("", "", "", "", "", e.toString());
			return false;
		}
	}
	
	public static String getYYYYMMDDhhmmss() {
		Locale currLocale = new Locale("KOREAN","KOREA");  
		String pattern = "yyyyMMddHHmmssSSS";
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
			LogUtil.error("", "", "", "", "", e.getStackTrace()[0].toString());
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
	
	public static void getCompareObject(List<Object> list, String fromDt, String toDt, boolean isMenuAndGrid) {

		//if (isMenuAndGrid && !expiryDate) {
			//return;
		//}

		if (list != null) {
			List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
			for (Object obj : list) {
				Map<String, Object> object = CastUtil.getObjectToMap(obj);
				// menu_cd 제거
				doCompare(newList, object, fromDt, toDt, isMenuAndGrid);
			}
			for (Map<String, Object> del : newList) {
				list.remove(del);
			}
		}
	}
	
	public static void getCompare(List<Map<String, Object>> list, String fromDt, String toDt, boolean isMenuAndGrid) {
		
		//if (isMenuAndGrid && !expiryDate) {
			//return;
		//}
		if (list != null) {
			List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> object : list) {
				doCompare(newList, object, fromDt, toDt, isMenuAndGrid);
			}
			//list = new ArrayList<Map<String, Object>>();
			//list.addAll(newList);
			for (Map<String, Object> del : newList) {
				list.remove(del);
			}
		}
	}

	public static void doCompare(List<Map<String, Object>> newList, Map<String, Object> object, String fromDt, String toDt, boolean isMenuAndGrid) {
		// menu_cd 제거
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		Date strTime = null;
		Date endTime = null;
		
		String tempFromDt = String.valueOf(object.get(fromDt));
		String temptoDt = String.valueOf(object.get(toDt));
		
		try {
			if (tempFromDt != null && tempFromDt.matches("[0-9]{14}")) {
				strTime = dateFormat.parse(tempFromDt);
			}
			if (temptoDt != null && temptoDt.matches("[0-9]{14}")) {
				endTime = dateFormat.parse(temptoDt);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LogUtil.error("", "", "", "", "", e.getStackTrace()[0].toString());
		}

		if ((strTime == null || strTime.before(date)) && (endTime == null || endTime.after(date))) {
			
		} else {
        	newList.add(object);
        }

		if (object.containsKey("menus")) {
			getCompare(CastUtil.getObjectToMapList(object.get("menus")), fromDt, toDt, isMenuAndGrid);
		}
	}

}