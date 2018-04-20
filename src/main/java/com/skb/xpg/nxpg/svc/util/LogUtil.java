package com.skb.xpg.nxpg.svc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogUtil {

	public static void info(String className, String interfaceId, String transactionType, String transactionId, String stbId, String extName, String data) {
		String logLevel = "info";
		Logger logger = LoggerFactory.getLogger(className);
		if (logger != null) {
			logger.debug(getLogString(logLevel, interfaceId, transactionType, transactionId, stbId, extName, data));
		}
	}


	public static void error(StackTraceElement[] elements, String interfaceId, String transactionType, String transactionId, String stbId, String extName, String data) {
		String logLevel = "Error";
		Logger logger = LoggerFactory.getLogger(elements[0].getClassName());;
		if (elements != null && elements.length > 0) {
			for (int iterator = 1; iterator <= elements.length; iterator++) {
//				logger = LoggerFactory.getLogger(elements[iterator - 1].getClassName());
				if (logger != null) {
					logger.error(getLogString(logLevel, interfaceId, transactionType, 
							transactionId, stbId, extName, /*"BACH", null,*/
							elements[iterator - 1].getMethodName() + "(" + elements[iterator - 1].getLineNumber()
									+ ")"));
				}
				break;
			}
		}
//		logger.error(elements.toString());
	}
	
	public static void debug(String className, String interfaceId, String transactionType, String transactionId, String stbId, String extName, String data) {
		String logLevel = "debug";
		Logger logger = LoggerFactory.getLogger(className);
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
		if (stbId == null)
			log += "|NULL";
		else {
			stbId = stbId.replaceAll("\\{", "").replaceAll("\\}", "");
			log += "|" + stbId;
		}
		if (extName == null)
			log += "|NULL";
		else
			log += "|" + extName;
			log += "|" + data;

		return log;
	}
}