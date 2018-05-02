//package com.skb.xpg.nxpg.svc.cache;
//
//import java.util.Date;
//
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Repository;
//
//import com.skb.xpg.nxpg.svc.common.NXPGCommon;
//
//@Repository
//public class CacheRepositoryImpl implements CacheRepository {
//
//	public CacheRepositoryImpl() {
//	}
//
//	@Override
//	@Cacheable(value = "errorCache")
//	public long redisErrorCache() {
//		NXPGCommon.redisErrorCount = 0;
//		Date dt = new Date();
//		return dt.getTime();
//	}
//
//}