package com.skb.xpg.nxpg.svc.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skb.xpg.nxpg.svc.common.ResultCommon;
import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.service.MenuService;
import com.skb.xpg.nxpg.svc.util.CastUtil;
import com.skb.xpg.nxpg.svc.util.DateUtil;
import com.skb.xpg.nxpg.svc.util.LogUtil;
import com.skb.xpg.nxpg.svc.util.StrUtil;

@RestController
@RequestMapping(value = "/{ver}", produces = "application/json; charset=utf-8")
public class MenuController {

	@Autowired
	MenuService menuService;
	@Autowired
	private Properties properties;
	
	// IF-NXPG-001
	@RequestMapping(value = "/menu/gnb")
	public Map<String, Object> getMenuGnb(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		
	        	
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, String> defaults = properties.getDefaults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		param.put("UUID", req.getHeader("UUID"));
		param.put("time_start", System.nanoTime() + "");
		param.put("redis_count", "0");
		
		rtn.putAll(result);
		
		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
		
		if (defaults != null && StrUtil.isEmpty(param.get("menu_stb_svc_id"))) {
			param.put("menu_stb_svc_id", defaults.get("menu_stb_svc_id"));
		}
		
//		if (StrUtil.isEmpty(param.get("menu_stb_svc_id"))) {
//			rtn.put("result", "9999");
//			return rtn;
//		}
		
		// 값 불러오기 
		try {
			menuService.getMenuGnb(ver, param, rtn);
		} catch (Exception e) {
			LogUtil.error(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "", e.getStackTrace()[0].toString());
			rtn.put("result", "9997");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		}
		
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		param.put("time_end", System.nanoTime() + "");
		LogUtil.rlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", param);
		return rtn;							
		
	}

	// IF-NXPG-002
	@RequestMapping(value = "/menu/all")
	public Map<String, Object> getMenuAll(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, String> defaults = properties.getDefaults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		param.put("UUID", req.getHeader("UUID"));
		param.put("time_start", System.nanoTime() + "");
		param.put("redis_count", "0");

		rtn.putAll(result);
		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (defaults != null && StrUtil.isEmpty(param.get("menu_stb_svc_id"))) {
			param.put("menu_stb_svc_id", defaults.get("menu_stb_svc_id"));
		}
		
		if (StrUtil.isEmpty(param.get("menu_stb_svc_id"))) {
			rtn.put("result", "9999");
			return rtn;
		}
		
		// 값 불러오기 
		try {
			menuService.getMenuAll(ver, param, rtn);
		} catch (Exception e) {
			LogUtil.error(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "", e.getStackTrace()[0].toString());
			rtn.put("result", "9997");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		}
		
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		param.put("time_end", System.nanoTime() + "");
		LogUtil.rlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", param);
		return rtn;
	}

	// IF-NXPG-003
	@RequestMapping(value = "/menu/block")
	public Map<String, Object> getMenuBlock(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, String> defaults = properties.getDefaults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		param.put("UUID", req.getHeader("UUID"));
		param.put("time_start", System.nanoTime() + "");
		param.put("redis_count", "0");

		rtn.putAll(result);
		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
		
		if (defaults != null && StrUtil.isEmpty(param.get("menu_stb_svc_id"))) {
			param.put("menu_stb_svc_id", defaults.get("menu_stb_svc_id"));
		}
		
		if (StrUtil.isEmpty(param.get("menu_id"))) {
			rtn.put("result", "9999");
			rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
			return rtn;
		}
		
		// 값 불러오기 
		Map<String, Object> bigbanner = null;
		Map<String, Object> blockblock = null;
		try {
			blockblock = menuService.getBlockBlock(ver, param);
		} catch (Exception e) {
			LogUtil.error(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "REDIS", e.getStackTrace()[0].toString());
		}
		// 조회값 없음

		try {
			bigbanner = menuService.getBlockBigBanner(ver, param);
		} catch (Exception e) {
			LogUtil.error(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "REDIS", e.getStackTrace()[0].toString());
		}
		
		rtn.put("result", "0000");
		if (bigbanner == null && blockblock == null) {

			rtn.put("result", "9998");
			rtn.put("blocks", null);
			rtn.put("block_count", "0");
			rtn.put("banners", null);
			rtn.put("banner_count", "0");
			
		} else if (bigbanner == null) {

			rtn.putAll(blockblock);
			rtn.put("banners", null);
			rtn.put("banner_count", "0");

		} else if (blockblock == null) {
			
			rtn.put("blocks", null);
			rtn.put("block_count", "0");
			rtn.putAll(bigbanner);
			
		} else {
			rtn.putAll(blockblock);
			rtn.putAll(bigbanner);
			if ((bigbanner.containsKey("result") && "0001".equals(bigbanner.get("result")))
					&& (blockblock.containsKey("result") && "0002".equals(blockblock.get("result")))) {
				rtn.put("result", "0003");
			}
		}
		
//		if (blockblock != null) {
//			rtn.putAll(blockblock);
//		} else {
//		}
			// 카운트 넣어주기 
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		param.put("time_end", System.nanoTime() + "");
		LogUtil.rlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", param);
		return rtn;
	}

	// IF-NXPG-005
	@RequestMapping(value = "/menu/month")
	public Map<String, Object> getMenuMonth(HttpServletRequest req, @PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, String> defaults = properties.getDefaults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		param.put("UUID", req.getHeader("UUID"));
		param.put("time_start", System.nanoTime() + "");
		param.put("redis_count", "0");

		rtn.putAll(result);
		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if ( StrUtil.isEmpty(param.get("menu_id"))) {
			rtn.put("result", "9999");
		} else {
			if (defaults != null && StrUtil.isEmpty(param.get("menu_stb_svc_id"))) {
				param.put("menu_stb_svc_id", defaults.get("menu_stb_svc_id"));
			}
			
			// 값 불러오기 
			try {
				menuService.getBlockMonth(rtn, param);
			} catch (Exception e) {
				LogUtil.error(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "", e.getStackTrace()[0].toString());
				rtn.put("result", "9997");
			}
		}
		
		rtn.put("reason", ResultCommon.reason.get(rtn.get("result")));
		
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		param.put("time_end", System.nanoTime() + "");
		LogUtil.rlog(param.get("IF"), "SEND.RES", param.get("UUID"), param.get("stb_id"), "STB", param);
		return rtn;
	}
	
}
