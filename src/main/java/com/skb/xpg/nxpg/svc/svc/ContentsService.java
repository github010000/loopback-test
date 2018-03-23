package com.skb.xpg.nxpg.svc.svc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;
import com.skb.xpg.nxpg.svc.util.StrUtil;

@Service
public class ContentsService {

	@Autowired
	private RedisClient redisClient;

	@Autowired
	private Properties properties;

	// @SuppressWarnings("serial")
	// public Map<String, Object> getSynopsis(String ver, Map<String, String> param)
	// {
	//
	// Map<String, Object> rtn = properties.getResults();
	//
	// //List<Object> list =
	// redisClient.oget("Contents:{FEF4B660-65F1-11E2-B2A1-FF7E72903272}|Contents:{C0011BCB-6C24-11E7-A687-D9AF8E5F116C}");
	//
	// String json = CastUtil.getObjectToString(redisClient.luaget(new
	// ArrayList<String>(){{ add("Contents"); add("Contents"); }},
	// "{092561E3-049F-11DF-ACB7-9B52BCC57005}"));
	//
	// rtn.put("content1", CastUtil.StringToJsonMap(json));
	// rtn.put("content2", "");
	//
	// return rtn;
	// }
	//
	// public Map<String, Object> getSynopsis2(String ver, Map<String, String>
	// param) {
	//
	// Map<String, Object> rtn = properties.getResults();
	//
	// rtn.put("content1", redisClient.hget("Contents",
	// "{C0011BCB-6C24-11E7-A687-D9AF8E5F116C}"));
	// rtn.put("content2", redisClient.hget("Contents",
	// "{FEF4B660-65F1-11E2-B2A1-FF7E72903272}"));
	//
	// return rtn;
	// }
	//
	// private void DoScript(String collection, Map<String, Object> map) {
	// Map<String, List<String>> scriptMap = properties.getScripts();
	// if (!scriptMap.containsKey(collection)) return;
	//
	// try {
	//
	// ScriptEngineManager manager = new ScriptEngineManager();
	//
	// ScriptEngine engine = manager.getEngineByName("JavaScript");
	//
	// List<String> list = scriptMap.get(collection);
	//
	// for (String val : list) {
	// String[] arr = val.split("\\|");
	// if (arr != null && arr.length > 2) {
	// engine.put(arr[0], "");
	// for (String key : arr[1].split("\\^")) {
	// engine.put(key, map.get(key) + "");
	// }
	//
	// engine.eval(arr[2]);
	// map.put(arr[0], engine.get(arr[0]));
	// }
	// }
	//
	// } catch (ScriptException | NullPointerException e) {[
	// e.printStackTrace();
	// }
	//
	// }

	// public Object getContentsRating(String ver, Map<String, String> param) {
	// Map<String, Object> rtn = properties.getResults();
	//
	// }

	// IF-NXPG-008
	public Map<String, Object> getContentsRating(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_RATING, param.get("sris_id")));
		} catch (Exception e) {
			return null;
		}
	}

	// IF-NXPG-010
	public Map<String, Object> getContentsSynopsis(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_SYNOPSIS, param.get("sris_id")));
		} catch (Exception e) {
			return null;
		}
	}

	// IF-NXPG-011
	public Map<String, Object> getContentsPeople(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_PEOPLE, param.get("sris_id")));
		} catch (Exception e) {
			return null;
		}
	}

	// IF-NXPG-014
	public Map<String, Object> getContentsGwsynop(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_GWSYNOP, param.get("sris_id")));
		} catch (Exception e) {
			return null;
		}
	}

	// IF-NXPG-015
	public Map<String, Object> getContentsCommerce(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_COMMERCE, param.get("sris_id")));
		} catch (Exception e) {
			return null;
		}
	}

	// IF-NXPG-016
	public Map<String, Object> getContentsCorner(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_CORNER, param.get("sris_id")));
		} catch (Exception e) {
			return null;
		}
	}

	// IF-NXPG-017
	public Map<String, Object> getContentsVodList(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.CONTENTS_VODLIST, param.get("pnr_id")));
		} catch (Exception e) {
			return null;
		}
	}
}
