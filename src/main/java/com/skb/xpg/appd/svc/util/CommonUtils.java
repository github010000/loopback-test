package com.skb.xpg.appd.svc.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.skb.xpg.appd.svc.xpg.ResultCommon;


@Component
public class CommonUtils {
	public boolean validCheck(HttpServletRequest req, String[] essParamArr){
		boolean result = true;
		for( String paramName: essParamArr){
			String paramValue = req.getParameter(paramName);
			
			if( paramValue == null || "".equals(paramValue)){
				result = false;
				break;
			}
		}
		return result;
	}
	
	public Map<String, Object> returnError(String ifName, String ver, String returnCode){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("IF", ifName);
		result.put("ver", ver);
		setReturnCode( result, returnCode);
		return result;
	}
	
	public void setReturnCode( Map<String, Object> result, String returnCode){
		result.put("result", returnCode);
		result.put("reason", ResultCommon.resultCodeListMap.get(returnCode));
	}
}
