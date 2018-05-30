package com.skb.xpg.nxpg.svc.util;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class LogUtil {
	
	private static String HostName;
	private static String HostIp;
	public static boolean useLogForResponseData;
	
	@Value("${user.useLogForResponseData}")
    private void setLogSwitch(boolean useLogForResponseData){
		LogUtil.useLogForResponseData = useLogForResponseData;
    }
	
	@Value("${user.logging.name}")
    private void setHostName(String HostName){
		LogUtil.HostName = HostName;
    }
	
	@Value("${user.logging.instance_index}")
    private void setHostIp(String HostIp){
		LogUtil.HostIp = HostIp;
    }
	
	private static Logger logger = LoggerFactory.getLogger(LogUtil.class.getName());

	public static void tlog(String interfaceId, String transactionType, String transactionId, String stbId, String extName, Object allData) {
		if (logger != null) {
			logger.info(getLogString(interfaceId, transactionType, transactionId, stbId, extName, allData));
		}
	}
	
	public static void tlog(String interfaceId, String transactionType, String transactionId, String stbId, String extName, Map allData, Map noResponse) {
		if (logger != null) {
			if (LogUtil.useLogForResponseData) {
				logger.info(getLogString(interfaceId, transactionType, transactionId, stbId, extName, allData));
			} else {
				logger.info(getLogString(interfaceId, transactionType, transactionId, stbId, extName, noResponse));
			}
		}
	}
	
	public static void info(String interfaceId, String transactionType, String transactionId, String stbId, String extName, String data) {
		if (logger != null) {
			logger.info(getLogString(interfaceId, transactionType, transactionId, stbId, extName, data));
		}
	}

	public static void error(String interfaceId, String transactionType, String transactionId, String stbId, String extName, String data) {
		if (logger != null) {
			logger.error(getLogString(interfaceId, transactionType, transactionId, stbId, extName, data));
		}
	}
	
	public static void debug(String interfaceId, String transactionType, String transactionId, String stbId, String extName, String data) {
		if (logger != null) {
			logger.debug(getLogString(interfaceId, transactionType, transactionId, stbId, extName, data));
		}
	}

	private static String getLogString(String interfaceId, String transactionType, String transactionId, String stbId, String extName, Object data) {
		String log = "";
		
		
		// HostName
		log += HostName;
		// HostIP
		log += "|" + HostIp;
		
//		
//		if (hostName == null)
//			log += "|NULL";
//		else
//			log += "|" + hostName;
//		if (hostIp == null)
//			log += "|NULL";
//		else
//			log += "|" + hostIp;
		
		if (interfaceId == null || interfaceId.isEmpty())
			log += "|NULL";
		else
			log += "|" + interfaceId;
		
		if (transactionType == null || transactionType.isEmpty())
			log += "|REQ";
		else
			log += "|" + transactionType;
		if (transactionId == null || transactionId.isEmpty())
			log += "|NULL";
		else
			log += "|" + transactionId;// 트랜잭션 아이디 
		if (stbId == null || stbId.isEmpty()) {
			log += "|NULL";
		} else {
//			stbId = stbId.replaceAll("\\{", "").replaceAll("\\}", "");
			log += "|" + stbId;
		}
		if (extName == null || extName.isEmpty()) {
			log += "|NULL";
		} else {
			log += "|" + extName;
		}
		
//		data = data.replaceAll(":", "=");
//		data = data.replaceAll("[\\{|\\}|\"]", "");
		String msg = "";
		if (data instanceof String) {
			msg = "{\"msg\":\"" + data + "\"}";
		} else if (data instanceof Map) {
			msg = CastUtil.getMapToString(CastUtil.getObjectToMap(data));
		} else if (data instanceof List) {
			msg = CastUtil.getObjectToJsonArrayString(data);
		} else {
			data = "{\"msg\":\"none\"}";
		}
		
		log += "|" + data;

		return log;
	}
}