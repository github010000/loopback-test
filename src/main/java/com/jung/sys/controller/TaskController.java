package com.jung.sys.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jung.sys.common.NXPGCommon;
import com.jung.sys.redis.RedisClient;
import com.jung.sys.service.CacheService;
import com.jung.sys.util.DateUtil;
import com.jung.sys.util.LogUtil;

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

    @Autowired
    private RedisClient redisClient;
    
	@RequestMapping(value = "/task/stat")
	public Map<String, Object> getStat(@PathVariable String ver) {
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.put("result", "0000");
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
		rtn.put("use_response_log", LogUtil.useLogForResponseData);
		rtn.put("version_check_is_equal", NXPGCommon.checkVersionEqual);
		rtn.put("instance_index", LogUtil.HostIp);
		rtn.put("check_expiry_date", DateUtil.expiryDate);
		rtn.put("use_cw", NXPGCommon.cwSwitch);
		
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

	// hdel 추가
	@RequestMapping(value = "/hdel/{key}/{field}")
	public String hdel(@PathVariable String key, @PathVariable String field) {
		try {
			return Long.toString(redisClient.hdel(key, field));
		} catch (Exception e) {
			LogUtil.error("", "", "", "", "", e.toString());
			return null;
		}
	}
	
	@RequestMapping(value = "/hget/{key}/{field}")
	public String hget(@PathVariable String key, @PathVariable String field) {
		Map<String, String> rtn = new HashMap<String, String>();
		try {
			return redisClient.hget(key, field, rtn);
		} catch (Exception e) {
			LogUtil.error("", "", "", "", "", e.toString());
			return null;
//			return e.toString();
		}
	}

	@RequestMapping(value = "/task/redis/dofirst")
	public Map<String, Object> doSwitchDoFirst(@PathVariable String ver) {
		NXPGCommon.useFirstRedis = true;
		
		return getStat(ver);
	}

	@RequestMapping(value = "/task/redis/dosecond")
	public Map<String, Object> doSwitchDoSecond(@PathVariable String ver) {
		NXPGCommon.useFirstRedis = false;
		
		return getStat(ver);
	}

//	@RequestMapping(value = "/task/etcstat")
//	public Map<String, Object> getEtcStat(@PathVariable String ver) {
//		Map<String, Object> rtn = new HashMap<String, Object>();
//		rtn.put("result", "0000");
//		
//		return rtn;
//	}

	@RequestMapping(value = "/task/logswitch/on")
	public Map<String, Object> doLogSwitchOn(@PathVariable String ver) {
		LogUtil.useLogForResponseData = true;
		
		return getStat(ver);
	}

	@RequestMapping(value = "/task/logswitch/off")
	public Map<String, Object> doLogSwitchOff(@PathVariable String ver) {
		LogUtil.useLogForResponseData = false;
		
		return getStat(ver);
	}

	@RequestMapping(value = "/task/cwswitch/on")
	public Map<String, Object> doCWSwitchOn(@PathVariable String ver) {
		NXPGCommon.cwSwitch = true;
		
		return getStat(ver);
	}

	@RequestMapping(value = "/task/cwswitch/off")
	public Map<String, Object> doCWSwitchOff(@PathVariable String ver) {
		NXPGCommon.cwSwitch = false;
		
		return getStat(ver);
	}

	@RequestMapping(value = "/task/dateswitch/on")
	public Map<String, Object> doDateSwitchOn(@PathVariable String ver) {
		DateUtil.expiryDate = true;
		
		return getStat(ver);
	}

	@RequestMapping(value = "/task/dateswitch/off")
	public Map<String, Object> doDateSwitchOff(@PathVariable String ver) {
		DateUtil.expiryDate = false;
		
		return getStat(ver);
	}

//	@RequestMapping(value = "/task/versionswitch")
//	public Map<String, Object> doVersionSwitch(@PathVariable String ver) {
//		NXPGCommon.switchCheckVersionEqual();
//		
//		return getEtcStat(ver);
//	}

	// CIMODE 상태 체크
	@RequestMapping(value = "/get-db-mode")
	public Map<String, Object> getcimode(@PathVariable String ver) {
		NXPGCommon.isCIMode();
		
		return getStatCimode();
	}
	
	// CIMODE 적용
	@RequestMapping(value = "/set-db-cimode")
	public Map<String, Object> setcimodeon(@PathVariable String ver) {
		NXPGCommon.onCIMode();
		
		return getStatCimode();
	}

	// CIMODE 해제
	@RequestMapping(value = "/set-db-normal")
	public Map<String, Object> setcimodeoff(@PathVariable String ver) {
		NXPGCommon.offCIMode();
		
		return getStatCimode();
	}

	private Map<String, Object> getStatCimode() {
		Map<String, Object> rtn = new HashMap<String, Object>();
		String svc_name = "nxpg-svc";
		
		rtn.put("svc_name", svc_name);
		rtn.put("result", "success");
		
		if(NXPGCommon.isCIMode()) {
			rtn.put("db_mode", "cimode");
		} else {
			rtn.put("db_mode", "normal");
		}
		
		return rtn;
	}
}