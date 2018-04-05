package com.skb.xpg.nxpg.svc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.service.MenuService;
import com.skb.xpg.nxpg.svc.util.DateUtil;
import com.skb.xpg.nxpg.svc.util.StrUtil;

@RestController
@RequestMapping(value = "/xpg/{ver}", produces = "application/json; charset=utf-8")
public class MenuController {

	@Autowired
	MenuService menuService;
	@Autowired
	private Properties properties;
	
	// IF-NXPG-001
	@RequestMapping(value = "/menu/gnb")
	public Map<String, Object> getMenuGnb(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		
		rtn.putAll(result);
		
		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
		
		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("stb_id"))) {
			rtn.put("result", "9999");
			return rtn;
		}
		// 값 불러오기 
		List<Map<String, Object>> resultMap = menuService.getMenuGnb(ver, param);
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("menus", resultMap);
			// 카운트 넣어주기 
			if (resultMap != null) rtn.put("total_count", resultMap.size());
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}

	// IF-NXPG-002
	@RequestMapping(value = "/menu/all")
	public Map<String, Object> getMenuAll(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();

		rtn.putAll(result);
		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
		
		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("stb_id"))) {
			rtn.put("result", "9999");
			return rtn;
		}
		
		// 값 불러오기 
		List<Map<String, Object>> resultMap = menuService.getMenuAll(ver, param);
		// 조회값 없음
		if (resultMap == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("menus", resultMap);
			// 카운트 넣어주기 
			if (resultMap != null) rtn.put("total_count", resultMap.size());
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}

	// IF-NXPG-003
	@RequestMapping(value = "/menu/block")
	public Map<String, Object> getMenuBlock(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();

		rtn.putAll(result);
		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
		
		if (StrUtil.isEmpty(param.get("menu_id")) || StrUtil.isEmpty(param.get("stb_id"))) {
			rtn.put("result", "9999");
			return rtn;
		}
		
		// 값 불러오기 
		Map<String, Object> bigbanner = menuService.getBlockBigBanner(ver, param);
		Map<String, Object> blockblock = menuService.getBlockBlock(ver, param);
		// 조회값 없음
		
		rtn.put("result", "0000");
		if (bigbanner != null) {
			rtn.putAll(bigbanner);
		} else {
			rtn.put("banners", null);
		}
		if (blockblock != null) {
			rtn.putAll(blockblock);
		} else {
			rtn.put("blocks", null);
		}
			// 카운트 넣어주기 
//			if (bigbanner != null) rtn.put("total_count", bigbanner.size());
		
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}

	// IF-NXPG-005
	@RequestMapping(value = "/menu/month")
	public Map<String, Object> getMenuMonth(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();

		rtn.putAll(result);
		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
		
		if (StrUtil.isEmpty(param.get("menu_id")) || StrUtil.isEmpty(param.get("stb_id"))) {
			rtn.put("result", "9999");
			return rtn;
		}
		
		// 값 불러오기 
		Map<String, Object> bigbanner = menuService.getBlockBigBanner(ver, param);
		Map<String, Object> month = menuService.getBlockMonth(ver, param);
		// 조회값 없음
		if (month == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.putAll(bigbanner);
			rtn.putAll(month);
			// 카운트 넣어주기 
//			if (bigbanner != null) rtn.put("total_count", bigbanner.size());
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}
	
}
