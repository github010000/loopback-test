package com.skb.xpg.nxpg.svc.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;

@Service
public class GridService {

	@Autowired
	private RedisClient redisClient;
	
	// IF-NXPG-006
	public Map<String, Object> getGrid(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget(NXPGCommon.GRID_CONTENTS, param.get("menu_id")));
		} catch (Exception e) {
			return null;
		}
	}
	
	// IF-NXPG-007
	public Map<String, Object> getGridEvent(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget("grid_banner", param.get("menu_id")));
		} catch (Exception e) {
			return null;
		}
	}
}
