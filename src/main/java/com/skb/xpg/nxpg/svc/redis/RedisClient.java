package com.skb.xpg.nxpg.svc.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.service.CacheService;
import com.skb.xpg.nxpg.svc.util.LogUtil;

@Service
public class RedisClient {

	@Autowired
	@Qualifier("primaryRedisTemplate")
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	@Qualifier("secondRedisTemplate")
	private RedisTemplate<String, Object> secondRedisTemplate;

	@Autowired
	CacheService cacheService;
	

	public String hget(String key, String field) {
		Object obj = null;
		
		if (NXPGCommon.isUseFirstRedis()) {
			try {
				obj = redisTemplate.<String, Object>opsForHash().get(key, field);
			} catch (Exception e) {
				LogUtil.error("", "", "", "", "", e.toString());
				cacheService.addErrorCountAfterChangeRedis();
				obj = secondRedisTemplate.<String, Object>opsForHash().get(key, field);
			}
		} else {
			try {
				obj = secondRedisTemplate.<String, Object>opsForHash().get(key, field);
			} catch (Exception e) {
				LogUtil.error("", "", "", "", "", e.toString());
				cacheService.addErrorCountAfterChangeRedis();
				obj = redisTemplate.<String, Object>opsForHash().get(key, field);
			}
		}

		if(obj != null && obj instanceof String) {
			return (String) obj;
		} else return null;
	}
	
	
}
