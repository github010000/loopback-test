package com.skb.xpg.nxpg.svc.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
	
	public static int getStrToInt(String str) {
		if (str == null) return 0;
		if (str.equals("")) return 0;
		
		int rtn = 0;
		String temp = "";
		try {
			for(int i = 0; i < str.length(); i++) {
				if (str.charAt(i) == 46) break;
				if ((str.charAt(i) >= 48) && (str.charAt(i) <= 57)) {
					temp += str.charAt(i);
				}
			}
			rtn = Integer.parseInt(temp);
		} catch (Exception e) {
			LogUtil.error("", "", "", "", "", e.getStackTrace()[0].toString());
			return 0;
		}
		return rtn;
	}
	
	public static int getStringToInteger(String str) {
		int rtn = 0;
		
		if (str != null & str.matches("[0-9]+")) {
			try {
				rtn = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				rtn = 0;
				LogUtil.error("", "", "", "", "", e.getStackTrace()[0].toString());
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
				LogUtil.error("", "", "", "", "", e.getStackTrace()[0].toString());
			}
		}
		return rtn;
	}

	public static long getObjectToLong(Object obj) {
		long rtn = 0;
		
		if (obj != null && obj instanceof Long) {
			try {
				rtn = (long) obj;
			} catch (NumberFormatException e) {
				rtn = 0;
				LogUtil.error("", "", "", "", "", e.getStackTrace()[0].toString());
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
				LogUtil.error("", "", "", "", "", e.getStackTrace()[0].toString());
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
				LogUtil.error("", "", "", "", "", e.getStackTrace()[0].toString());
				return null;
			}
		} else return null;
	}

	public static List<Map<String, Object>> StringToJsonListMap(String json) {
		if (json != null) {	
			try {
				Gson gson = new Gson();
				Type type = new TypeToken<List<Map<String, Object>>>() {}.getType();
				List<Map<String, Object>> data = gson.fromJson(json, type);
				return data;
			} catch (Exception e) {
				LogUtil.error("", "", "", "", "", e.getStackTrace()[0].toString());
				return null;
			}
		} else return null;
	}
	
	public static Map<String, Object> StringToJsonMap(String json) {
		if (json != null) {	
			try {
				GsonJsonParser parser = new GsonJsonParser();
				Map<String, Object> map = parser.parseMap(json);
				return map;
			} catch (Exception e) {
				LogUtil.error("", "", "", "", "", e.getStackTrace()[0].toString());
				return null;
			}
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
	public static String getMapToString(Map<String, Object> obj) {
		if (obj != null && obj instanceof Map) {
			return new JSONObject(obj).toString();
		} else return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getObjectToListString(Object obj) {
		if (obj != null && obj instanceof List) {
			List<String> map = (List<String>) obj;
			return map;
		} else return null;
	}
	
	public static String getListToJsonArrayString(Object obj) {
		String rtn = "";
		if (obj != null && obj instanceof List) {
			try {
				Gson gson = new Gson();
				rtn = gson.toJson(obj);
				
			} catch (Exception e) {
				LogUtil.error("", "", "", "", "", e.getStackTrace()[0].toString());
			}
		}
		return rtn;
	}
	
	public static String getObjectToJsonArrayString(Object obj) {
		String rtn = "";
		if (obj != null && obj instanceof List) {
			try {
				rtn = new JSONArray(obj).toString();
			} catch (JSONException e) {
				LogUtil.error("", "", "", "", "", e.getStackTrace()[0].toString());
			}
		}
		return rtn;
	}
	
	// value2에 값을 value1로 넣어준다.
	public static void copyMapData(Map<String, Object> value1, Map<String, Object> value2) {
		if (value1 == null) return;
		if (value2 == null) return;
		
		try {
			for (Map.Entry<String, Object> entry : value2.entrySet()) {
				if (!"id_package".equals(entry.getKey())) {
					value1.put(entry.getKey(), entry.getValue());
				}
			}
		} catch (Exception e) {
			LogUtil.error("", "", "", "", "", e.getStackTrace()[0].toString());
		}
	}
	
	// id_package 값을 확인하여서 price arr를 obj로 변환시켜준다.
	public static void checkPackAgeList(Object list, Map<String, String> param) {
		String idPackAge = "15";
		if (param.containsKey("id_package")) {
			idPackAge = param.get("id_package");
		}
		int intPackAge = getStrToInt(idPackAge);
		// 초기값 설정
		if (intPackAge == 0) intPackAge = 15;
		
		List<Map<String, Object>> mapList = getObjectToMapList(list);
		if (mapList == null) return;
		for(int i = 0; i < mapList.size(); i++) {
			try {
				Map<String, Object> map = mapList.get(i);
				
				// 테스트 데이터
				/*
				List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("id_package", "10");
				temp.put("sale_prc", 0);
				temp.put("sale_prc_vat", 0);
				tempList.add(temp);temp = new HashMap<String, Object>();
				temp.put("id_package", 15);
				temp.put("sale_prc", 1500);
				temp.put("sale_prc_vat", 1500);
				tempList.add(temp);
				map.put("price", tempList);
				*/
				///////////////////
				
				if (!map.containsKey("price")) {
					continue;
				}
				// List형태의 price를 확인하면서 데이터를 처리한다.
				// param에 id_package를 가지고와서 동일한 정보를 넣어주고 없으면 기존값 그대로 넣어준다.
				List<Map<String, Object>> prices = getObjectToMapList(map.get("price"));
				if (prices == null) continue;
				for (int j = 0; j < prices.size(); j++) {
					Map<String, Object> price = prices.get(j);
					if (price.get("id_package") == null) continue;
					int pricePackAge = getStrToInt(price.get("id_package")+"");
					if (pricePackAge == 0) continue;
					
					if (pricePackAge == intPackAge) {
						copyMapData(map, price);
						break;
					}
				}
				// price 데이터 삭제
				map.remove("price");
			} catch (Exception e) {
				LogUtil.error("", "", "", "", "", e.toString());
			}
		}
	}
}
