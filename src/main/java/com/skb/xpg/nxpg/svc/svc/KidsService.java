package com.skb.xpg.nxpg.svc.svc;

import java.util.List;
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
	public List getMenuKzchar(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonList((String) redisClient.hget("menu_kidsCharacter", param.get("menu_id")));
		} catch (Exception e) {
			return null;
		}
	}
	
	// IF-NXPG-102
	public List getMenuKzgnb(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonList((String) redisClient.hget("menu_kidsGnb", param.get("menu_stb_svc_id")));
		} catch (Exception e) {
			return null;
		}
	}
}
