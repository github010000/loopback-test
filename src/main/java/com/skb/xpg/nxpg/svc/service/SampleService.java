package com.skb.xpg.nxpg.svc.service;

import java.util.HashMap;
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
import com.skb.xpg.nxpg.svc.util.LogUtil;

@Service
public class SampleService {

	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private Properties properties;
	
	public Map<String, Object> getSampleData() {
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "0000");
		result.put("if", "TSET-IF-001");
		for (int i = 0; i < 10000; i++) {
			result.put("key" + i, "value" + i);
		}
		
		return result;
	}

	public Map<String, Object> hgetRedis(String key, String field) {
		Map<String, Object> map = CastUtil.getObjectToMap(redisClient.hget(key, field));
		DoScript(key, map);
		return map;
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
			LogUtil.error(e.getStackTrace(), "", "", "", "", "", "");
		}
		
	}
}
