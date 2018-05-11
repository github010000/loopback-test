package com.skb.xpg.nxpg.svc.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Component;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

@Component
public class CastUtil {

	public static String getObjectToString(Object obj) {
		if (obj != null && obj instanceof String) {
			String str = (String) obj;
			return str;
		} else return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getObjectToMap(Object obj) {
		if (obj != null && obj instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) obj;
			return map;
		} else return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getObjectToMapStringString(Object obj) {
		if (obj != null && obj instanceof Map) {
			Map<String, String> mapStringString = (Map<String, String>) obj;
			return mapStringString;
		} else return null;
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getObjectToMapList(Object obj) {
		if (obj != null && obj instanceof List) {
			List<Map<String, Object>> list = (List<Map<String, Object>>) obj;
			return list;
		} else return null;
	}
	
	public static int getStringToInteger(String str) {
		int rtn = 0;
		
		if (str != null & str.matches("[0-9]+")) {
			try {
				rtn = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				rtn = 0;
				LogUtil.error("", "", "", "", "", e.toString());
			}
		}
		return rtn;
	}

	public static int getObjectToInteger(Object obj) {
		int rtn = 0;
		
		if (obj != null && obj instanceof Integer) {
			try {
				rtn = Integer.parseInt(obj.toString());
			} catch (NumberFormatException e) {
				rtn = 0;
				LogUtil.error("", "", "", "", "", e.toString());
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
				LogUtil.error("", "", "", "", "", e.toString());
			}
		}
		return rtn;
	}
	
	public static List<Object> StringToJsonList(String json) {
		if (json != null) {	
			try {
				JacksonJsonParser parser = new JacksonJsonParser();
				List<Object> list = parser.parseList(json);
				return list;	
			} catch (Exception e) {
				LogUtil.error("", "", "", "", "", e.toString());
				return null;
			}
		} else return null;
	}

	public static List<Map<String, Object>> StringToJsonListMap(String json) {
		if (json != null) {	
			Gson gson = new Gson();
			Type type = new TypeToken<List<Map<String, Object>>>() {}.getType();
			List<Map<String, Object>> data = gson.fromJson(json, type);
			return data;
		} else return null;
	}
	
	public static Map<String, Object> StringToJsonMap(String json) {
		if (json != null) {	
			GsonJsonParser parser = new GsonJsonParser();
			Map<String, Object> map = parser.parseMap(json);
			return map;
		} else return null;
	}
	
	public static String getString(String value) {
		String result = "";
		
		if (value instanceof String) {
			result = (String) value;
		}
		
		return result;
		
	}	
	
	@SuppressWarnings("unchecked")
	public static List<String> getObjectToListString(Object obj) {
		if (obj != null && obj instanceof List) {
			List<String> map = (List<String>) obj;
			return map;
		} else return null;
	}
}
