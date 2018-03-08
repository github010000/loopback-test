package com.skb.xpg.nxpg.svc.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.svc.SampleService;

@RestController
@RequestMapping(produces = "application/json; charset=utf-8")
public class SampleController {

	@Autowired
	@Qualifier("activeProfile")
	private String profile;

	@Autowired
	private Properties properties;
	
	@Autowired
	private SampleService sampleService;

	@RequestMapping(value = "/base-results")
	public Map<String, Object> sample() {
		return properties.getResults();
	}

	@RequestMapping(value = "/profile")
	public String getActiveProfile() {
		return profile;
	}

	@RequestMapping(value = "/synopsis/{cid}")
	public Map<String, Object> getSynopsis(@PathVariable String cid) {
		Map<String, Object> rtn = properties.getResults();
		rtn.put("contents", sampleService.hgetRedis("Contents", cid));
		return rtn;
	}

	@RequestMapping(value = "/test/{key}/{field}")
	public Map<String, Object> getSample(@PathVariable String key, @PathVariable String field) {
		return sampleService.hgetRedis(key, field);
	}
	
}
