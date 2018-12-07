package com.skb.xpg.nxpg.svc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skb.xpg.nxpg.svc.common.ResultCommon;
import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.service.InsideService;
import com.skb.xpg.nxpg.svc.util.DateUtil;
import com.skb.xpg.nxpg.svc.util.LogUtil;
import com.skb.xpg.nxpg.svc.util.StrUtil;

@RestController
@RequestMapping(value = "/{ver}", produces = "application/json; charset=utf-8")
public class InsideController {

	@Autowired
	InsideService insideService;
	
	@Autowired
	private Properties properties;
	
	// IF-NXPG-019
	@RequestMapping(value = "/inside/info")
	public Map<String, Object> getInsideInfo(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		
		param.put("UUID", req.getHeader("UUID"));
		
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("epsd_id"))) {
			rtn.put("result", "9999");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			return rtn;
		}
		
		// 값 불러오기
		try {
			insideService.getInsideInfo(rtn, param);
		} catch (Exception e) {
			LogUtil.error(IF, "SEND.RES", param.get("UUID"), param.get("stb_id"), "REDIS", e.getStackTrace()[0].toString());
			rtn.put("result", "9997");
		}
		
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		LogUtil.tlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", rtn, param);
		return rtn;
	}
}
