package com.skb.xpg.nxpg.svc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogUtil {
	
	private static Logger logger = LoggerFactory.getLogger(LogUtil.class.getName());

	public static void info(String className, String interfaceId, String transactionType, String transactionId, String stbId, String extName, String data) {
		String logLevel = "info";
		if (logger != null) {
			logger.info(getLogString(logLevel, interfaceId, transactionType, transactionId, stbId, extName, data));
		}
	}

	public static void error(StackTraceElement[] elements, String interfaceId, String transactionType, String transactionId, String stbId, String extName, String data) {
		String logLevel = "Error";
		if (elements != null && elements.length > 0) {
			
			if (logger != null) {
				logger.error(getLogString(logLevel, interfaceId, transactionType, 
					transactionId, stbId, extName, /*"BACH", null,*/
					elements[0].toString() + " - " + elements[0].getMethodName() + "(" + elements[0].getLineNumber() + ")"));
				
				logger.error(getLogString(logLevel, interfaceId, transactionType, 
						transactionId, stbId, extName, /*"BACH", null,*/
						elements[0].getClassName() + " - " + elements[1].getMethodName() + "(" + elements[1].getLineNumber() + ")"));
			}
		}
	}
	
	public static void debug(String className, String interfaceId, String transactionType, String transactionId, String stbId, String extName, String data) {
		String logLevel = "debug";
		if (logger != null) {
			logger.debug(getLogString(logLevel, interfaceId, transactionType, transactionId, stbId, extName, data));
		}
	}

	private static String getLogString(String logLevel, String interfaceId, String transactionType, String transactionId, String stbId, String extName, String data) {
		String log = "";
		String serviceName = "xpg-svc";
		
		log = DateUtil.getYYYYMMDDhhmmssms();
		
		log += "|" + serviceName;
		if (logLevel == null)
			log += "|NULL";
		else
			log += "|" + logLevel;
		if (interfaceId == null)
			log += "|NULL";
		else
			log += "|" + interfaceId;
		if (transactionType == null)
			log += "|NULL";
		else
			log += "|" + transactionType;
		if (transactionId == null)
			log += "|NULL";
		else
			log += "|" + transactionId;// 트랜잭션 아이디 
		if (stbId == null) {
			log += "|NULL";
		} else {
			stbId = stbId.replaceAll("\\{", "").replaceAll("\\}", "");
			log += "|" + stbId;
		}
		if (extName == null) {
			log += "|NULL";
		} else {
			log += "|" + extName;
		}
		log += "|" + data;

		return log;
	}
}