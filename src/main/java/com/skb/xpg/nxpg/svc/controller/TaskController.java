package com.skb.xpg.nxpg.svc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public Map<String, Object> getStat(@PathVariable String ver) {
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.put("error_count", cacheService.getErrorCount());
		rtn.put("active_profile", activeProfile);
		
		if (activeProfile.contains("suy") && NXPGCommon.isUseFirstRedis()) {
			rtn.put("in_use_redis", "수유");
			rtn.put("first_redis", "수유");
			rtn.put("second_redis", "성수");
		} else if (activeProfile.contains("suy") && !NXPGCommon.isUseFirstRedis()) {
			rtn.put("in_use_redis", "성수");
			rtn.put("first_redis", "수유");
			rtn.put("second_redis", "성수");
		} else if (activeProfile.contains("ssu") && NXPGCommon.isUseFirstRedis()) {
			rtn.put("in_use_redis", "성수");
			rtn.put("first_redis", "성수");
			rtn.put("second_redis", "수유");
		} else if (activeProfile.contains("ssu") && !NXPGCommon.isUseFirstRedis()) {
			rtn.put("in_use_redis", "수유");
			rtn.put("first_redis", "성수");
			rtn.put("second_redis", "수유");
		} else {
			rtn.put("in_use_first_redis", NXPGCommon.isUseFirstRedis());
		}
		
		return rtn;
	}

	@RequestMapping(value = "/task/adderror")
	public Map<String, Object> addError(@PathVariable String ver) {
		Map<String, Object> rtn = new HashMap<String, Object>();
		
		rtn.put("error_count", cacheService.addErrorCountAfterChangeRedis());
		rtn.put("first_redis", NXPGCommon.isUseFirstRedis());
		rtn.put("active_profile", activeProfile);
		
		return rtn;
	}

	@RequestMapping(value = "/task/switch")
	public Map<String, Object> doSwitch(@PathVariable String ver) {
		NXPGCommon.switchUseRedis();
		
		return getStat(ver);
	}

	@RequestMapping(value = "/cimode")
	public Map<String, Object> getcimode(@PathVariable String ver) {
		NXPGCommon.switchUseRedis();
		
		return getStat(ver);
	}

	@RequestMapping(value = "/cimode/on")
	public Map<String, Object> setcimodeon(@PathVariable String ver) {
		NXPGCommon.switchUseRedis();
		
		return getStat(ver);
	}

	@RequestMapping(value = "/cimode/off")
	public Map<String, Object> setcimodeoff(@PathVariable String ver) {
		NXPGCommon.switchUseRedis();
		
		return getStat(ver);
	}

}
