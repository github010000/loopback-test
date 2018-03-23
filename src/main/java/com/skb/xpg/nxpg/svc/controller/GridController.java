package com.skb.xpg.nxpg.svc.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.svc.GridService;
import com.skb.xpg.nxpg.svc.util.DateUtil;
import com.skb.xpg.nxpg.svc.util.StrUtil;

@RestController
@RequestMapping(value = "/nxpg/{ver}", produces = "application/json; charset=utf-8")
public class GridController {

	@Autowired
	GridService gridService;
	@Autowired
	private Properties properties;
	
	// IF-NXPG-006
	@RequestMapping(value = "/grid/grid")
	public Map<String, Object> getGrid(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> rtn = properties.getResults();
		
		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
		
		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("menu_id"))
				|| StrUtil.isEmpty(param.get("stb_id"))) {
			rtn.put("result", "9999");
			return rtn;
		}
		
		// 값 불러오기 
		Map<String, Object> resultMap = gridService.getGrid(ver, param);
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("grid", resultMap);
			// 카운트 넣어주기 
			if (resultMap != null) rtn.put("total_count", resultMap.size());
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}

	// IF-NXPG-007
	@RequestMapping(value = "/grid/event")
	public Map<String, Object> getGridEvent(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> rtn = properties.getResults();
		
		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("menu_id"))
				|| StrUtil.isEmpty(param.get("stb_id"))) {
			rtn.put("result", "9999");
			return rtn;
		}
		
		// 값 불러오기 
		Map<String, Object> resultMap = gridService.getGridEvent(ver, param);
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("event", resultMap);
			// 카운트 넣어주기 
			if (resultMap != null) rtn.put("total_count", resultMap.size());
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}
	
}
