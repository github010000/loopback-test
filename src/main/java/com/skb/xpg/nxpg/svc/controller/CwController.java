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
import com.skb.xpg.nxpg.svc.service.CwService;
import com.skb.xpg.nxpg.svc.util.DateUtil;
import com.skb.xpg.nxpg.svc.util.StrUtil;

@RestController
@RequestMapping(value = "/xpg/{ver}", produces = "application/json; charset=utf-8")
public class CwController {

	@Autowired
	CwService cwService;
	
	@Autowired
	private Properties properties;


	@RequestMapping(value = "/inter/cwgrid")
	public Map<String, Object> getCwGrid(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");

		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.putAll(result);

		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
		
		rtn.put("IF", IF);
		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("menu_id"))
				|| StrUtil.isEmpty(param.get("stb_id")) || StrUtil.isEmpty(param.get("cw_call_id"))
				|| StrUtil.isEmpty(param.get("type"))) {
			rtn.put("result", "9999");
			return rtn;
		}
		
		// 값 불러오기
		List<Map<String, Object>> list = cwService.cwGetGrid(ver, param);
		// 조회값 없음
		if (list == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("grid", list);
			rtn.put("total_count", list.size()+"");
		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}
	
	@RequestMapping(value = "/inter/cwrelation")
	public Map<String, Object> getCwRelation(@PathVariable String ver, @RequestParam Map<String, String> param) {
		String IF = param.get("IF");
		
		Map<String, Object> result = properties.getResults();
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.putAll(result);
		
		rtn.put("request_time", DateUtil.getYYYYMMDDhhmmss());
		
		rtn.put("IF", IF);
		if (StrUtil.isEmpty(param.get("menu_stb_svc_id")) || StrUtil.isEmpty(param.get("menu_id"))
				|| StrUtil.isEmpty(param.get("sris_id")) || StrUtil.isEmpty(param.get("epsd_id"))
				|| StrUtil.isEmpty(param.get("cw_call_id")) || StrUtil.isEmpty(param.get("type"))) {
			rtn.put("result", "9999");
			return rtn;
		}
		
		// 값 불러오기
		List<Map<String, Object>> list = cwService.cwGetRelation(ver, param);
		// 조회값 없음
		if (list == null) {
			rtn.put("result", "9998");
		}
		// 성공
		else {
			rtn.put("result", "0000");
			rtn.put("related_info", list);
			rtn.put("total_count", list.size()+"");
			rtn.put("status_code", "0000");

		}
		rtn.put("response_time", DateUtil.getYYYYMMDDhhmmss());
		return rtn;
	}
	
}
