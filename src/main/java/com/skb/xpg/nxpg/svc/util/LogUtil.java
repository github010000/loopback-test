package com.skb.xpg.nxpg.svc.util;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class LogUtil {
	
	private static String HostName = "xpg-nxpg-svc";
	public static String HostIp;
	public static boolean useLogForResponseData;
	
	public static int zipoThreadCount = 0;
	
	@Value("${user.useLogForResponseData}")
    private void setLogSwitch(boolean useLogForResponseData){
		LogUtil.useLogForResponseData = useLogForResponseData;
    }
	
//	@Value("${user.logging.name}")
//    private void setHostName(String HostName){
//		LogUtil.HostName = HostName;
//    }
	
	@Value("${user.logging.instance_index}")
    private void setHostIp(String HostIp){
		LogUtil.HostIp = HostIp;
    }
	
	private static Logger logger = LoggerFactory.getLogger(LogUtil.class.getName());

	public static void tlog(String interfaceId, String transactionType, String transactionId, String stbId, String extName, Object noResponse) {
		if (logger != null) {
			logger.info(getLogString(interfaceId, transactionType, transactionId, stbId, extName, noResponse));
		}
	}
	
	public static void rlog(String interfaceId, String transactionType, String transactionId, String stbId, String extName, Object noResponse) {
		if (logger != null) {
//			if (LogUtil.useLogForResponseData) {
//				logger.info(getLogString(interfaceId, transactionType, transactionId, stbId, extName, allData));
//			} else {
			logger.info(getLogString(interfaceId, transactionType, transactionId, stbId, extName, printProcessTime(noResponse)));
//			}
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
			log += "|SEND.RES";
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
			if (data.toString().startsWith("{")) {
				msg = data + "";
			} else {
				msg = "{\"msg\":\"" + data + "\"}";
			}
		} else if (data instanceof Map) {
			msg = CastUtil.getMapToString(CastUtil.getObjectToMap(data));
		} else if (data instanceof List) {
			
			msg = CastUtil.getObjectToJsonArrayString(data);
			
		} else {
			msg = "{\"msg\":\"none\"}";
		}
		
		log += "|" + msg;

		return log;
	}
	
	private static String printProcessTime(Object object) {
		
		Map<String, String> param = CastUtil.getObjectToMapStringString(object);
		
		double start = 0;
		if (param.containsKey("time_start")) {
			start = CastUtil.getStringToLong(param.get("time_start"));
		}
		double end = 0;
		if (param.containsKey("time_end")) {
			end = CastUtil.getStringToLong(param.get("time_end"));
		}
		
		String redis_count = param.get("redis_count");
		
		double rtime = (end - start) / (double) 1000000000.0;
		
		String str_time = "0.000";
		
		// 만약 start, end가 둘중 하나라도 없으면 데이터를 안넣어준다.
		if (start > 0.0 && end > 0.0)
			str_time = String.format("%.3f", rtime);
		
		String log = "";
		log += "{xpg_res},res_time=" + str_time + ",redis_hget_cnt=" + redis_count + "";
//		log += ",thread_cnt=" + Thread.activeCount() + "";
		
		if (param.containsKey("cw_call_id") && param.containsKey("cw_time_start") && param.containsKey("cw_time_end")) {
			
			double cw_start = CastUtil.getStringToLong(param.get("cw_time_start"));
			double cw_end = CastUtil.getStringToLong(param.get("cw_time_end"));
			
//			long after_start = CastUtil.getStringToLong(param.get("after_start"));
//			long after_end = CastUtil.getStringToLong(param.get("after_end"));
//			
			double cw = (cw_end - cw_start) / (double) 1000000000;
			double biz = (end - cw_end) / (double) 1000000000;
			String cw_time = String.format("%.3f", cw);
			String biz_time = String.format("%.3f", biz);
//			float after = (after_end - after_start) / 1000000 / 1000;
			
			log += ",cw=" + cw_time;
			
			
			double cwt = 0;
			double cwd = 0;
			double bizt = 0;
			double bizd = 0;
			
			if (param.containsKey("cw_thread_time_start") && param.containsKey("cw_thread_time_end")) {
				double cw_thread_start = CastUtil.getStringToLong(param.get("cw_thread_time_start"));
				double cw_thread_end = CastUtil.getStringToLong(param.get("cw_thread_time_end"));
				cwt = (cw_thread_end - cw_thread_start) / (double) 1000000000;
				cwd = cw - cwt;
				log += ",cwdelay=" + String.format("%.3f", cwd) + ",cwthread=" + String.format("%.3f", cwt);
			} else {
				log += ",0,0";
			}
			log += ",biz=" + biz_time;
			
			if (param.containsKey("biz_thread_time_start") && param.containsKey("biz_thread_time_end")) {
				double biz_thread_start = CastUtil.getStringToLong(param.get("biz_thread_time_start"));
				double biz_thread_end = CastUtil.getStringToLong(param.get("biz_thread_time_end"));
				bizt = (biz_thread_end - biz_thread_start) / (double) 1000000000;
				bizd = biz - bizt;
				log += ",bizdelay=" + String.format("%.3f", bizd) + ",bizthread=" + String.format("%.3f", bizt);
			} else {
				log += ",0,0";
			}
			
			if (param.containsKey("cw_call_id")) {
				log += ",cwcallid=" + param.get("cw_call_id") + "";
			}
			
			if (param.containsKey("cw_exception")) {
				log += "," + param.get("cw_exception") + "";
			}
			if (param.containsKey("cwt_exception")) {
				log += "," + param.get("cwt_exception") + "";
			}
			if (param.containsKey("biz_exception")) {
				log += "," + param.get("biz_exception") + "";
			}
		}
		
		return log;
	}
	
}