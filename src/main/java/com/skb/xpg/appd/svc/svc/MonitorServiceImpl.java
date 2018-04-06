package com.skb.xpg.appd.svc.svc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.stereotype.Service;

import com.skb.xpg.appd.svc.mongo.MongoManager;
import com.skb.xpg.appd.svc.redis.RedisClient;
import com.skb.xpg.appd.svc.util.StringUtils;
import com.skb.xpg.appd.svc.xpg.XpgCommon;

@Service
public class MonitorServiceImpl implements MonitorService {
	private static final Logger logger = LoggerFactory.getLogger(MonitorServiceImpl.class);
	
	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private MongoManager mongoManager;

	public  Map<String, Object>  getRedisCollectionList( ){
			
			Set<String> keys;
			
			Map<String, Object> KeyList = (Map<String, Object>) new HashMap();
			
			try {
				keys = (Set<String>) redisClient.keys("*");
				Iterator iterator = keys.iterator();

				// check values
				while (iterator.hasNext()) {
					String szKey = (String) iterator.next();

					Map<String, Object> obj = new HashMap();
					String szkeyType = redisClient.type( szKey );

					obj.put("key", szKey);
					obj.put("keytype", szkeyType);
					String szlen = "";
					if( szkeyType.equals(XpgCommon.__HASH) ){
						szlen = String.valueOf( redisClient.hlen( szKey ) );
					}
					else if( szkeyType.equals(XpgCommon.__STRING) ){
						szlen = String.valueOf( redisClient.strlen( szKey ) );
					}
					obj.put("len", szlen);

					KeyList.put(szKey, obj);
				}
 
			} catch (Exception e) {
				if( logger != null){logger.error("Error", e);}
				return KeyList;
			}
			 
			return KeyList;
	 }
	
	public Map<String, Object> getRedisCollection( Map<String, Object> param ){
		String collection		= StringUtils.defaultIfEmpty((String)param.get("collection"), "");
		String collectiontype   = StringUtils.defaultIfEmpty((String)param.get("collectiontype"), "");
		String getOnlykeys   = StringUtils.defaultIfEmpty((String)param.get("getOnlykeys"), "");
		String paramkey		= StringUtils.defaultIfEmpty((String)param.get("paramkey"), "");
		
		Map<String, Object> maps = new HashMap<String, Object>();
		
		boolean isKeyList =  "true".equals( getOnlykeys );
		
		boolean isHash = false;
		if( collectiontype != null){ isHash = collectiontype.equals("HASH");}
		 
		try {
			if (isKeyList) {
				maps = getRedisCollectionKeys( param );
			} else {
				if (!isHash) { // String 
					Object objectData = redisClient.get( collection );
					
					List<String> stringArraylist;
					if( objectData instanceof List ){
						stringArraylist = (ArrayList<String>)objectData;
						Collections.reverse(stringArraylist);
					    maps.put(collection, stringArraylist);
					}
					else{
						maps.put(collection, objectData);
					}
					
					return maps;
				}
				
				if (!"".equals(paramkey))
				{
					if (isHash) { // Hash
						Object rdata = redisClient.hget(collection, paramkey);

						maps.put(paramkey, rdata);
					} 
				} else { // List

					if (isHash) { // Hash
						boolean isBigData = true; // 20 over
						long nMaxDataLen = 20;

						Cursor<Entry<String, Object>> cursor = null;
						if (isBigData) {
							try {
								cursor = redisClient.hscan(collection);

								if (cursor != null) {
									int nCount = 0;
									while (cursor.hasNext()) {
										Entry<String, Object> e = cursor.next();

										maps.put(e.getKey(), e.getValue());

										if (nCount >= nMaxDataLen) {
											break;
										}
									}
//									cursor.close();
								}
							} catch (Exception e) {
								if( logger != null){logger.error("Error", e);}
//								if(cursor != null){
//									cursor.close();
//								}
							} finally {
								if ( cursor != null ) try{cursor.close();}catch(Exception e){logger.error("Error", e);} 
							}
						}
					} 
				}
			}
			
		} catch (Exception e) {
			if( logger != null){logger.error("Error", e);}
			return maps;
		}
		return maps;
	}
	
	public Map<String, Object> getRedisCollectionKeys( Map<String, Object> param ){
		String collection		= (String) param.get("collection");
		String collectiontype   = (String) param.get("collectiontype");
		int nDivValue = 5000;
		Set<String> keys;
		Map<String, Object> keyInfo = new TreeMap<String, Object>();
		int nDivisionCnt;
		try {
			if(collectiontype.equals(XpgCommon.__HASH)){
				keys = redisClient.hkeys(collection);
			}
			else {
				
				//logger.info("getRedisCollectionKeys 1");
				Object objectData = redisClient.get( collection );
				
				List<String> stringArraylist;
				if( objectData instanceof List ){
					stringArraylist = (ArrayList<String>)objectData;
					Collections.reverse(stringArraylist);
					keyInfo.put("KEYS1", stringArraylist);
				}
				else{
					keyInfo.put("KEYS1", objectData);
				}
				
				return keyInfo;
			}
			
			int nKeysLen = keys.size();
			List<String> list = new ArrayList<String>(keys);
			
			nDivisionCnt = (int)Math.ceil( (double)nKeysLen / nDivValue ) ;
			
			int startIndex = 0;
			int endIndex = nDivValue-1;
			int nCount = 1;
			while( nDivisionCnt > 0  ){
				
				if( endIndex >= nKeysLen){
					endIndex = nKeysLen-1;
				}
				
				Set<String> subSet = new LinkedHashSet<String>(list.subList(startIndex, endIndex+1));
				
				keyInfo.put("KEYS"+nCount, subSet);
				startIndex = startIndex + nDivValue;
				endIndex = endIndex + nDivValue;
				nDivisionCnt--;
				nCount++;
			} 
		} catch (Exception e) {
			if( logger != null){logger.error("Error", e);}
			return keyInfo;
		}
		
		return keyInfo;
	}

	@Override
	public List<Object> getMongoData(Map<String, Object> param) {
		String collectionName = (String) param.get("collectionName");
		String key = (String) param.get("key");
		
		List<Object> resultList = new ArrayList<>();
		
		try {
			if( key==null || "".equals(key)){
//				logger.info("collectionName: " + collectionName);
				resultList = mongoManager.getAllData(collectionName);
				
//				logger.info("resultList size: " + resultList.size());
			}else{
				Map<String, Object> data = mongoManager.getData(key, collectionName);
				resultList.add(data);				
			}
		} catch (Exception e) {
			if( logger != null){logger.error("Error", e);}
//			e.printStackTrace();
		}
		
		return resultList;
	}
}
