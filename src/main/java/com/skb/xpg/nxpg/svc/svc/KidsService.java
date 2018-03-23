package com.skb.xpg.nxpg.svc.svc;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;

@Service
public class KidsService {

	@Autowired
	private RedisClient redisClient;
	
	// IF-NXPG-101
	public Map<String, Object> getMenuKzchar(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.MENU_GNB, param.get("menu_stb_svc_id")));
		} catch (Exception e) {
			return null;
		}
	}
	
	// IF-NXPG-102
	public Map<String, Object> getMenuKzgnb(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.MENU_ALL, param.get("menu_stb_svc_id")));
		} catch (Exception e) {
			return null;
		}
	}
}
