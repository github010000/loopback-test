package com.skb.xpg.appd.svc.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay Lee on 27/12/2016.
 */
public class StringUtils {
	// 문자열을 받아서 리스트로 만들어준다.
	public static List<String> getStringToList(String value, String pattern) {
		List<String> result = new ArrayList<String>();
		if (value != null) {
//			if (value.equals("") == false) {
			if (value.isEmpty() == false) {
				int conIndex = value.indexOf(pattern);
				while(conIndex > -1) {
					result.add(value.substring(0, conIndex));
					value = value.substring(conIndex+1, value.length());
					conIndex = value.indexOf(pattern);
				}
				result.add(value);
			}
		}
		return result;
	}
	public static String getLogString(String interfaceId, String type, String stbId, String data) {
		String log = "xpg-appd-svc";
		if (interfaceId == null) log += "|NULL";
		else log += "|" + interfaceId;
		if (type == null) log += "|NULL";
		else log += "|" + type;
		log += "|NULL"; // 트랜젝션 아이디
		if (stbId == null) log += "|NULL";
		else {
			stbId = stbId.replaceAll("\\{", "").replaceAll("\\}", "");
			log += "|" + stbId;
		}
		log += "|NULL"; // 외부 연동 시스템 명
		log += "|" +data;
		
		return log;
	}
	
    public static <T extends CharSequence> T defaultIfEmpty(final T str, final T defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static String upperCase(final String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }

}
