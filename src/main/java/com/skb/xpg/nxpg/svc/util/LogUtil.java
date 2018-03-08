package com.skb.xpg.nxpg.svc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogUtil {

	public static void info(String className, String interfaceId, String type, String stgId, String data) {
		Logger logger = LoggerFactory.getLogger(className);
		
		if (logger != null) {
			logger.debug(getLogString(interfaceId, type, stgId, data));
		}
	}

	public static void error(StackTraceElement[] elements, String interfaceId) {
		Logger logger;
		if (elements != null && elements.length > 0) {
			for (int iterator = 1; iterator <= elements.length; iterator++) {
				logger = LoggerFactory.getLogger(elements[iterator - 1].getClassName());
				if (logger != null) {
					logger.error(getLogString(interfaceId, "BACH", null,
							elements[iterator - 1].getMethodName() + "(" + elements[iterator - 1].getLineNumber()
									+ ")"));
				}
			}
		}
	}

	public static void debug(String className, String interfaceId, String type, String stgId, String data) {
		Logger logger = LoggerFactory.getLogger(className);
		if (logger != null) {
			logger.debug(getLogString(interfaceId, type, stgId, data));
		}
	}

	private static String getLogString(String interfaceId, String type, String stbId, String data) {
		String log = "xpg-svc";
		if (interfaceId == null)
			log += "|NULL";
		else
			log += "|" + interfaceId;
		if (type == null)
			log += "|NULL";
		else
			log += "|" + type;
		log += "|NULL"; // 트랜젝션 아이디
		if (stbId == null)
			log += "|NULL";
		else {
			stbId = stbId.replaceAll("\\{", "").replaceAll("\\}", "");
			log += "|" + stbId;
		}
		log += "|NULL"; // 외부 연동 시스템 명
		log += "|" + data;

		return log;
	}

}
