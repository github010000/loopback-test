package com.skb.xpg.nxpg.svc.util;

import java.util.Map;

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
	private static String firstProfile;
	public static boolean logSwitch;
	
	@Autowired
	@Qualifier("firstProfile")
    private void setFirstProfile(String firstProfile){
		LogUtil.firstProfile = firstProfile;
    }

	@Value("${user.logswitch}")
    private void setLogSwitch(boolean logSwitch){
		LogUtil.logSwitch = logSwitch;
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
	
	public static void tlog(String interfaceId, String transactionType, String transactionId, String stbId, String extName, Map data) {
		if (logSwitch && logger != null) {
			logger.info(getLogString(interfaceId, transactionType, transactionId, stbId, extName, data));
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
		if (data instanceof String) {
			data = "{\"msg\":\"" + data + "\"}";
		} else if (data instanceof Map) {
			data = CastUtil.getMapToString(CastUtil.getObjectToMap(data));
		} else {
			data = "{\"msg\":\"none\"}";
		}
		
		log += "|" + data;

		return log;
	}
}