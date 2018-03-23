package com.skb.xpg.nxpg.svc.svc;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.redis.RedisClient;

@Service
public class MenuService {

	@Autowired
	private RedisClient redisClient;
	
	public Object getMenuGnb(String ver, Map<String, String> param) {
		return redisClient.hget(NXPGCommon.MENU_GNB, param.get("menu_stb_svc_id"));
	}
}
