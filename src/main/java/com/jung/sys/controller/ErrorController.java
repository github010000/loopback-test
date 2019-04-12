package com.jung.sys.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jung.sys.common.ResultCommon;
import com.jung.sys.config.Properties;
import com.jung.sys.util.DateUtil;

@RestController
@RequestMapping(value = "/", produces = "application/json; charset=utf-8")
public class ErrorController {
	
	@Autowired
	private Properties properties;
	
	@RequestMapping(value = "/sleep")
	public Map<String, Object> error(@RequestParam Map<String, String> param) {
		Map<String, Object> result = properties.getResults();

		result.put("IF", param.get("IF"));
		result.put("result", "9998");
		result.put("reason", ResultCommon.reason.get(result.get("result")));
		result.put("response_time", DateUtil.getYYYYMMDDhhmmss());
//		LogUtil.tlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", result, param);
		return result;
	}
}
