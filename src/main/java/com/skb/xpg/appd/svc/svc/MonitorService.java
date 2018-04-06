package com.skb.xpg.appd.svc.svc;

import java.util.List;
import java.util.Map;

public interface MonitorService {
	Map<String, Object> getRedisCollectionList();
	Map<String, Object> getRedisCollection(Map<String, Object> param);
	Map<String, Object> getRedisCollectionKeys(Map<String, Object> param);
	
	List<Object> getMongoData(Map<String, Object> param);
}
