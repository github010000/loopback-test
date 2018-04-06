package com.skb.xpg.appd.svc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RestController;

import com.skb.xpg.appd.svc.redis.RedisClient;
import com.skb.xpg.appd.svc.svc.MonitorService;
import com.skb.xpg.appd.svc.util.StringUtils;


@RestController
@RequestMapping("/mnt")
public class MonitorController {
	private static final Logger logger = LoggerFactory.getLogger(MonitorController.class.getName());
	
	@Autowired
	private MonitorService mntService;
	
	@Autowired
	private RedisClient redisClient;
	
	@RequestMapping(params="m=getMNTRedisCollectionList")
	public String getMNTRedisCollectionList(HttpServletRequest request, HttpServletResponse response) /*throws Exception*/ {
		// logger.debug("[REQ] " + request.getQueryString());
		if( request != null)
			if( logger != null){logger.debug(StringUtils.getLogString("getMNTRedisCollectionList", "RECV.REQ", null, request.getQueryString()));}
		
		Map<String, Object> keyset  = mntService.getRedisCollectionList();
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>( keyset );
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("keyset", treeMap);
		
		return result.toString();
//		return new ModelAndView("MNTRedisViewer", result);
	}
	
	@RequestMapping(params="m=getMNTRedisCollection")
	public String getMNTRedisCollection(HttpServletRequest request, HttpServletResponse response) /*throws Exception*/ {
		
		Map<String, Object> param = new HashMap<String, Object>();

		String collectiontype =  StringUtils.defaultIfEmpty(request.getParameter("collectiontype"), "");
		String getOnlykeys = StringUtils.defaultIfEmpty(request.getParameter("getOnlykeys"), "");
		String paramkey = StringUtils.defaultIfEmpty(request.getParameter("paramkey"), "");
		String collection = StringUtils.defaultIfEmpty(request.getParameter("collection"), "");

		param.put("paramkey", paramkey );
		param.put("collection", collection );
		param.put("collectiontype", collectiontype);
		param.put("getOnlykeys", getOnlykeys );

		Map<String, Object> keyset;
		Map<String, Object> result = new HashMap<String, Object>();
		
		keyset  = mntService.getRedisCollection(param);
		
		if( !"".equals(paramkey) || (!"".equals(paramkey) && "STRING".equals(collectiontype) ) ){
			result = keyset;
		}
		else{ // List
			if( collection != null && !collection.isEmpty()){
				if( result != null) {result.put( collection, keyset);}
			}
		}
		
		return result.toString();
//		return new ModelAndView("json", "xResult", result);
	}
	
	@RequestMapping(params="m=getMongoData")
	public String getMongoData(HttpServletRequest request, HttpServletResponse response) /*throws Exception*/ {
		Map<String, Object> param = new HashMap();

		String collectionName =  StringUtils.defaultIfEmpty(request.getParameter("collectionName"), "");
		String key = StringUtils.defaultIfEmpty(request.getParameter("key"), "");

		param.put("collectionName", collectionName );
		param.put("key", key );

		List<Object> resultList = mntService.getMongoData(param);
		if( resultList != null){
			return resultList.toString();
		}else{
			return null;
		}
	}
	
	
	@RequestMapping("/rms/test/{key}/{field}")
	public Object rmsTest(@PathVariable String key, @PathVariable String field) {
		return redisClient.hget(key, field);
	}
}
