package com.skb.xpg.nxpg.svc.svc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;

@Service
public class LftService {

	@Autowired
	private RedisClient redisClient;
	
	// IF-NXPG-403
	public Map<String, Object> getContentsLftsynop(String ver, Map<String, String> param) {
		try {
			return CastUtil.StringToJsonMap((String) redisClient.hget("synopsis_liveChildStory", param.get("epsd_id")));
		} catch (Exception e) {
			return null;
		}
	}
}
