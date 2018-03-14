package com.skb.xpg.nxpg.svc.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skb.xpg.nxpg.svc.config.Properties;

@RestController
@RequestMapping(value="error", produces="application/json; charset=utf-8")
public class ErrorController {

	@Autowired
	private Properties properties;
	
	@RequestMapping(value = "/401")
	public Map<String, Object> error401() {
		Map<String, Object> rtn = properties.getResults();
		
		rtn.put("result", "401");
		
		return rtn;
	}

	@RequestMapping(value = "/403")
	public Map<String, Object> error403() {
		Map<String, Object> rtn = properties.getResults();
		
		rtn.put("result", "403");
		
		return rtn;
	}
	
	@RequestMapping(value = "/404")
	public Map<String, Object> error404() {
		Map<String, Object> rtn = properties.getResults();
		
		rtn.put("result", "404");
		
		return rtn;
	}

	@RequestMapping(value = "/500")
	public Map<String, Object> error500() {
		Map<String, Object> rtn = properties.getResults();
		
		rtn.put("result", "500");
		
		return rtn;
	}
	
	@RequestMapping(value = "/590")
	public Map<String, Object> error590() {
		Map<String, Object> rtn = properties.getResults();
		
		rtn.put("result", "590");
		
		return rtn;
	}

}
