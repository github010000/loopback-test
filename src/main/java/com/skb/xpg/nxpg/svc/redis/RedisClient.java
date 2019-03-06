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

	public Long hdel(String key, String field) {
		redisTemplate.<String, Object>opsForHash().delete(key, field);
		return secondRedisTemplate.<String, Object>opsForHash().delete(key, field);
	}

	public String hget(String key, String field, Map<String, String> param) {
		Object obj = null;
		
		Map<String, Object> keyAndField = new HashMap<String, Object>();
		keyAndField.put("key", key);
		keyAndField.put("field", field);
		
		String stb_id = "";
		if (param.containsKey("stb_id")) {
			stb_id = param.get("stb_id");
		} else {
			stb_id = param.get("cw_stb_id");
		}
		LogUtil.tlog(param.get("IF"), "SEND.REQ", param.get("UUID"), stb_id, "REDIS", CastUtil.getMapToString(keyAndField));
		
		
		if (NXPGCommon.isUseFirstRedis()) {
			try {
				obj = redisTemplate.<String, Object>opsForHash().get(key, field);
				int cnt = 0;
				if (param.containsKey("redis_count")) {
					cnt = CastUtil.getStringToInteger(param.get("redis_count").toString());
				}
				cnt = cnt + 1;
				param.put("redis_count", cnt + "");
				
				LogUtil.tlog(param.get("IF"), "RECV.RES", param.get("UUID"), stb_id, "REDIS", CastUtil.getMapToString(keyAndField));
				
			} catch (Exception e) {
				LogUtil.error(param.get("IF"), "RECV.RES", param.get("UUID"), stb_id, "REDIS", e.getStackTrace()[0].toString());
				cacheService.addErrorCountAfterChangeRedis();
				obj = secondRedisTemplate.<String, Object>opsForHash().get(key, field);
			}
		} else {
			try {
				obj = secondRedisTemplate.<String, Object>opsForHash().get(key, field);
				int cnt = 0;
				if (param.containsKey("redis_count")) {
					cnt = CastUtil.getStringToInteger(param.get("redis_count").toString());
				}
				cnt = cnt + 1;
				param.put("redis_count", cnt + "");
				
				LogUtil.tlog(param.get("IF"), "RECV.RES", param.get("UUID"), stb_id, "REDIS", CastUtil.getMapToString(keyAndField));
				
			} catch (Exception e) {
				LogUtil.error(param.get("IF"), "RECV.RES", param.get("UUID"), stb_id, "REDIS", e.getStackTrace()[0].toString());
				cacheService.addErrorCountAfterChangeRedis();
				obj = redisTemplate.<String, Object>opsForHash().get(key, field);
			}
		}

		if(obj != null && obj instanceof String) {
			return (String) obj;
		} else return null;
	}
	
	
}
