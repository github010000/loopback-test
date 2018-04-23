package com.skb.xpg.nxpg.svc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.service.CacheService;

/**
 * Created by dmshin on 06/02/2017.
 */
@RestController
@RequestMapping(value = "/{ver}", produces = "application/json; charset=utf-8")
public class TaskController {
	
	@Autowired
	@Qualifier("activeProfile")
	private String activeProfile;
	
	@Autowired
	private CacheService cacheService;

	@RequestMapping(value = "/task/stat")
	public Map<String, Object> getError(@PathVariable String ver, @RequestParam Map<String, String> param) {
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.put("error_count", NXPGCommon.getRedisErrorCount());
		rtn.put("first_redis", NXPGCommon.isUseFirstRedis());
		rtn.put("active_profile", activeProfile);
		
		return rtn;
	}

	@RequestMapping(value = "/task/adderror")
	public Map<String, Object> addError(@PathVariable String ver, @RequestParam Map<String, String> param) {
		Map<String, Object> rtn = new HashMap<String, Object>();
		cacheService.addErrorCountAfterChangeRedis();
		rtn.put("error_count", NXPGCommon.getRedisErrorCount());
		rtn.put("first_redis", NXPGCommon.isUseFirstRedis());
		rtn.put("active_profile", activeProfile);
		
		return rtn;
	}

}
