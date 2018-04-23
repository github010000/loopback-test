package com.skb.xpg.nxpg.svc.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.cache.CacheRepository;
import com.skb.xpg.nxpg.svc.common.NXPGCommon;

@Service
public class CacheService {
	
	@Autowired
	private CacheRepository cacheRepository;
//	
//	@Cacheable(value = "findMemberCache", key="#name")
//	private Long getErrorDate(String name) {
//		System.out.println("new @Cacheable!!");
//		NXPGCommon.redisErrorCount = 0;
//		Date dt = new Date();
//		return dt.getTime();
//	}
	
	public boolean addErrorCountAfterChangeRedis() {
		long diff = new Date().getTime() - cacheRepository.redisErrorCache();
		long diffSeconds = diff / 1000;
		if (diffSeconds <= 10) {
			NXPGCommon.redisErrorCount++;
			if (NXPGCommon.redisErrorCount > 10) {
				NXPGCommon.switchUseRedis();
				NXPGCommon.redisErrorCount = 0;
				return true;
			}
		} else {
			NXPGCommon.redisErrorCount = 0;
		}
		return false;
	}
}
