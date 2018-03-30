package com.skb.xpg.nxpg.svc.service;

import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;

@Service
public class XpgService {

	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private Properties properties;
	
	public Map<String, Object> hgetRedis(String ver, String sub1, String sub2, Map<String, String> param) {
		
		Map<String, Object> api = properties.getApi();
		Map<String, Object> rtn = properties.getResults();
		rtn.put("ver", ver);
		if (api.containsKey(sub1 + "_" + sub2)) {
			Map<String, String> infos = CastUtil.getObjectToMapStringString(api.get(sub1 + "_" + sub2));
			String redis = infos.get("redis");
			String field = infos.get("field");
			String essential = infos.get("essential");
			
			if (param.isEmpty()) {
				if (essential != null) {
					rtn.put("result", "0003");
					rtn.put("reason", "필수 파라미터 누락");
					return rtn;
				}
			} else {
				for (String key : param.keySet()) {
					if (!essential.contains(key)) {
						rtn.put("result", "0003");
						rtn.put("reason", "필수 파라미터 누락");
						return rtn;
					}
				}
			}
			
			Map<String, Object> data = CastUtil.getObjectToMap(redisClient.hget(redis, param.get(field)));
			if (data == null) {
				rtn.put("result", "0002");
				rtn.put("reason", "데이터 없음");
			} else {
				DoScript(redis, data);
				rtn.put(infos.get("response_name"), data);	
			}
		} else {
			rtn.put("result", "0001");
			rtn.put("reason", "존재하지 않는 URL");
		}
		
		
		return rtn;
	}
	
	private void DoScript(String collection, Map<String, Object> map) {
		Map<String, List<String>> scriptMap = properties.getScripts();
		if (!scriptMap.containsKey(collection)) return;
		
		try {

			ScriptEngineManager manager = new ScriptEngineManager();

		    ScriptEngine engine = manager.getEngineByName("JavaScript");
		    
		    List<String> list = scriptMap.get(collection);
		    
		    for (String val : list) {
		    	String[] arr = val.split("\\|");
			    if (arr != null && arr.length > 2) {
				    engine.put(arr[0], "");
			    	for (String key : arr[1].split("\\^")) {
					    engine.put(key, map.get(key) + "");
			    	}
				    
				    engine.eval(arr[2]);
				    map.put(arr[0], engine.get(arr[0]));
			    }
		    }
		    
		} catch (ScriptException | NullPointerException e) {
			e.printStackTrace();
		}
		
	}
}
