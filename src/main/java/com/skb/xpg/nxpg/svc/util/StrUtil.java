package com.skb.xpg.nxpg.svc.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay Lee on 27/12/2016.
 */
public class StrUtil {
	
	
    public static <T extends CharSequence> T isNull(final T str, final T defaultStr) {
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
	
	// 문자열을 받아서 리스트로 만들어준다.
	public static List<String> getList(String value, String pattern) {
		List<String> result = new ArrayList<String>();
		if (value != null) {
			if (!value.isEmpty()) {
				int conIndex = value.indexOf(pattern);
				while (conIndex > -1) {
					result.add(value.substring(0, conIndex));
					value = value.substring(conIndex + 1, value.length());
					conIndex = value.indexOf(pattern);
				}
				result.add(value);
			}
		}
		return result;
	}
}
