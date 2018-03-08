package com.skb.xpg.nxpg.svc.svc;

import java.util.ArrayList;
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
public class ContentsService {

	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private Properties properties;
	
	@SuppressWarnings("serial")
	public Map<String, Object> getSynopsis(String ver, Map<String, String> param) {

		Map<String, Object> rtn = properties.getResults();
		
		//List<Object> list = redisClient.oget("Contents:{FEF4B660-65F1-11E2-B2A1-FF7E72903272}|Contents:{C0011BCB-6C24-11E7-A687-D9AF8E5F116C}");
		
		String json = CastUtil.getObjectToString(redisClient.luaget(new ArrayList<String>(){{ add("Contents"); add("Contents"); }}, "{092561E3-049F-11DF-ACB7-9B52BCC57005}"));
		
		rtn.put("content1", CastUtil.StringToJsonMap(json));
		rtn.put("content2", "");
		
		return rtn;
	}
	
	public Map<String, Object> getSynopsis2(String ver, Map<String, String> param) {

		Map<String, Object> rtn = properties.getResults();
		
		rtn.put("content1", redisClient.hget("Contents", "{C0011BCB-6C24-11E7-A687-D9AF8E5F116C}"));
		rtn.put("content2", redisClient.hget("Contents", "{FEF4B660-65F1-11E2-B2A1-FF7E72903272}"));
		
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
