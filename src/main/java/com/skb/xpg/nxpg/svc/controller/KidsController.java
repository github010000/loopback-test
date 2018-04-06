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
import com.skb.xpg.nxpg.svc.service.GridService;
import com.skb.xpg.nxpg.svc.service.KidsService;
import com.skb.xpg.nxpg.svc.service.MenuService;
import com.skb.xpg.nxpg.svc.util.DateUtil;
import com.skb.xpg.nxpg.svc.util.StrUtil;

@RestController
@RequestMapping(value = "/xpg/{ver}", produces = "application/json; charset=utf-8")
public class KidsController {

	@Autowired
	MenuService menuService;
	@Autowired
	KidsService kidsService;
	@Autowired
	GridService gridService;
	@Autowired
	private Properties properties;
	
	// IF-NXPG-101
	@RequestMapping(value = "/menu/kzchar")
	public Map<String, Object> getMenuKzchar(@PathVariable String ver, @RequestParam Map<String, String> param) {
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
		List list = kidsService.getMenuKzchar(ver, param);
		// 조회값 없음
		if (list == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("menus", list);
			// 카운트 넣어주기
			if (list != null)
				rtn.put("total_count", list.size());
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}
	
	// IF-NXPG-102
	@RequestMapping(value = "/menu/kzgnb")
	public Map<String, Object> getMenuKzgnb(@PathVariable String ver, @RequestParam Map<String, String> param) {
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
		List list = kidsService.getMenuKzgnb(ver, param);
		// 조회값 없음
		if (list == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("menus", list);
			// 카운트 넣어주기
			if (list != null)
				rtn.put("total_count", list.size());
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}

//	// IF-NXPG-401
//	@RequestMapping(value = "/menu/lftmenu")
//	public Map<String, Object> getMenuLftMenu(@PathVariable String ver, @RequestParam Map<String, String> param) {
//		String IF = param.get("IF");
//		Map<String, Object> result = properties.getResults();
//		Map<String, Object> rtn = new HashMap<String, Object>();
//		rtn.putAll(result);
//
//		rtn.put("IF", IF);
//		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
//
//		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("stb_id"))
//				|| StrUtil.isEmpty(param.get("menu_id"))) {
//			rtn.put("result", "9999");
//			return rtn;
//		}
//
//		// 값 불러오기 
//		Map<String, Object> bigbanner = menuService.getBlockBigBanner(ver, param);
//		// 조회값 없음
//		if (bigbanner == null) {
//			rtn.put("result", "9998");
//		} else {
//			rtn.put("result", "0000");
//			rtn.putAll(bigbanner);
//			// 카운트 넣어주기 
////					if (bigbanner != null) rtn.put("total_count", bigbanner.size());
//		}
//		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
//		return rtn;
//	}
//
//	// IF-NXPG-402
//	@RequestMapping(value = "/grid/lftgrid")
//	public Map<String, Object> getGridLftGrid(@PathVariable String ver, @RequestParam Map<String, String> param) {
//		String IF = param.get("IF");
//		Map<String, Object> result = properties.getResults();
//		Map<String, Object> rtn = new HashMap<String, Object>();
//		rtn.putAll(result);
//
//		rtn.put("IF", IF);
//		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
//
//		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("stb_id"))) {
//			rtn.put("result", "9999");
//			return rtn;
//		}
//
//		// 값 불러오기 
//		Map<String, Object> resultMap = gridService.getGrid(ver, param);
//		// 조회값 없음
//		if (resultMap == null) {
//			rtn.put("result", "9998");
//		}
//		// 성공
//		else {
//			rtn.put("result", "0000");
//			rtn.putAll(resultMap);
//		}
//		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
//		return rtn;
//	}

	// IF-NXPG-403
	@RequestMapping(value = "/contents/lftsynop")
	public Map<String, Object> getContentsLftsynop(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.putAll(result);

		rtn.put("IF", IF);
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());

		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("menu_id"))
				|| StrUtil.isEmpty(param.get("sris_id")) || StrUtil.isEmpty(param.get("epsd_id"))
				|| StrUtil.isEmpty(param.get("stb_id"))) {
			rtn.put("result", "9999");
			return rtn;
		}

		// 값 불러오기
		Map<String, Object> map = kidsService.getContentsLftsynop(ver, param);
		// 조회값 없음
		if (map == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("contents", map);
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}
}
