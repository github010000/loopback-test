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
import com.skb.xpg.nxpg.svc.service.GridService;
import com.skb.xpg.nxpg.svc.util.DateUtil;
import com.skb.xpg.nxpg.svc.util.LogUtil;
import com.skb.xpg.nxpg.svc.util.StrUtil;

@RestController
@RequestMapping(value = "/{ver}", produces = "application/json; charset=utf-8")
public class GridController {

	@Autowired
	GridService gridService;
	@Autowired
	private Properties properties;
	
	// IF-NXPG-006
	@RequestMapping(value = "/grid/grid")
	public Map<String, Object> getGrid(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.putAll(result);
		
		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
		
		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("menu_id"))) {
			rtn.put("result", "9999");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			return rtn;
		}
		
		if (StrUtil.isEmpty(param.get("page_no"))) {
			param.put("page_no", "1");
		}
		
		if (StrUtil.isEmpty(param.get("page_cnt"))) {
			param.put("page_cnt", "30");
		}
		
		// 값 불러오기 
		Map<String, Object> resultMap = null;
		try {
			resultMap = gridService.getGrid(ver, param);
		} catch (Exception e) {
			LogUtil.error(IF, "REQ", "", param.get("stb_id"), "", e.toString());
		}
		
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.putAll(resultMap);
		}
		
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}

	// IF-NXPG-007
	@RequestMapping(value = "/grid/event")
	public Map<String, Object> getGridEvent(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.putAll(result);
		
		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("menu_id"))) {
			rtn.put("result", "9999");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			return rtn;
		}
		
		// 값 불러오기 
		Map<String, Object> resultMap = null;
		try {
			resultMap = gridService.getGridEvent(ver, param);
		} catch (Exception e) {
			LogUtil.error(IF, "REQ", "", param.get("stb_id"), "", e.toString());
		}
		
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.putAll(resultMap);
		}
		
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}
	
}
