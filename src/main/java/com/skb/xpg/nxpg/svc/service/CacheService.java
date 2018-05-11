package com.skb.xpg.nxpg.svc.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.util.LogUtil;

@Service
public class CacheService {
	
//	@Autowired
//	private CacheRepository cacheRepository;
//	
//	@Cacheable(value = "findMemberCache", key="#name")
//	private Long getErrorDate(String name) {
//		System.out.println("new @Cacheable!!");
//		NXPGCommon.redisErrorCount = 0;
//		Date dt = new Date();
//		return dt.getTime();
//	}
	
	private float startTime = 0;
	private int redisErrorCount;
	
	@Value("${second.redis.switching-milisecond}")
	private float switchingTime;
	
	public int getErrorCount() {
		return redisErrorCount;
	}
	
	public int addErrorCountAfterChangeRedis() {
		if (startTime == 0) {
			startTime = System.nanoTime();
		}
		float now_time = System.nanoTime();
		float difference = (now_time - startTime) / (float) 1e6;
		
		if (difference <= switchingTime) {
			
			redisErrorCount++;
			if (redisErrorCount > 10) {
				LogUtil.info("", "", "", "", "", "REDIS SWITCH");
				NXPGCommon.switchUseRedis();
				redisErrorCount = 0;
			}
			
		} else {
			redisErrorCount = 0;
			startTime = System.nanoTime();
		}
//		System.out.println("asdf_" + redisErrorCount);
		return redisErrorCount;
//		long diff = new Date().getTime() - cacheRepository.redisErrorCache();
//		long diffSeconds = diff / 1000;
//		if (diffSeconds <= 10) {
//			NXPGCommon.redisErrorCount++;
//			if (NXPGCommon.redisErrorCount > 10) {
//				NXPGCommon.switchUseRedis();
//				NXPGCommon.redisErrorCount = 0;
//				return true;
//			}
//		} else {
//			NXPGCommon.redisErrorCount = 0;
//		}
//		return false;
	}
}
