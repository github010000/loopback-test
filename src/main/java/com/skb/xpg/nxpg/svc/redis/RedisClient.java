package com.skb.xpg.nxpg.svc.redis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.service.CacheService;
import com.skb.xpg.nxpg.svc.util.CastUtil;
import com.skb.xpg.nxpg.svc.util.LogUtil;

@Service
@RefreshScope
public class RedisClient {

	@Autowired
	@Qualifier("primaryRedisTemplate")
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	@Qualifier("secondRedisTemplate")
	private RedisTemplate<String, Object> secondRedisTemplate;
	
	@Autowired
	CacheService cacheService;
	

	public String hget(String key, String field, Map<String, String> param) {
		Object obj = null;
		
		Map<String, Object> keyAndField = new HashMap<String, Object>();
		keyAndField.put("key", key);
		keyAndField.put("field", field);
		
		LogUtil.tlog(param.get("IF"), "SEND.REQ", param.get("UUID"), param.get("stb_id"), "REDIS", CastUtil.getMapToString(keyAndField));
		
		
		if (NXPGCommon.isUseFirstRedis()) {
			try {
				obj = redisTemplate.<String, Object>opsForHash().get(key, field);
				
				LogUtil.tlog(param.get("IF"), "RECV.RES", param.get("UUID"), param.get("stb_id"), "REDIS", obj);
				
			} catch (Exception e) {
				LogUtil.error(param.get("IF"), "", param.get("UUID"), param.get("stb_id"), "REDIS", e.getStackTrace()[0].toString());
				cacheService.addErrorCountAfterChangeRedis();
				obj = secondRedisTemplate.<String, Object>opsForHash().get(key, field);
			}
		} else {
			try {
				obj = secondRedisTemplate.<String, Object>opsForHash().get(key, field);
				
				LogUtil.tlog(param.get("IF"), "RECV.RES", param.get("UUID"), param.get("stb_id"), "REDIS", obj);
				
			} catch (Exception e) {
				LogUtil.error(param.get("IF"), "", param.get("UUID"), param.get("stb_id"), "REDIS", e.getStackTrace()[0].toString());
				cacheService.addErrorCountAfterChangeRedis();
				obj = redisTemplate.<String, Object>opsForHash().get(key, field);
			}
		}

		if(obj != null && obj instanceof String) {
			return (String) obj;
		} else return null;
	}
	
	
}
