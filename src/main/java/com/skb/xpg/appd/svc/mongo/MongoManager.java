package com.skb.xpg.appd.svc.mongo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


@Component
public class MongoManager {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getData(String key, String collectionName) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(key));
		Map<String, Map<String, Object>> result = mongoTemplate.findOne(query, Map.class, collectionName);
		if(result != null){
			return result.get(key);
		}else{
			return null;
		}
	}
	
	public List<Object> getAllData(String collectionName) {
		
		return mongoTemplate.findAll(Object.class, collectionName);
	}
}