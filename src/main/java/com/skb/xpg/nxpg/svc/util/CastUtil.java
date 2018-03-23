package com.skb.xpg.nxpg.svc.util;

import java.util.List;
import java.util.Map;

import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Component;

@Component
public class CastUtil {

	public static String getObjectToString(Object obj) {
		if (obj != null && obj instanceof String) {
			return (String) obj;
		} else return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getObjectToMap(Object obj) {
		if (obj != null && obj instanceof Map<?, ?>) {
			return (Map<String, Object>) obj;
		} else return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getObjectToMapStringString(Object obj) {
		if (obj != null && obj instanceof Map<?, ?>) {
			return (Map<String, String>) obj;
		} else return null;
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getObjectToMapList(Object obj) {
		if (obj != null && obj instanceof List<?>) {
			return (List<Map<String, Object>>) obj;
		} else return null;
	}

	public static int getStringToInteger(String str) {
		int rtn = 0;
		
		if (str != null & str.matches("[0-9]+")) {
			try {
				rtn = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				rtn = 0;
			}
		}
		return rtn;
	}

	public static Long getStringToLong(String str) {
		long rtn = 0;
		if (str != null & str.matches("[0-9]+")) {
			try {
				rtn = Long.parseLong(str);
			} catch (NumberFormatException e) {
				rtn = 0;
			}
		}
		return rtn;
	}
	
	public static List<Object> StringToJsonList(String json) {
		JacksonJsonParser parser = new JacksonJsonParser();
		List<Object> list = parser.parseList(json);
		return list;

	}
	
	public static Map<String, Object> StringToJsonMap(String json) {
		if (json != null) {

			GsonJsonParser parser = new GsonJsonParser();
			Map<String, Object> map = parser.parseMap(json);
			return map;
		}
		return null;
	}
}
